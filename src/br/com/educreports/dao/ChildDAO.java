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

package br.com.educreports.dao;
import java.sql.*;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;

import br.com.educreports.dal.ConnectionModule;
import br.com.educreports.models.Child;
import br.com.educreports.session.userSession;

/**
 * Child data access object
 * @author itsmenicky
 * @version 2.0
 */
public class ChildDAO {
    private Connection        conexao;
    private PreparedStatement pst;
    private ResultSet         rs;

    /**
     * Class constructor
     */
    public ChildDAO() {
        this.conexao = ConnectionModule.conector();
    }

    /**
     * Function responsible for searching child in the database to view table
     */
    public ResultSet search_child_to_table(String search) {
        String sql;

        if (!userSession.getInstance().getUser().getHierarchy().equals("Admin")) {
            sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ? and teacher_name like ? and status like 'Active%'";
        } else {
            sql = "select RA, child_name as Nome, date_format(birth, '%d/%m/%Y') as Nascimento, class as Turma, teacher_name as 'Professor(a)' from tb_child where child_name like ? and status like 'Active%'";
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search + "%");

            if (!userSession.getInstance().getUser().getHierarchy().equals("Admin")) {
                pst.setString(2, userSession.getInstance().getUser().getUsername() + "%");
            }

            rs = pst.executeQuery();

            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return null;
    }

    /**
     * Function responsible for searching last fields to studentView
     * @param search
     * @return
     */
    public ResultSet search_child(String search){
        String sql = "select * from tb_child where RA=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search);
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    /**
     * Function responsible for searching child photo
     * @param RA
     * @return
     */
    public ResultSet search_child_photo(String RA) {
        String sql = "select child_photo from tb_child where RA=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, RA);
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {}

        return null;
    }

    /**
     * Function responsible for searching reports
     * @param search
     * @return
     */
    public ResultSet search_reports(String search) {
        String sql =
            "select ID_Rel as ID, report as Relatório, date_format(emission_date, '%d/%m/%Y') as 'Data de emissão' from tb_reports where child_RA=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, search);
            rs = pst.executeQuery();

            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return null;
    }

    /**
     * Function responsible for saving student into database
     * @param student
     */
    public void save(Child student){
        String sql = "insert into tb_child(child_name, birth, class, child_photo, child_phone, responsible, address, teacher_name, teacher_id, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, student.getName());
            java.util.Date utilDate = student.getBirth_date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            pst.setDate(2, sqlDate);
            pst.setString(3, student.getGrade());
            Blob blob = new SerialBlob(student.getPhoto());
            pst.setBlob(4, blob);
            pst.setString(5, student.getContact());
            pst.setString(6, student.getResponsible());
            pst.setString(7, student.getAddress());
            pst.setString(8, student.getTeacher_name());
            pst.setString(9, student.getTeacher_id().toString());
            pst.setString(10, "Active");
            int added = pst.executeUpdate();
            if(added > 0){
                JOptionPane.showMessageDialog(null, "Aluno " + student.getName() + " cadastrado com sucesso!");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Function responsible for update student data in database
     * @param student
     * @param RA
     */
    public void update(Child student, String RA, byte[] imageBytes){
        String sql = "update tb_child set class=?, child_photo=?, child_phone=?, responsible=?, address=?, teacher_name=?, teacher_id=?, status=? where RA=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, student.getGrade());
            pst.setBytes(2, imageBytes);
            pst.setString(3, student.getContact());
            pst.setString(4, student.getResponsible());
            pst.setString(5, student.getAddress());
            pst.setString(6, student.getTeacher_name());
            pst.setString(7, student.getTeacher_id().toString());
            pst.setString(8, student.getStatus());
            pst.setString(9, RA);
            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Dados do aluno atualizados com sucesso!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
