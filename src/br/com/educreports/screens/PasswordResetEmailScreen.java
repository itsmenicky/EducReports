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
import java.util.Random;
import javax.swing.JOptionPane;
import br.com.educreports.services.sendEmail;

/**
 * @version 1.1
 * @author Nick1
 */
public class PasswordResetEmailScreen extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form PasswordResetEmailScreen
     */
    public PasswordResetEmailScreen() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        email_field = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Montserrat", 0, 11)); // NOI18N
        jLabel2.setText("Insira seu email");

        email_field.setFont(new java.awt.Font("Montserrat", 0, 11)); // NOI18N
        email_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                email_fieldActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void email_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_email_fieldActionPerformed

    }//GEN-LAST:event_email_fieldActionPerformed

    /**
     * Event responsible for capture the email field and send a confirmation
     * email to the account.
     *
     * @param evt
     */

    private void verifyCode(String mail_address) {
        Random generator = new Random();
        int mail_code = generator.nextInt(9000) + 1000;
        sendEmail.mailSender(mail_address, mail_code);
        String email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
        while (email_confirmation.equals("")) {
            JOptionPane.showMessageDialog(null, "Código inválido!");
            email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
        }
        while (Integer.parseInt(email_confirmation) != mail_code) {
            JOptionPane.showMessageDialog(null, "Código inválido!");
            email_confirmation = JOptionPane.showInputDialog(null, "Insira o código que enviamos ao seu email", "Código de verificação", HEIGHT);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (email_field.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Insira seu email!");
        } else {
            conexao = ConnectionModule.conector();
            String sql = "select * from tb_user where email like ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, email_field.getText());
                String mail_address = email_field.getText();
                rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Um email de confirmação foi enviado para o endereço, verifique sua caixa de entrada.");
                     verifyCode(mail_address);
                     this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Email não encontrado! Insira um endereço válido.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(PasswordResetEmailScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PasswordResetEmailScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PasswordResetEmailScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PasswordResetEmailScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PasswordResetEmailScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField email_field;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
