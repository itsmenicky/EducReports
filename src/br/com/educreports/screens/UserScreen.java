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
import com.sun.tools.javac.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.security.MessageDigest;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Screen for users management
 * 
 * @version 1.0
 * @author Nick1
 */

public class UserScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form UsersScreen
     */
    public UserScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    /**
     * Function responsible for creating users in the database
     */
    private void add_user() {
        String sql = "insert into tb_user(username, email, login, hierarchy) values (?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserName.getText());
            pst.setString(2, txtUserEmail.getText());
            pst.setString(3, txtUserLogin.getText());
            pst.setString(4, cbProfile.getSelectedItem().toString());
            if (txtUserName.getText().isBlank() || txtUserLogin.getText().isBlank() || cbProfile.getSelectedItem().toString() == " ") {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                int added = pst.executeUpdate();
                if (added > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                    clean_fields();
                    btnDeleteUser.setEnabled(true);
                    btnEditUser.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for clean interface text fields, combobox and table
     */
    private void clean_fields() {
        txtUserId.setText(null);
        txtUserName.setText(null);
        txtUserEmail.setText(null);
        txtUserLogin.setText(null);
        cbProfile.setSelectedItem(null);
        lblSearch.setText(null);
        ((DefaultTableModel) tbUsers.getModel()).setRowCount(0);
    }

    /**
     * Function responsible for search users in the database
     */
    private void search() {
        String sql = "select id_user as Id, username as Nome, email as Email, login as Login, hierarchy as Perfil from tb_user where username like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblSearch.getText() + "%");
            rs = pst.executeQuery();
            tbUsers.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting interface text fields and combobox with database informations
     */
    private void set_fields() {
        int set = tbUsers.getSelectedRow();
        txtUserId.setText(tbUsers.getModel().getValueAt(set, 0).toString());
        txtUserName.setText(tbUsers.getModel().getValueAt(set, 1).toString());
        txtUserLogin.setText(tbUsers.getModel().getValueAt(set, 3).toString());
        Object email = tbUsers.getModel().getValueAt(set, 2);
        if (email != null) {
            txtUserEmail.setText(tbUsers.getModel().getValueAt(set, 2).toString());
        } else {
            txtUserEmail.setText(null);
        }
        cbProfile.setSelectedItem(tbUsers.getModel().getValueAt(set, 4).toString());
    }

    /**
     * Function responsible for updating user data in the database
     */
    private void edit_user() {
        String sql = "update tb_user set username=?, email=?, login=?, hierarchy=? where id_user=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserName.getText());
            pst.setString(2, txtUserEmail.getText());
            pst.setString(3, txtUserLogin.getText());
            pst.setString(4, cbProfile.getSelectedItem().toString());
            pst.setString(5, txtUserId.getText());
            if (txtUserName.getText().isBlank() || txtUserLogin.getText().isBlank() || cbProfile.getSelectedItem().toString() == null) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                int updated = pst.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(null, "Dados atualizados!");
                    clean_fields();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for deleting users from the database
     */
    private void delete_user() {
        String sql = "delete from tb_user where id_user=?";
        try {
            int confirmation = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esse usuário?", "ATENÇÃO!", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUserId.getText());
                int removed = pst.executeUpdate();
                if (removed > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!");
                    clean_fields();
                }
            }
        } catch (Exception e) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbProfile = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtUserLogin = new javax.swing.JTextField();
        txtUserEmail = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsers = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        lblSearch = new javax.swing.JTextField();
        btnDeleteUser = new javax.swing.JButton();
        btnEditUser = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(790, 388));

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(224, 18, 53));
        jLabel1.setText("Usuários");

        jLabel2.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel2.setText("* ID do usuário");

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel3.setText("* Nome");

        jLabel4.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel4.setText("* Login");

        jLabel6.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel6.setText("Email");

        cbProfile.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        cbProfile.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Admin", "Docente" }));
        cbProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProfileActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel7.setText("* Perfil");

        txtUserId.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtUserId.setEnabled(false);

        txtUserName.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtUserLogin.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtUserEmail.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        tbUsers.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        tbUsers.setModel(new javax.swing.table.DefaultTableModel(
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
        tbUsers.setPreferredSize(new java.awt.Dimension(351, 131));
        tbUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsers);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-icon.png"))); // NOI18N

        lblSearch.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        lblSearch.setToolTipText("Pesquisar/Selecionar Usuário");
        lblSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblSearchKeyReleased(evt);
            }
        });

        btnDeleteUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/remove-user.png"))); // NOI18N
        btnDeleteUser.setToolTipText("remover usuário");
        btnDeleteUser.setContentAreaFilled(false);
        btnDeleteUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteUser.setEnabled(false);
        btnDeleteUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteUserMouseClicked(evt);
            }
        });

        btnEditUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit-user.png"))); // NOI18N
        btnEditUser.setToolTipText("editar usuário");
        btnEditUser.setContentAreaFilled(false);
        btnEditUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditUser.setEnabled(false);
        btnEditUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditUserMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel9.setText("* Campos obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtUserLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtUserEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(cbProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDeleteUser)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnEditUser))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(528, 528, 528)
                                .addComponent(jLabel9)))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSearch)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEditUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDeleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtUserLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtUserEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cbProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 26, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProfileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbProfileActionPerformed

    /**
     * Event responsible for calling search function
     * @param evt 
     */
    private void lblSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblSearchKeyReleased
        search();
    }//GEN-LAST:event_lblSearchKeyReleased
/**
 * Event responsible for calling set fields function and configuring interface buttons
 * @param evt 
 */
    private void tbUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsersMouseClicked
        set_fields();
        btnDeleteUser.setEnabled(true);
        btnEditUser.setEnabled(true);
    }//GEN-LAST:event_tbUsersMouseClicked

/**
 * Event responsible for calling edit user function and configuring interface add user button
 * @param evt 
 */
    private void btnEditUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditUserMouseClicked
        edit_user();
    }//GEN-LAST:event_btnEditUserMouseClicked
/**
 * Event responsible for calling delete user function and configuring add user button
 * @param evt 
 */
    private void btnDeleteUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteUserMouseClicked
       delete_user();
    }//GEN-LAST:event_btnDeleteUserMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnEditUser;
    private javax.swing.JComboBox<String> cbProfile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblSearch;
    private javax.swing.JTable tbUsers;
    private javax.swing.JTextField txtUserEmail;
    private javax.swing.JTextField txtUserId;
    private javax.swing.JTextField txtUserLogin;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
