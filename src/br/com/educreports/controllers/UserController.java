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
import br.com.educreports.models.User;
import br.com.educreports.screens.UserScreenView;
import br.com.educreports.services.sendEmail;

import javax.swing.*;
import java.util.Random;

/**
 * @author itsmenicky
 * @version 3.0
 */
public class UserController {
    private UserDAO userDAO;
    private UserScreenView view;

    /**
     * Class constructor
     */
    public UserController(UserScreenView view){
        userDAO = new UserDAO();
        this.view = view;
    }

    /**
     * Function responsible for validate and update user data
     * @param user
     * @param user_id
     * @param user_email_field
     */
    public void update_user(User user, String user_id, String user_email_field, String user_current_email){
        Random generator = new Random();
        int mail_code = generator.nextInt(9000) + 1000;
        if(user.isValidUpdate()){
            if(!user_email_field.equals(user_current_email)){
                String email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
                sendEmail.mailSender(user_email_field, mail_code, "Confirmação de email", "Bem vindo(a) ao EducReports!", "Ficamos felizes de ter você conosco, insira o código acima em seu aplicativo para a confirmação de sua conta ;)");
                while (!email_confirmation.equals(Integer.toString(mail_code))) {
                    JOptionPane.showMessageDialog(null, "Código inválido!");
                    email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
                }
                userDAO.edit_user(user, user_id);
            }
            userDAO.edit_user(user, user_id);
        } else{
            JOptionPane.showMessageDialog(null, "OCORREU UM ERRO AO ATUALIZAR OS DADOS: " + user.getValidationUpdateErrors());
        }
    }
}
