/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaled.medjhoum
 */
public class AbstractSQL {
    
    
    private String driverClass = "com.mysql.jdbc.Driver";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String databaseName = "abonnement_db";
    private String user = "root";
    private String password = "";
    private String dbURL = "jdbc:mysql://"+serverName+":"+portNumber+"/"+databaseName;
    
    private java.sql.Connection dbConnect = null;
    private java.sql.Statement dbStatement = null;
    
    public Boolean connect() {
        try {
            Class.forName(driverClass).newInstance();
            this.dbConnect = DriverManager.getConnection(dbURL, this.user, this.password);
            this.dbStatement = this.dbConnect.createStatement();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Executer une requete SQL
     * @param sql
     * @return resultat de la requete
     */
    public ResultSet execResult(String sql) {
        try {
            ResultSet rs = this.dbStatement.executeQuery(sql);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void exec(String sql){
        try {
            this.dbStatement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fermer la connexion au serveur de DB
     */
    public void close() {
        try {
            this.dbStatement.close();
            this.dbConnect.close();
            this.dbConnect.close();
        } catch (SQLException ex) {
            Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
