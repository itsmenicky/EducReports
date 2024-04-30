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
import java.sql.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.util.Date;
import java.text.DateFormat;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * EducReports main screen
 * 
 * @version 2.0
 * @author itsmenicky
 */
public class MainScreen extends javax.swing.JFrame {

    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setVisible(true);
        initComponents();
    }

    /**
     * Function responsible for issuing reports on students registered in the system
     */
    private void studentsReport() {
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
    private void teachersReport() {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        menu_username = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/desktop-background.jpg"));
        Image image = icon.getImage();
        desktop = new javax.swing.JDesktopPane(){

            public void paintComponent(Graphics g){
                g.drawImage(image,0,0,getWidth(),getHeight(),this);
            }
        };
        jMenuBar1 = new javax.swing.JMenuBar();
        students_menu = new javax.swing.JMenu();
        students_menuitem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menu_teachers_rel = new javax.swing.JMenuItem();
        menu_students_rel = new javax.swing.JMenuItem();
        menu_reportsmanagement = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        users_menu = new javax.swing.JMenu();
        users_menuitem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EducReports");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        setIconImage(Toolkit.getDefaultToolkit().getImage(LoginScreen.class.getResource("/assets/desktop-logo.png")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(65, 168, 97));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/EducReports-headerlogo.png"))); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel5.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Bem Vindo(a)");

        menu_username.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        menu_username.setForeground(new java.awt.Color(255, 255, 255));
        menu_username.setText("Usuário");

        lblDate.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setText("Data");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(menu_username, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 344, Short.MAX_VALUE)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(menu_username))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblDate)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 663, Short.MAX_VALUE)
        );

        jMenuBar1.setBackground(new java.awt.Color(65, 168, 97));
        jMenuBar1.setForeground(new java.awt.Color(65, 168, 97));
        jMenuBar1.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jMenuBar1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jMenuBar1ComponentResized(evt);
            }
        });

        students_menu.setText("Alunos");

        students_menuitem.setText("Cadastro/Edição");
        students_menuitem.setEnabled(false);
        students_menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                students_menuitemActionPerformed(evt);
            }
        });
        students_menu.add(students_menuitem);

        jMenuBar1.add(students_menu);

        jMenu2.setText("Relatórios");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        menu_teachers_rel.setText("Relatório de Docentes");
        menu_teachers_rel.setEnabled(false);
        menu_teachers_rel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_teachers_relActionPerformed(evt);
            }
        });
        jMenu2.add(menu_teachers_rel);

        menu_students_rel.setText("Relatório de Alunos");
        menu_students_rel.setEnabled(false);
        menu_students_rel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_students_relActionPerformed(evt);
            }
        });
        jMenu2.add(menu_students_rel);

        menu_reportsmanagement.setText("Gerenciador de Relatórios");
        menu_reportsmanagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_reportsmanagementActionPerformed(evt);
            }
        });
        jMenu2.add(menu_reportsmanagement);

        jMenuItem5.setText("Emitir Relatório");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        users_menu.setText("Usuários");
        users_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                users_menuActionPerformed(evt);
            }
        });

        users_menuitem.setText("Editar usuário");
        users_menuitem.setEnabled(false);
        users_menuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                users_menuitemActionPerformed(evt);
            }
        });
        users_menu.add(users_menuitem);

        jMenuItem1.setText("Cadastrar usuário");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        users_menu.add(jMenuItem1);

        jMenuBar1.add(users_menu);

        jMenu4.setText("Ajuda");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem7.setText("Sobre");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(desktop)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktop)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * Event responsible for open the ReportsEmission screen
 * @param evt 
 */
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        ReportsEmissionScreen emissao = new ReportsEmissionScreen();
        desktop.add(emissao);
        emissao.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuBar1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jMenuBar1ComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuBar1ComponentResized
/**
 * Event responsible for setting actual date in the system
 * @param evt 
 */
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblDate.setText(formatador.format(data));
    }//GEN-LAST:event_formWindowActivated

    private void users_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_users_menuActionPerformed

    }//GEN-LAST:event_users_menuActionPerformed

    /**
     * Event responsbile for open the Users screen
     * @param evt 
     */
    private void users_menuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_users_menuitemActionPerformed
        UserScreen usuarios = new UserScreen();
        desktop.add(usuarios);
        usuarios.setVisible(true);
    }//GEN-LAST:event_users_menuitemActionPerformed
/**
 * Event responsible for open the Students screen
 * @param evt 
 */
    private void students_menuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_students_menuitemActionPerformed
        StudentScreen alunos = new StudentScreen();
        alunos.setVisible(true);
        desktop.add(alunos);
    }//GEN-LAST:event_students_menuitemActionPerformed
/**
 * Event responsible for calling the studentsReport function
 * @param evt 
 */
    private void menu_students_relActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_students_relActionPerformed
        studentsReport();
    }//GEN-LAST:event_menu_students_relActionPerformed
/**
 * Event responsible for calling the teachersReport function
 * @param evt 
 */
    private void menu_teachers_relActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_teachers_relActionPerformed
        teachersReport();
    }//GEN-LAST:event_menu_teachers_relActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed

    }//GEN-LAST:event_jMenu4ActionPerformed
/**
 * Event responsible for calling the About screen
 * @param evt 
 */
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        AboutScreen about = new AboutScreen();
        about.setVisible(true);
        desktop.add(about);
    }//GEN-LAST:event_jMenuItem7ActionPerformed
/**
 * Event responsible for calling the ReportManagement screen
 * @param evt 
 */
    private void menu_reportsmanagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_reportsmanagementActionPerformed
       ReportManagementScreen reportsmanagement = new ReportManagementScreen();
       reportsmanagement.setVisible(true);
       desktop.add(reportsmanagement);
    }//GEN-LAST:event_menu_reportsmanagementActionPerformed
/**
 * Event responsible for calling the Register screen
 * @param evt 
 */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        RegisterScreen register = new RegisterScreen();
        register.setVisible(true);
        desktop.add(register);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDate;
    public static javax.swing.JMenuItem menu_reportsmanagement;
    public static javax.swing.JMenuItem menu_students_rel;
    public static javax.swing.JMenuItem menu_teachers_rel;
    public static javax.swing.JLabel menu_username;
    public static javax.swing.JMenu students_menu;
    public static javax.swing.JMenuItem students_menuitem;
    public static javax.swing.JMenu users_menu;
    public static javax.swing.JMenuItem users_menuitem;
    // End of variables declaration//GEN-END:variables
}
