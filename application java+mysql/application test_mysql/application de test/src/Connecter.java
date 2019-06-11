
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author liamsi
 */
public class Connecter {
    Connection con;
public Connecter(){
    try {
Class.forName("com.mysql.jdbc.Driver");
System.out.println("chargement est reussie");
} 
catch(ClassNotFoundException e) {
System.out.println(e);}

try {
con=DriverManager.getConnection("jdbc:mysql://localhost:3306/db","root","");}
catch(SQLException e){System.out.println(e);}

}
Connection ObtenirConnextion(){return con;}

    
}
