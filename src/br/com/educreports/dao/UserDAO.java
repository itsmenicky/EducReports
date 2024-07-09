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

package br.com.educreports.dao;
import br.com.educreports.dal.ConnectionModule;
import br.com.educreports.models.User;
import br.com.educreports.services.passwordCrypt;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection conexao;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Class constructor
     */
    public UserDAO(){
        conexao = ConnectionModule.conector();
    }

    /**
     * Consult responsible for authenticate credentials in database
     * @param login
     * @param password
     * @return
     */
    public User userLogin(String login, String password){
        String sql = "select * from tb_user where login=? and password=?";

        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, login);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if(rs.next()){
                User userLogged = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5).toCharArray(), rs.getString(6), rs.getString(7));
                return userLogged;
            } else{
                JOptionPane.showMessageDialog(null, "Usuário/Senha incorretos!");
                return null;
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    /**
     * Method responsible for veryfing if the connection to the database is valid
     * @return
     */
    public boolean isConnectionValid() {
        try {
            return conexao != null && !conexao.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consult responsible authenticate a user email in database
     * @param email
     * @return
     */
    public Boolean emailAuth(String email){
        String sql = "select * from tb_user where email like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return false;
    }

    /**
     * Consult responsible for updating user password
     * @param password
     * @param account
     */
    public void updatePassword(String password, String account){
        String sql = "update tb_user set password=? where email like ?";
        try {
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, password);
            pst.setString(2, account);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Senha atualizada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Consult responsible for saving a user in database
     * @param user
     */
    public void save(User user){
        String sql = "insert into tb_user(username, email, login, password, hierarchy) values (?,?,?,?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getLogin());
            byte[] passwordBytes = new String(user.getPassword()).getBytes(StandardCharsets.UTF_8);
            String password = new String(passwordBytes, StandardCharsets.UTF_8);
            pst.setString(4, passwordCrypt.passCrypt(password));
            pst.setString(5, user.getHierarchy());
            int added = pst.executeUpdate();
            if (added > 0) {
                JOptionPane.showMessageDialog(null, "Usuário " + user.getUsername() + " cadastrado com sucesso!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for updating user data in the database
     */
    public void edit_user(User user, String user_id) {
        String sql = "update tb_user set username=?, email=?, login=?, hierarchy=?, status=? where id_user=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getLogin());
            pst.setString(4, user.getHierarchy());
            pst.setString(5, user.getStatus());
            pst.setString(6, user_id);
            int updated = pst.executeUpdate();
            if(updated > 0){
                JOptionPane.showMessageDialog(null, "Alterações salvas!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for search users in the database
     */
    public ResultSet search(String search) {
        String sql = "select id_user as Id, username as Nome, email as Email, login as Login, hierarchy as Perfil, status as Status from tb_user where username like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search + "%");
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
