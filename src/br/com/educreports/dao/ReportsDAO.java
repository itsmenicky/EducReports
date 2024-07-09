package br.com.educreports.dao;

import java.awt.*;
import java.sql.*;

import java.text.SimpleDateFormat;

import javax.swing.*;

import br.com.educreports.dal.ConnectionModule;

public class ReportsDAO {
    private Connection        conexao;
    private PreparedStatement pst;
    private ResultSet         rs;

    /**
     * Class constructor
     */
    public ReportsDAO() {
        this.conexao = ConnectionModule.conector();
    }

    /**
     * Consult responsible for saving reports in database
     * @param child_RA
     * @param child_name
     * @param child_birth
     * @param child_class
     * @param teacher_name
     * @param report
     */
    public void createReport(String child_RA, String child_name, String child_birth, String child_class,
                             String teacher_name, String report, Blob child_photo) {
        String sql =
            "insert into tb_reports (child_RA, child_name, birth, class, teacher_name, report, child_photo) values(?,?, ?,?,?, ?, ?)";

        try {
            pst = conexao.prepareStatement(sql);

            if (child_RA.isBlank() || child_birth.isBlank() || child_class.isBlank() || report.isBlank()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                pst.setString(1, child_RA);
                pst.setString(2, child_name);

                String           dataNasc      = child_birth;
                SimpleDateFormat format        = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date   dateUtil      = format.parse(dataNasc);
                java.sql.Date    dataFormatada = new java.sql.Date(dateUtil.getTime());

                pst.setDate(3, dataFormatada);
                pst.setString(4, child_class);
                pst.setString(5, teacher_name);
                pst.setString(6, report);
                pst.setBlob(7, child_photo);

                int added = pst.executeUpdate();

                if (added > 0) {
                    JOptionPane.showMessageDialog(null, "Relatório emitido com sucesso!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Consult responsible for deleting reports from database
     * @param id_report
     */
    public void deleteReport(String id_report) {
        String sql = "delete from tb_reports where ID_Rel=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_report);

            int removed = pst.executeUpdate();

            if (removed > 0) {
                JOptionPane.showMessageDialog(null, "Relatório removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível deletar o relatório.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Consult responsible for updating reports in database
     * @param id_report
     * @param report
     */
    public void updateReport(String id_report, String report) {
        String sql = "update tb_reports set report=? where ID_Rel=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, report);
            pst.setString(2, id_report);

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Relatório atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível atualizar o relatório.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public Blob catch_report_child_photo(String id_rel){
        String sql = "select child_photo from tb_reports where ID_Rel like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_rel);
            rs = pst.executeQuery();
            if(rs.next()){
                return rs.getBlob("child_photo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Method responsible for validating connection with the database
     * @return
     */
    public Connection validateConnection() {
        return this.conexao;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
