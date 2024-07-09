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

import br.com.educreports.dao.ChildDAO;
import br.com.educreports.dao.ReportsDAO;
import br.com.educreports.screens.ReportManagementView;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author itsmenicky
 * @version 2.0
 */
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
                reportsDAO.catch_report_child_photo(id_report);
                Blob child_photo = reportsDAO.catch_report_child_photo(id_report);
                filtro.put("child_photo", blob_to_icon(child_photo));
                filtro.put("ID_Rel", Integer.parseInt(id_report));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/StudentLearningReport.jasper"), filtro, reportsDAO.validateConnection());
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public Image blob_to_icon(Blob child_photo_blob) throws SQLException {
        byte[] img = child_photo_blob.getBytes(1, (int) child_photo_blob.length());
        BufferedImage imagem = null;
        try {
            imagem = ImageIO.read(new ByteArrayInputStream(img));
            return imagem;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
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
