package br.com.educreports.dao;

import br.com.educreports.dal.ConnectionModule;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherDAO {
    private Connection conexao;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Class constructor
     */
    public TeacherDAO() {
        this.conexao = ConnectionModule.conector();
    }

    /**
     * Consult responsible for search teacher in the database
     * @param teacher_name
     * @return
     */
    public ResultSet search_teacher(String teacher_name){
        String sql = "select id_user as ID, username as Nome from tb_user where username like ? and hierarchy='Docente' and status!='Disabled' ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, teacher_name + "%");
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
