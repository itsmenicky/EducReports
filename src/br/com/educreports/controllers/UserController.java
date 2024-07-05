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
     * @param user_email
     */
    public void update_user(User user, String user_id, String user_email){
        Random generator = new Random();
        int mail_code = generator.nextInt(9000) + 1000;
        if(user.isValid()){
            String email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
            sendEmail.mailSender(user_email, mail_code, "Confirmação de email", "Bem vindo(a) ao EducReports!", "Ficamos felizes de ter você conosco, insira o código acima em seu aplicativo para a confirmação de sua conta ;)");
            while (!email_confirmation.equals(Integer.toString(mail_code))) {
                JOptionPane.showMessageDialog(null, "Código inválido!");
                email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
            }
            userDAO.edit_user(user, user_id);
        }
    }
}
