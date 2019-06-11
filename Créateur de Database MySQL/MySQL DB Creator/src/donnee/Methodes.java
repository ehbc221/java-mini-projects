package donnee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

import modele.MonServeur;

public class Methodes
{
  
    private java.sql.PreparedStatement st=null;
	private ResultSet rs=null;
	private Connection con=null;
   
    public Methodes(){
   
    }
    
  
    public boolean CreerBase(MonServeur ser)
    {
    	 ConnexionJDBC cone=new ConnexionJDBC();
    		con=cone.OuvreConnexion(ser);
    		
    	try
		{
			st=con.prepareStatement("Create database "+ser.getNombase());
			st.executeUpdate();
			
		}
		catch(SQLException ex)
		{
			System.out.println(ex.getMessage());
			return false;
			
			
		}
		return true;
    	
    }
    

}


