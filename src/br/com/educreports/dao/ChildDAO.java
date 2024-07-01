package br.com.educreports.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import br.com.educreports.dal.ConnectionModule;
import br.com.educreports.screens.MainScreenView;
import br.com.educreports.session.userSession;

public class ChildDAO {
    private Connection        conexao;
    private PreparedStatement pst;
    private ResultSet         rs;

    /**
     * Class constructor
     */
    public ChildDAO() {
        this.conexao = ConnectionModule.conector();
    }

    /**
     * Function responsible for searching child in the database
     */
    public ResultSet search_child(String search) {
        String sql;

        if (!userSession.getInstance().getUser().getHierarchy().equals("Admin")) {
            sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ? and teacher_name like ?";
        } else {
            sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ?";
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search + "%");

            if (!userSession.getInstance().getUser().getHierarchy().equals("Admin")) {
                pst.setString(2, userSession.getInstance().getUser().getUsername() + "%");
            }

            rs = pst.executeQuery();

            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return null;
    }

    public ResultSet search_child_photo(String RA) {
        String sql = "select child_photo from tb_child where RA=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, RA);
            rs = pst.executeQuery();

            return rs;
        } catch (Exception e) {}

        return null;
    }

    public ResultSet search_reports(String search) {
        String sql =
            "select ID_Rel as ID, report as Relatório, date_format(emission_date, '%d/%m/%Y') as 'Data de emissão' from tb_reports where child_RA=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search);
            rs = pst.executeQuery();

            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return null;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
