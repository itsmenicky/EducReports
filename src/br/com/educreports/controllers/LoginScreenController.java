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
import br.com.educreports.screens.LoginView;
import br.com.educreports.screens.MainScreenView;
import br.com.educreports.services.sendEmail;
import br.com.educreports.session.userSession;
import static java.awt.image.ImageObserver.HEIGHT;
import javax.swing.*;
import java.util.Random;

/**
 * @author itsmenicky
 * @version 2.0
 */
public class LoginScreenController {
    private UserDAO userDAO;
    private LoginView view;

    /**
     * Class constructor
     * @param view
     */
    public LoginScreenController(LoginView view){
        this.userDAO = new UserDAO();
        this.view = view;
        updateConnectionStatus();
    }

    /**
     * Method responsible for user logon
     */
    public void login(String login, String password) {
        User userLogger = userDAO.userLogin(login, password);
        if(!userLogger.getStatus().equals("Active")) {
            JOptionPane.showMessageDialog(null, "Usuário inativo no sistema, entre em contato com o administrador.");
        }else {
            userSession.getInstance().setUser(userLogger);
            User userLogged = userSession.getInstance().getUser();
            Random generator = new Random();
            int mail_code = generator.nextInt(9999) + 1000;
            sendEmail.mailSender(userLogged.getEmail(), mail_code, "Confirmação de login", "Código para confirmação de login", "Confirme seu login na sua conta EducReports");
            String mail_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
            while (!mail_confirmation.equals(Integer.toString(mail_code))) {
                JOptionPane.showMessageDialog(null, "Código inválido!");
                mail_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
            }
            MainScreenView telaPrincipal = new MainScreenView();
            telaPrincipal.setExtendedState(6);
            telaPrincipal.setVisible(true);
            MainScreenView.menu_username.setText(userLogger.getUsername());
            if (userLogged.getHierarchy().equals("Admin")) {
                MainScreenView.students_menu.setEnabled(true);
                MainScreenView.users_menu.setEnabled(true);
                MainScreenView.menu_students_rel.setEnabled(true);
                MainScreenView.menu_teachers_rel.setEnabled(true);
                MainScreenView.students_menuitem.setEnabled(true);
                MainScreenView.user_edit_menuitem.setEnabled(true);
                MainScreenView.user_register_menuitem.setEnabled(true);
            }
            view.dispose();
        }
    }

    /**
     * Method responsible for change the database connection status icon
     */
    public void updateConnectionStatus(){
        Boolean connectionStatus = userDAO.isConnectionValid();
        view.updateDbIcon(connectionStatus);
    }
}
