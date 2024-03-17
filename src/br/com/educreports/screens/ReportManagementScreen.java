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
package br.com.educreports.screens;

import java.sql.*;
import javax.swing.JOptionPane;
import br.com.educreports.dal.ModuloConexao;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Nick1
 */
public class ReportManagementScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form ReportManagementScreen and starts conection with
     * database
     */
    public ReportManagementScreen() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    /**
     * Function responsible for searching child in the database
     */
    private void search_child() {
        String sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, searchBar.getText() + "%");
            rs = pst.executeQuery();
            tbChild.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting registered reports according to the
     * selected student
     */
    private void set_reports() {
        int student = tbChild.getSelectedRow();
        String sql = "select ID_Rel as ID, report as Relatório, date_format(emission_date, '%d/%m/%Y') as 'Data de emissão' from tb_reports where child_RA=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tbChild.getModel().getValueAt(student, 0).toString());
            rs = pst.executeQuery();
            tbReports.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting report text area according to the
     * selected report
     */
    private void set_report_area() {
        int report = tbReports.getSelectedRow();
        txtReport.setText(tbReports.getModel().getValueAt(report, 1).toString());
        btnPrintReport.setEnabled(true);
        btnUpdateReport.setEnabled(true);
        btnDeleteReport.setEnabled(true);
    }

    /**
     * Function responsible for print the selected report
     */
    private void print_report() {
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                System.out.println(conexao);
                HashMap<String, Object> filtro = new HashMap<>();
                filtro.put("ID_Rel", Integer.parseInt(id_report));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/StudentLearningReport.jasper"), filtro, conexao);
                JasperViewer.viewReport(print, false);
                clean_fields();
                btnPrintReport.setEnabled(false);
                btnUpdateReport.setEnabled(false);
                btnDeleteReport.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for deleting the selected report
     */
    private void delete_report() {
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Deseja excluir este relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            String sql = "delete from tb_reports where ID_Rel=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, id_report);
                int removed = pst.executeUpdate();
                if (removed > 0) {
                    JOptionPane.showMessageDialog(null, "Relatório removido com sucesso!");
                    clean_fields();
                    btnPrintReport.setEnabled(false);
                    btnUpdateReport.setEnabled(false);
                    btnDeleteReport.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível deletar o relatório.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for update the selected report content
     */
    private void update_report() {
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma as alterações no relatório?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            String sql = "update tb_reports set report=? where ID_Rel=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtReport.getText());
                pst.setString(2, id_report);
                int updated = pst.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(null, "Relatório atualizado com sucesso!");
                    clean_fields();
                    btnPrintReport.setEnabled(false);
                    btnUpdateReport.setEnabled(false);
                    btnDeleteReport.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível atualizar o relatório.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    /**
     * Function responsible for cleaning tables, report text area and search bar
     */
    private void clean_fields() {
        ((DefaultTableModel) tbReports.getModel()).setRowCount(0);
        ((DefaultTableModel) tbChild.getModel()).setRowCount(0);
        txtReport.setText(null);
        searchBar.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        searchBar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbChild = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbReports = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtReport = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnPrintReport = new javax.swing.JButton();
        btnDeleteReport = new javax.swing.JButton();
        btnUpdateReport = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setTitle("Gerenciador de Relatórios");

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(224, 18, 53));
        jLabel1.setText("Gerenciador de Relatórios");

        searchBar.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        searchBar.setText("Pesquisar aluno(a)");
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBarKeyReleased(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-icon.png"))); // NOI18N

        tbChild.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        tbChild.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbChild.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbChildMouseClicked(evt);
            }
        });
        tbChild.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbChildKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbChild);

        tbReports.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        tbReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbReports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbReportsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbReports);

        jLabel2.setFont(new java.awt.Font("Montserrat", 0, 16)); // NOI18N
        jLabel2.setText("Relatórios Cadastrados");

        txtReport.setColumns(20);
        txtReport.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtReport.setRows(5);
        jScrollPane3.setViewportView(txtReport);

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 16)); // NOI18N
        jLabel3.setText("Relatório");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/report-icon.png"))); // NOI18N

        btnPrintReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/print-report.png"))); // NOI18N
        btnPrintReport.setToolTipText("Imprimir relatório");
        btnPrintReport.setContentAreaFilled(false);
        btnPrintReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrintReport.setEnabled(false);
        btnPrintReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintReportActionPerformed(evt);
            }
        });

        btnDeleteReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete-report.png"))); // NOI18N
        btnDeleteReport.setToolTipText("Excluir relatório");
        btnDeleteReport.setContentAreaFilled(false);
        btnDeleteReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteReport.setEnabled(false);
        btnDeleteReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteReportActionPerformed(evt);
            }
        });

        btnUpdateReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit-report.png"))); // NOI18N
        btnUpdateReport.setToolTipText("Editar relatório");
        btnUpdateReport.setContentAreaFilled(false);
        btnUpdateReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateReport.setEnabled(false);
        btnUpdateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(searchBar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(104, 104, 104)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnPrintReport)
                                        .addGap(69, 69, 69)
                                        .addComponent(btnUpdateReport)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnDeleteReport)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(159, 159, 159)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addGap(147, 147, 147)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUpdateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnPrintReport)
                                .addComponent(btnDeleteReport, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed
    /**
     * Event responsible for calling the search_child function
     *
     * @param evt
     */
    private void searchBarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyReleased
        search_child();
    }//GEN-LAST:event_searchBarKeyReleased

    private void tbChildKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbChildKeyPressed

    }//GEN-LAST:event_tbChildKeyPressed
    /**
     * Event responsible for calling the set_reports function
     *
     * @param evt
     */
    private void tbChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbChildMouseClicked
        set_reports();
    }//GEN-LAST:event_tbChildMouseClicked
    /**
     * Event responsible for calling the set_report_area function
     *
     * @param evt
     */
    private void tbReportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbReportsMouseClicked
        set_report_area();
    }//GEN-LAST:event_tbReportsMouseClicked
    /**
     * Event responsible for calling print_report function
     *
     * @param evt
     */
    private void btnPrintReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintReportActionPerformed
        print_report();
    }//GEN-LAST:event_btnPrintReportActionPerformed
    /**
     * Event responsible for calling delete_report function
     *
     * @param evt
     */
    private void btnDeleteReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteReportActionPerformed
        delete_report();
    }//GEN-LAST:event_btnDeleteReportActionPerformed
    /**
     * Event responsible for calling update_report function
     *
     * @param evt
     */
    private void btnUpdateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateReportActionPerformed
        update_report();
    }//GEN-LAST:event_btnUpdateReportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteReport;
    private javax.swing.JButton btnPrintReport;
    private javax.swing.JButton btnUpdateReport;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField searchBar;
    private javax.swing.JTable tbChild;
    private javax.swing.JTable tbReports;
    private javax.swing.JTextArea txtReport;
    // End of variables declaration//GEN-END:variables
}
