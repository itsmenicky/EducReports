package br.com.educreports.controllers;

import br.com.educreports.dal.ConnectionModule;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.sql.Connection;

public class MainScreenController {

    public MainScreenController(){}

    /**
     * Function responsible for issuing reports on students registered in the system
     */
    public void studentsReport() {
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Connection conexao = ConnectionModule.conector();
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/StudentReport.jasper"), null, conexao);
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for issuing reports on students registered in the system
     */
    public void teachersReport() {
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Connection conexao = ConnectionModule.conector();
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/TeacherReport.jasper"), null, conexao);
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
}
