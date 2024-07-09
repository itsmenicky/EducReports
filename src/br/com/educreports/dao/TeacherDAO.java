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

import br.com.educreports.dal.ConnectionModule;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Teacher data access object
 * @author itsmenicky
 * @version 2.0
 */
public class TeacherDAO {
    private Connection conexao;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Class constructor
     */
    public TeacherDAO() {
        this.conexao = ConnectionModule.conector();
    }

    /**
     * Consult responsible for search teacher in the database
     * @param teacher_name
     * @return
     */
    public ResultSet search_teacher(String teacher_name){
        String sql = "select id_user as ID, username as Nome from tb_user where username like ? and hierarchy='Docente' and status!='Disabled' ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, teacher_name + "%");
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
