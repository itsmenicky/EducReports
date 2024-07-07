package br.com.educreports.controllers;

import br.com.educreports.dao.UserDAO;
import br.com.educreports.models.User;
import br.com.educreports.screens.UserScreenView;
import br.com.educreports.services.sendEmail;

import javax.swing.*;
import java.util.Random;


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
