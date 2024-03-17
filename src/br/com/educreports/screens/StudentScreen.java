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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.proteanit.sql.DbUtils;
import javax.swing.table.DefaultTableModel;

/**
 * Student management screen
 * 
 * @version 1.0
 * @author Nick1
 */
public class StudentScreen extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    //Instantiating object for byte stream
    private FileInputStream fis;

    //global variable to store the image size (bytes)
    private int size;

    /**
     * Creates new form StudentScreen
     */
    public StudentScreen() {
        initComponents();
        conexao = ConnectionModule.conector();
    }

    /**
     * Function responsible for uploading photo on the system, and setting the photo on the interface
     */
    private void photoUpload() {
        JFileChooser fileExplorer = new JFileChooser();
        fileExplorer.setDialogTitle("Selecionar arquivo");
        fileExplorer.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg"));
        int result = fileExplorer.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                fis = new FileInputStream(fileExplorer.getSelectedFile());
                size = (int) fileExplorer.getSelectedFile().length();
                Image photo = ImageIO.read(fileExplorer.getSelectedFile()).getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(photo));
                lblPhoto.updateUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for search teacher in the database
     */
    private void search_teacher() {
        String sql = "select id_user as ID, username as Nome from tb_user where username like ? and hierarchy='Docente'";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSearchTeacher.getText() + "%");
            rs = pst.executeQuery();
            tbTeacher.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for search students in the database
     */
    private void search_student() {
        String sql = "select RA as RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma from tb_child where child_name like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSearchStudent.getText() + "%");
            rs = pst.executeQuery();
            tbStudent.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting student txt fields and photo on the interface
     */
    private void setStudentFields() {
        int set = tbStudent.getSelectedRow();
        txtRA.setText(tbStudent.getModel().getValueAt(set, 0).toString());
        txtName.setText(tbStudent.getModel().getValueAt(set, 1).toString());
        txtBirth.setText(tbStudent.getModel().getValueAt(set, 2).toString());
        txtClass.setText(tbStudent.getModel().getValueAt(set, 3).toString());
        String sql = "select child_phone, responsible, address, teacher_name, teacher_id, child_photo from tb_child where RA=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtRA.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtResponsible.setText(rs.getString("responsible"));
                txtPhone.setText(rs.getString("child_phone"));
                txtAddress.setText(rs.getString("address"));
                txtTeacherId.setText(rs.getString("teacher_id"));
                txtTeacherName.setText(rs.getString("teacher_name"));
                Blob blob = (Blob) rs.getBlob("child_photo");
                byte[] img = blob.getBytes(1, (int) blob.length());
                BufferedImage imagem = null;
                try {
                    imagem = ImageIO.read(new ByteArrayInputStream(img));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                ImageIcon icone = new ImageIcon(imagem);
                Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH));
                lblPhoto.setIcon(foto);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for setting teacher text fields 
     */
    private void setTeacherFields() {
        int set = tbTeacher.getSelectedRow();
        txtTeacherId.setText(tbTeacher.getModel().getValueAt(set, 0).toString());
        txtTeacherName.setText(tbTeacher.getModel().getValueAt(set, 1).toString());
    }

    /**
     * Function responsible for creating students in the database
     */
    private void addStudent() {
        String sql = "insert into tb_child(RA, child_name, birth, class, child_photo, child_phone, responsible, address, teacher_name, teacher_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            if (txtRA.getText().isBlank() || txtName.getText().isBlank() || txtBirth.getText().isBlank() || txtClass.getText().isBlank() || txtPhone.getText().isBlank() || txtResponsible.getText().isBlank() || txtAddress.getText().isBlank() || txtTeacherName.getText().isBlank() || txtTeacherId.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                pst.setString(1, txtRA.getText());
                pst.setString(2, txtName.getText());
                String birthString = txtBirth.getText();
                try {
                    //Criando objeto SimpleDateFormat para o formato que a gente deseja
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    //Converte a string para um objeto java.util.date
                    java.util.Date dateUtil = format.parse(birthString);
                    //Converte o objeto do java.util.date para java.sql.date
                    java.sql.Date dateSQL = new java.sql.Date(dateUtil.getTime());
                    pst.setDate(3, dateSQL);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "O campo data deve ser preenchido no formato dd/mm/aaaa");
                }
                pst.setString(4, txtClass.getText());
                pst.setBlob(5, fis, size);
                pst.setString(6, txtPhone.getText());
                pst.setString(7, txtResponsible.getText());
                pst.setString(8, txtAddress.getText());
                pst.setString(9, txtTeacherName.getText());
                pst.setString(10, txtTeacherId.getText());

                int added = pst.executeUpdate();
                if (added > 0) {
                    JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
                    clean_fields();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for cleaning interface text fields, table and setting default icon on photo area
     */
    private void clean_fields() {
        txtRA.setText(null);
        txtName.setText(null);
        txtBirth.setText(null);
        txtClass.setText(null);
        txtPhone.setText(null);
        txtResponsible.setText(null);
        txtAddress.setText(null);
        txtTeacherName.setText(null);
        txtTeacherId.setText(null);
        txtSearchStudent.setText(null);
        txtSearchTeacher.setText(null);
        lblPhoto.setIcon(new ImageIcon(getClass().getResource("/assets/camera-icon.png")));
        ((DefaultTableModel) tbTeacher.getModel()).setRowCount(0);
        ((DefaultTableModel) tbStudent.getModel()).setRowCount(0);
    }

    /**
     * Function responsible for deleting student from database
     */
    private void delete_student() {
        String sql = "delete from tb_child where RA=?";
        int confirmation = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esse aluno?", "Atenção!!", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtRA.getText());
                int removed = pst.executeUpdate();
                if (removed > 0) {
                    JOptionPane.showMessageDialog(null, "Aluno removido com sucesso!");
                    clean_fields();
                    btnEditStudent.setEnabled(false);
                    btnDelStudent.setEnabled(false);
                    btnAddStudent.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Function responsible for update student information in database
     */
    private void edit_student() {
        String sql = "update tb_users class=?, child_photo=?, child_phone=?, responsible=?, address=?, teacher_name=?, teacher_id=? where RA=?";
        int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a atualização dos dados deste usuário?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                if (txtClass.getText().isBlank() || txtPhone.getText().isBlank() || txtResponsible.getText().isBlank() || txtAddress.getText().isBlank() || txtTeacherName.getText().isBlank() || txtTeacherId.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                } else {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtClass.getText());
                    pst.setBlob(2, fis, size);
                    pst.setString(3, txtPhone.getText());
                    pst.setString(4, txtResponsible.getText());
                    pst.setString(5, txtAddress.getText());
                    pst.setString(6, txtTeacherName.getText());
                    pst.setString(7, txtTeacherId.getText());
                    pst.setString(8, txtRA.getText());
                    int updated = pst.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Dados do aluno atualizados com sucesso!");
                        btnAddStudent.setEnabled(true);
                        txtRA.setEnabled(true);
                        txtName.setEnabled(true);
                        txtBirth.setEnabled(true);
                    }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtTeacherId = new javax.swing.JTextField();
        txtRA = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtBirth = new javax.swing.JTextField();
        txtClass = new javax.swing.JTextField();
        txtResponsible = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtTeacherName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTeacher = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbStudent = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtSearchStudent = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtSearchTeacher = new javax.swing.JTextField();
        lblPhoto = new javax.swing.JLabel();
        btnAddStudent = new javax.swing.JButton();
        btnEditStudent = new javax.swing.JButton();
        btnDelStudent = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Alunos");

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(224, 18, 53));
        jLabel1.setText("Alunos");

        jLabel2.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel2.setText("RA do aluno");

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel3.setText("Nome");

        jLabel4.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel4.setText("Data de nascimento");

        jLabel5.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel5.setText("Turma");

        jLabel6.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel6.setText("Nome do responsável");

        jLabel7.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel7.setText("Contato");

        jLabel8.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel8.setText("Endereço");

        jLabel9.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel9.setText("Id do professor");

        jLabel10.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel10.setText("Nome do professor");

        txtTeacherId.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtTeacherId.setEnabled(false);

        txtRA.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtName.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtBirth.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtClass.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtResponsible.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtPhone.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        txtTeacherName.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtTeacherName.setEnabled(false);

        tbTeacher.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        tbTeacher.setModel(new javax.swing.table.DefaultTableModel(
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
        tbTeacher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTeacherMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbTeacher);

        tbStudent.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        tbStudent.setModel(new javax.swing.table.DefaultTableModel(
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
        tbStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStudentMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbStudent);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-icon.png"))); // NOI18N
        jLabel11.setToolTipText("");

        txtSearchStudent.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtSearchStudent.setToolTipText("Pesquisar/Selecionar aluno");
        txtSearchStudent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStudentKeyReleased(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-icon.png"))); // NOI18N
        jLabel12.setToolTipText("");

        txtSearchTeacher.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        txtSearchTeacher.setToolTipText("Selecionar o professor");
        txtSearchTeacher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchTeacherKeyReleased(evt);
            }
        });

        lblPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/camera-icon.png"))); // NOI18N
        lblPhoto.setToolTipText("Carregar foto");
        lblPhoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblPhoto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPhotoMouseClicked(evt);
            }
        });

        btnAddStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/add-user2.png"))); // NOI18N
        btnAddStudent.setContentAreaFilled(false);
        btnAddStudent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStudentActionPerformed(evt);
            }
        });

        btnEditStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit-user2.png"))); // NOI18N
        btnEditStudent.setContentAreaFilled(false);
        btnEditStudent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditStudent.setEnabled(false);
        btnEditStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStudentActionPerformed(evt);
            }
        });

        btnDelStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/remove-user2.png"))); // NOI18N
        btnDelStudent.setContentAreaFilled(false);
        btnDelStudent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelStudent.setEnabled(false);
        btnDelStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelStudentActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel13.setText("* Todos os campos são obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(67, 67, 67)
                        .addComponent(jLabel13)
                        .addGap(84, 84, 84))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(6, 6, 6)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(6, 6, 6)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(6, 6, 6)
                            .addComponent(txtTeacherId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(6, 6, 6)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(6, 6, 6)
                            .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(6, 6, 6)
                            .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(6, 6, 6)
                            .addComponent(txtResponsible, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(6, 6, 6)
                            .addComponent(txtRA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(6, 6, 6)
                            .addComponent(txtTeacherName, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnAddStudent)
                            .addGap(18, 18, 18)
                            .addComponent(btnDelStudent)
                            .addGap(18, 18, 18)
                            .addComponent(btnEditStudent))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtSearchTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(txtSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblPhoto)
                        .addGap(86, 86, 86))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel13))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSearchTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPhoto)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel2))
                            .addComponent(txtRA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel3))
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel4))
                            .addComponent(txtBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel5))
                            .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel6))
                            .addComponent(txtResponsible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel7))
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel8))
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel9))
                            .addComponent(txtTeacherId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel10))
                            .addComponent(txtTeacherName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnAddStudent, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnDelStudent, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(btnEditStudent, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(64, 64, 64))))
        );

        txtSearchStudent.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Event responsible for calling photo upload function
     * @param evt 
     */
    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked
        photoUpload();
    }//GEN-LAST:event_lblPhotoMouseClicked

    /**
     * Event responsible for calling search teacher function
     * @param evt 
     */
    private void txtSearchTeacherKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchTeacherKeyReleased
        search_teacher();
    }//GEN-LAST:event_txtSearchTeacherKeyReleased

    /**
     * Event responsible for calling set teacher fields function
     * @param evt 
     */
    private void tbTeacherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTeacherMouseClicked
        setTeacherFields();
    }//GEN-LAST:event_tbTeacherMouseClicked

    /**
     * Event responsible for calling add student function
     * @param evt 
     */
    private void btnAddStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStudentActionPerformed
        addStudent();
    }//GEN-LAST:event_btnAddStudentActionPerformed

    /**
     * Event responsible for calling search student function
     * @param evt 
     */
    private void txtSearchStudentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStudentKeyReleased
        search_student();
    }//GEN-LAST:event_txtSearchStudentKeyReleased

    /**
     * Event responsible for, with the click on a table field, setting the interface student text fields and configuring student buttons
     * @param evt 
     */
    private void tbStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMouseClicked
        setStudentFields();
        btnAddStudent.setEnabled(false);
        btnDelStudent.setEnabled(true);
        btnEditStudent.setEnabled(true);
        txtRA.setEnabled(false);
        txtName.setEnabled(false);
        txtBirth.setEnabled(false);
    }//GEN-LAST:event_tbStudentMouseClicked

    /**
     * Event responsible for calling delete student function
     * @param evt 
     */
    private void btnDelStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelStudentActionPerformed
        delete_student();
    }//GEN-LAST:event_btnDelStudentActionPerformed

    /**
     * Event responsible for calling edit student function
     * @param evt 
     */
    private void btnEditStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStudentActionPerformed
        edit_student();
    }//GEN-LAST:event_btnEditStudentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddStudent;
    private javax.swing.JButton btnDelStudent;
    private javax.swing.JButton btnEditStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPhoto;
    private javax.swing.JTable tbStudent;
    private javax.swing.JTable tbTeacher;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBirth;
    private javax.swing.JTextField txtClass;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtRA;
    private javax.swing.JTextField txtResponsible;
    private javax.swing.JTextField txtSearchStudent;
    private javax.swing.JTextField txtSearchTeacher;
    private javax.swing.JTextField txtTeacherId;
    private javax.swing.JTextField txtTeacherName;
    // End of variables declaration//GEN-END:variables
}
