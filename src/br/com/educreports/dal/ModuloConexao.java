package br.com.educreports.dal;

import java.sql.*;
/**
 *
 * @author Nick1
 */
public class ModuloConexao {
    
     public static Connection conector() {
        java.sql.Connection conexao = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/educdb";
        String user = "root";
        String password = "C6621E01C8";
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            return null;
        }
    }
}
