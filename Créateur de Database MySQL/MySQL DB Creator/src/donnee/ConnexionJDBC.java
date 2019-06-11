package donnee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import modele.MonServeur;

public class ConnexionJDBC {
	private Connection con=null;
	//private PreparedStatement st=null;
	//private ResultSet rs=null;
	
	
    public ConnexionJDBC() {
	
    	
    }
    public Connection OuvreConnexion(MonServeur ser) {
    	

         
          try
         {
             Class.forName("com.mysql.jdbc.Driver");
            //premier connexion pas de base de donnée
            	 con=DriverManager.getConnection("jdbc:mysql://"+ser.getNomserveur()+":3306",""+ser.getUserbase()+"",""+ser.getMpasse()+"");
            	 
                
         }
         catch(ClassNotFoundException ex){
             System.out.println("Probleme de pilote de base de donnes");
             JOptionPane.showMessageDialog(null, "Connexion echouée. Cause: Probleme de pilote de base de donnes");
             System.out.println(ex.getMessage());
         }
          catch(SQLException ex){
              System.out.println("Probleme de connexion ou de requete");
              JOptionPane.showMessageDialog(null, "Connexion echouée. Cause: Probleme de connexion a la base");
              System.out.println(ex.getMessage());
          }  
		return con;
		
		
    }
    
    public Connection TesterParam(MonServeur ser) {
    	

        
        try
       {
           Class.forName("com.mysql.jdbc.Driver");
          
          	 //deuxième connexion il ya une base de donnée
          	 con=DriverManager.getConnection("jdbc:mysql://"+ser.getNomserveur()+":3306/"+ser.getNombase()+"",""+ser.getUserbase()+"",""+ser.getMpasse()+"");
          
       }
       catch(ClassNotFoundException ex){
           System.out.println("Probleme de pilote de base de donnes");
             System.out.println(ex.getMessage());
       }
        catch(SQLException ex){
            System.out.println("Probleme de connexion ou de requete");
             System.out.println(ex.getMessage());
        }  
		return con;
		
		
  }

}