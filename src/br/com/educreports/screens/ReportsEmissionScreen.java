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
import br.com.educreports.dal.ConnectionModule;
import java.awt.Graphics;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Screen for reports emission
 * 
 * @version 2.0
 * @author itsmenicky
 */
public class ReportsEmissionScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form ReportsEmission
     */
    public ReportsEmissionScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }

    /**
     * Function responsible for searching student on database
     */
    private void search_child() {
        String sql = null;
         if(LoginScreen.Adminprofile == false){
             sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)'  from tb_child where child_name like ? and teacher_name like ? and status!= 'Disabled'";
        }else{
            sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ? and status!='Disabled'";
        }
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblSearch.getText() + "%");
            if(LoginScreen.Adminprofile == false){
                pst.setString(2, MainScreen.menu_username.getText() + "%");
            }
            rs = pst.executeQuery();
            tbChild.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting the interface fields with student database information
     */
    private void set_fields() {
        int set = tbChild.getSelectedRow();
        txtRA.setText(tbChild.getModel().getValueAt(set, 0).toString());
        txtName.setText(tbChild.getModel().getValueAt(set, 1).toString());
        txtBirth.setText(tbChild.getModel().getValueAt(set, 2).toString());
        txtClass.setText(tbChild.getModel().getValueAt(set, 3).toString());
        txtTeacher.setText(tbChild.getModel().getValueAt(set, 4).toString());
        String sql = "select child_photo from tb_child where RA=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtRA.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                Blob blob = (Blob) rs.getBlob("child_photo");
                byte[] img = blob.getBytes(1, (int) blob.length());
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new ByteArrayInputStream(img));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                ImageIcon icon = new ImageIcon(image);
                Icon photo = new ImageIcon(icon.getImage().getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH));
                lblPhoto.setIcon(photo);
            }
        } catch (Exception e) {
        }
    }

   /**
    * Function to convert Icon to Blob
    * @param icon
    * @return blob
    */
    private Blob IconToBlob(Icon icon) {
        try {
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics graphics = image.getGraphics();

            icon.paintIcon(null, graphics, 0, 0);
            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            Blob blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
            
            return blob;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO DE CONVERSÃO: FALHA EM CONVERTER ICON PARA BLOB, CONTATE O SUPORTE");
            return null;
        }
    }

    /**
     * Function responsbile for creating reports into database
     */
    private void create_report() {
        String sql = "insert into tb_reports (child_RA, child_name, birth, class, teacher_name, report) values(?,?, ?,?,?, ?)";
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a emissão deste relatório?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                pst = conexao.prepareStatement(sql);
                if (txtRA.getText().isBlank() || txtBirth.getText().isBlank() || txtClass.getText().isBlank() || txtAreaReport.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                } else {
                    pst.setString(1, txtRA.getText());
                    pst.setString(2, txtName.getText());
                    String dataNasc = txtBirth.getText();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date dateUtil = format.parse(dataNasc);
                    java.sql.Date dataFormatada = new java.sql.Date(dateUtil.getTime());
                    pst.setDate(3, dataFormatada);
                    pst.setString(4, txtClass.getText());
                    pst.setString(5, txtTeacher.getText());
                    pst.setString(6, txtAreaReport.getText());
                    int added = pst.executeUpdate();
                    if (added > 0) {
                        JOptionPane.showMessageDialog(null, "Relatório emitido com sucesso!");
                        clean_fields();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    /**
     * 
     * Function responsible for cleaning reports emission fields
     */
    private void clean_fields(){
        txtRA.setText(null);
        txtName.setText(null);
        txtBirth.setText(null);
        txtClass.setText(null);
        txtTeacher.setText(null);
        txtAreaReport.setText(null);
        lblSearch.setText(null);
        ((DefaultTableModel) tbChild.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtBirth = new javax.swing.JTextField();
        txtClass = new javax.swing.JTextField();
        lblPhoto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbChild = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        lblSearch = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaReport = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtRA = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTeacher = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Emissão de Relatórios");

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(224, 18, 53));
        jLabel1.setText("Emissão de Relatórios");

        jLabel2.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel2.setText("Nome do aluno");

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel3.setText("Data de Nascimento");

        jLabel4.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel4.setText("Turma");

        txtName.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtName.setEnabled(false);

        txtBirth.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtBirth.setEnabled(false);

        txtClass.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtClass.setEnabled(false);

        lblPhoto.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        lblPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/camera-icon.png"))); // NOI18N
        lblPhoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblPhoto.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPhotoMouseClicked(evt);
            }
        });

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
        jScrollPane1.setViewportView(tbChild);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-icon.png"))); // NOI18N

        lblSearch.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        lblSearch.setToolTipText("Selecione a criança");
        lblSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSearchActionPerformed(evt);
            }
        });
        lblSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblSearchKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Montserrat", 0, 16)); // NOI18N
        jLabel9.setText("Relatório");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/report-icon.png"))); // NOI18N

        txtAreaReport.setColumns(20);
        txtAreaReport.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtAreaReport.setRows(5);
        jScrollPane2.setViewportView(txtAreaReport);

        jButton1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton1.setText("Emitir relatório");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel5.setText("RA do aluno");

        txtRA.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtRA.setEnabled(false);
        txtRA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRAActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel6.setText("Professor(a)");

        txtTeacher.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtTeacher.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRA, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblPhoto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(115, 115, 115)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSearch))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(62, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtRA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblPhoto)
                        .addGap(38, 38, 38)
                        .addComponent(jButton1)
                        .addGap(46, 46, 46))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Event responsible for calling search child function
     * @param evt 
     */
    private void lblSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblSearchKeyReleased
        search_child();
    }//GEN-LAST:event_lblSearchKeyReleased
/**
 * Event responsible for calling set fields function
 * @param evt 
 */
    private void tbChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbChildMouseClicked
        set_fields();
    }//GEN-LAST:event_tbChildMouseClicked

    private void lblSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSearchActionPerformed

    private void txtRAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRAActionPerformed
/**
 * Event responsible for calling create report function
 * @param evt 
 */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        create_report();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked

    }//GEN-LAST:event_lblPhotoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPhoto;
    private javax.swing.JTextField lblSearch;
    private javax.swing.JTable tbChild;
    private javax.swing.JTextArea txtAreaReport;
    private javax.swing.JTextField txtBirth;
    private javax.swing.JTextField txtClass;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtRA;
    private javax.swing.JTextField txtTeacher;
    // End of variables declaration//GEN-END:variables
}
