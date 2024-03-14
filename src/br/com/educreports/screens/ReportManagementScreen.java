/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.educreports.screens;
import java.sql.*;
import javax.swing.JOptionPane;
import br.com.educreports.dal.ModuloConexao;
import java.util.HashMap;
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
     * Creates new form ReportManagementScreen
     */
    public ReportManagementScreen() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
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
    
    private void set_reports(){
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
    
    private void set_report_area(){
        int report = tbReports.getSelectedRow();
        txtReport.setText(tbReports.getModel().getValueAt(report, 1).toString());
    }
    
    private void print_report(){
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "ATENÇÃO",  JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            try {
                System.out.println(conexao);
                HashMap<String, Object> filtro = new HashMap<>();
                filtro.put("ID_Rel", Integer.parseInt(id_report));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/Reports/StudentLearningReport.jasper"), filtro, conexao);
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    private void delete_report(){
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Deseja excluir este relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            String sql = "delete from tb_reports where ID_Rel=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, id_report);
                int removed = pst.executeUpdate();
                if(removed > 0){
                    JOptionPane.showMessageDialog(null, "Relatório removido com sucesso!");
                } else{
                    JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível deletar o relatório.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }   
        }
    }
    
    private void update_report(){
        int report = tbReports.getSelectedRow();
        String id_report = tbReports.getModel().getValueAt(report, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma as alterações no relatório?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
        if(confirmation == JOptionPane.YES_OPTION){
            String sql = "update tb_reports report=? where ID_Rel=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtReport.getText());
                pst.setString(2, id_report);
                int updated = pst.executeUpdate();
                if(updated > 0){
                    JOptionPane.showMessageDialog(null, "Relatório atualizado com sucesso!");
                } else{
                    JOptionPane.showMessageDialog(null, "ERRO INESPERADO: Não foi possível atualizar o relatório.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }   
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/print-report.png"))); // NOI18N
        jButton1.setToolTipText("Imprimir relatório");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete-report.png"))); // NOI18N
        jButton2.setToolTipText("Excluir relatório");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit-report.png"))); // NOI18N
        jButton3.setToolTipText("Editar relatório");
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                                        .addComponent(jButton1)
                                        .addGap(69, 69, 69)
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2)))
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
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed

    private void searchBarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyReleased
        search_child();
    }//GEN-LAST:event_searchBarKeyReleased

    private void tbChildKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbChildKeyPressed

    }//GEN-LAST:event_tbChildKeyPressed

    private void tbChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbChildMouseClicked
      set_reports();
    }//GEN-LAST:event_tbChildMouseClicked

    private void tbReportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbReportsMouseClicked
        set_report_area();
    }//GEN-LAST:event_tbReportsMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        print_report();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       delete_report();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       update_report();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
