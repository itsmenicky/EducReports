package br.com.educreports.controllers;

import br.com.educreports.dao.ChildDAO;
import br.com.educreports.dao.ReportsDAO;
import br.com.educreports.screens.ReportManagementView;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.util.HashMap;

public class ReportManagementController {
    private ChildDAO childDAO;
    private ReportManagementView view;
    private ReportsDAO reportsDAO;

    public ReportManagementController(ReportManagementView view){
        this.childDAO = new ChildDAO();
        this.view = view;
        this.reportsDAO = new ReportsDAO();
    }

    /**
     * Function responsible for print the selected report
     */
    public void print_report(String id_report) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                HashMap<String, Object> filtro = new HashMap<>();
                filtro.put("ID_Rel", Integer.parseInt(id_report));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/StudentLearningReport.jasper"), filtro, reportsDAO.validateConnection());
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for deleting the selected report
     * @param id_report
     */
    public void deleteReport(String id_report){
        int confirmation = JOptionPane.showConfirmDialog(null, "Deseja excluir este relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            reportsDAO.deleteReport(id_report);
        }
    }

    /**
     * Function responsible for update the report selected content
     * @param id_report
     * @param report
     */
    public void updateReport(String id_report, String report){
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma as alterações no relatório?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            reportsDAO.updateReport(id_report, report);
        }
    }
}