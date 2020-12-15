/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toko.komputer.pengaturan;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Mia
 */
public class Koneksi {
    
    public Connection getKoneksi(){
        Connection conn=null;
        try {
            conn=DriverManager.getConnection("jdbc:mysql://localhost/db_penjualan","root","");
        } catch (SQLException ex) {
            Logger.getLogger(Koneksi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
  
}
