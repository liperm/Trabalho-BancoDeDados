package dao;
import java.sql.*;

public class ConectaBanco {
    static final String URL = "jdbc:mysql://localhost:3306/AvaliacaoSAKS01";
    static final String USER = "root";
    static final String PASS = "";
    
    public static Connection createConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        
        return conn;
    }
    
}
