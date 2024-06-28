package br.com.educreports.controllers;

import br.com.educreports.dao.UserDAO;
import br.com.educreports.models.User;
import br.com.educreports.screens.RegisterView;
import br.com.educreports.services.sendEmail;

import javax.swing.*;
import java.util.Random;

public class RegisterScreenController {
    private UserDAO userDAO;
    private RegisterView view;

    /**
     * Class constructor
     * @param view
     */
    public RegisterScreenController(RegisterView view){
        this.view = view;
        this.userDAO = new UserDAO();
    }

    /**
     * Method responsible for validate user to save in database
     * @param user
     */
    public void register_user(User user){
        if(user.isValid() && !userDAO.emailAuth(user.getEmail())){
            Random generator = new Random();
            int mail_code = generator.nextInt(9000) + 1000;
            String email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
            while (!email_confirmation.equals(Integer.toString(mail_code))) {
                JOptionPane.showMessageDialog(null, "Código inválido!");
                email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", view.HEIGHT);
            }
            sendEmail.mailSender(user.getEmail(), mail_code, "Confirmação de email", "Bem vindo(a) ao EducReports!", "Ficamos felizes de ter você conosco, insira o código acima em seu aplicativo para a confirmação de sua conta ;)");
            userDAO.save(user);
        }else{
            JOptionPane.showMessageDialog(null, "Usuário inválido! ERRO: " + user.getValidationErrors());
        }
    }
}
