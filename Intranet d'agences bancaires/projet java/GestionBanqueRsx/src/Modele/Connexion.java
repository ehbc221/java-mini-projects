package Modele;

import java.sql.*;

public class Connexion {	

		protected Connection con;
		protected PreparedStatement st;
		protected ResultSet rs;
		
		public Connexion(){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banque","root","l32@rd");				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}		
		}
}
