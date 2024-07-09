/*
 * Copyright (C) 2024 Nickolas Martins
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package br.com.educreports.controllers;

import br.com.educreports.dao.UserDAO;
import br.com.educreports.screens.PasswordResetView;
import br.com.educreports.services.passwordCrypt;
import br.com.educreports.services.sendEmail;
import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import static java.awt.image.ImageObserver.HEIGHT;

/**
 * @author itsmenicky
 * @version 2.0
 */
public class PasswordResetController {
    private UserDAO userDAO;

    /**
     * Class constructor
     */
    public PasswordResetController(){
        userDAO = new UserDAO();
    }

    /**
     * Method responsible for sending verify code to the email, and request the code to the user
     * @param mail_address
     */
    public void verifyCode(String mail_address) {
        Random generator = new Random();
        int mail_code = generator.nextInt(9000) + 1000;
        sendEmail.mailSender(mail_address, mail_code, "Recuperação de senha", "Código para recuperação de senha da sua conta EducReports", "Se você não solicitou um reset de senha, desconsidere o email.");
        String email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
        while (!email_confirmation.equals(Integer.toString(mail_code))) {
            JOptionPane.showMessageDialog(null, "Código inválido!");
            email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
        }
        PasswordResetView password_reset = new PasswordResetView(mail_address);
        password_reset.setVisible(true);
    }

    /**
     * Method responsible for authenticate email for password reset
     * @param email
     * @return
     */
    public Boolean passResetAuth(String email){
        return userDAO.emailAuth(email);
    }

    /**
     * Method responsible for validate password
     * @return
     */
    public boolean isPasswordValid(char[] password){
        String special_char = "!@#$%^&*()-_=+[]{}|;:',.<>?";
        String upper_case = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String low_case = "abcdefghijklmnopqrstuvwxyz";
        Boolean has_special_char = false;
        Boolean has_uppercase = false;
        Boolean has_lowcase = false;
        Boolean has_number = false;
        for(char character : password){
            if(special_char.contains(String.valueOf(character))){
                has_special_char = true;
            }else if (upper_case.contains(String.valueOf(character))){
                has_uppercase = true;
            }else if (low_case.contains(String.valueOf(character))){
                has_lowcase = true;
            }else if(Character.isDigit(character)){
                has_number = true;
            }

            if(has_special_char == true && has_uppercase == true && has_lowcase == true && has_number == true){
                return true;
            }
        }
        return false;
    }

    /**
     * Method responsible for update user password
     * @param password
     * @param password_confirm
     * @param account
     * @throws NoSuchAlgorithmException
     */
    public void updatePassword(char[] password, char[] password_confirm, String account) throws NoSuchAlgorithmException {
        if(!Arrays.equals(password, password_confirm)){
            JOptionPane.showMessageDialog(null, "As senhas não correspondem");
        }else if (!isPasswordValid(password_confirm)){
            JOptionPane.showMessageDialog(null, "A senha deve conter no mínimo 1 caractere especial, letra maiúscula e minúscula");
        } else{
            byte[] passwordBytes = new String(password_confirm).getBytes(StandardCharsets.UTF_8);
            String passwordString = new String(passwordBytes, StandardCharsets.UTF_8);
            userDAO.updatePassword(passwordCrypt.passCrypt(passwordString), account);
        }
    }
}
