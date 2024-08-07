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

import br.com.educreports.dal.ConnectionModule;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.sql.Connection;

/**
 * @author itsmenicky
 * @version 3.0
 */
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
