package br.com.educreports.dao;

import br.com.educreports.dal.ConnectionModule;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsDAO {
    private Connection conexao;
    private PreparedStatement pst;
    private ResultSet rs;

    public ReportsDAO(){
        this.conexao = ConnectionModule.conector();
    }

    public Connection validateConnection(){
        return this.conexao;
    }

    public void deleteReport(String id_report){
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

    public void updateReport(String id_report, String report){
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
}
