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
package br.com.educreports.services;
import java.sql.*;
import br.com.educreports.dal.ConnectionModule;
import br.com.educreports.screens.LoginScreen;

/**
 *
 * @author Nick1
 * @version 2.0
 */
public class checkUser {
    
    public static Boolean check_user(){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Boolean authSession = false;
        
        String sql = "select * from tb_user where id_user=?";
        try {
            conexao = ConnectionModule.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, LoginScreen.userIDSession);
            rs = pst.executeQuery();
            if(rs.next()){
                String userStatus = rs.getString(7);
                if(userStatus.equals("Active")){
                    authSession = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return authSession;
    }
}
