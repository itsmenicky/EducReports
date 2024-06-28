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
                JOptionPane.showMessageDialog(null, "Usuário " + user.getUsername() + "cadastrado com sucesso!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
