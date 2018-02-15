/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
public class DerbyConnUtils {

    public static Connection getDerbyConnection()
            throws ClassNotFoundException, SQLException {
        String hostName = "localhost";
        String dbName = "gestiondocumentaria";
        String userName = "sgdf";
        String password = "Finanz62";
        return getDerbyConnection(hostName, dbName, userName, password);
    }

    public static Connection getDerbyConnection(String hostName, String dbName,
            String userName, String password) throws SQLException,
            ClassNotFoundException {
        
        String connectionURL = "jdbc:derby:" + dbName;

        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);
        return conn;
    }
}
