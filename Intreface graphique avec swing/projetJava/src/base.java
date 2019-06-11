



import java.lang.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

   
        class base  // classe de connection
     {
     	
    Connection con=null;
   Statement st=null;
   ResultSet rs;
    ResultSetMetaData rsmd;
   JTable jt=null;
   int nbc=0;
   String colonne[];
   String data[][];

	public base()
	{ 
	try  
         {
	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // si on utilise une source de donnée ODBC, il 
	//faut penser à faire la configuration depuis panneau de configuration
	 con=DriverManager.getConnection("jdbc:odbc:Emp");// emp est le nom de la source de donnée
	 /*
	  *   si on utilise une autre source de donnée, exemple mysql 
	  *   String driver = "com.mysql.jdbc.Driver";

        Class.forName(driver);
       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/maBase", "votre nom généralement root", "votre mot de passe");
	  */
	 
	 
	 st=con.createStatement();
	 
	 	 
	}
       	catch(SQLException e)
       		{JOptionPane.showMessageDialog(null,e.getMessage());
	        }
	catch(ClassNotFoundException a)
	{	JOptionPane.showMessageDialog(null,a.getMessage());
	}
	  
	   	
   }
       //**************** constructeur de la clases principale
       
      
      
     
         
         void ajout_conge(int id, String deb,  String fin)throws SQLException
         { String requete="INSERT INTO DemandeConge VALUES (?,?,?)";
         
           PreparedStatement sta=con.prepareStatement(requete);
           sta.setInt(1,id);
           sta.setString(2,deb);
           //sta.setString(3,dat);
           sta.setString(3,fin);
        int a=   sta.executeUpdate();
           if(a>0)
           {
        	   JOptionPane.showMessageDialog(null, "insertion effectué");
           }
           else
           {
        	   JOptionPane.showMessageDialog(null, "il y a une erreur");
           }
         }
         
         void ajout_personnel(int id,String np, String deb,  String fin)throws SQLException
         { String requete="INSERT INTO personnel VALUES (?,?,?,?)";
         
           PreparedStatement sta=con.prepareStatement(requete);
           sta.setInt(1,id);
           sta.setString(2,np);
           sta.setString(3,deb);
           sta.setString(4,fin);
           int a=sta.executeUpdate();
           if(a>0)
           {
        	   JOptionPane.showMessageDialog(null, "insertion effectué");
           }
           else
           {
        	   JOptionPane.showMessageDialog(null, "il y a une erreur");
           }
         }
         //**************************
         
         
         //*************************
        
          	
         JTable select_demandes()
         { String s="select * from DemandeConge";
         	 int nbligne=0;	
    	
    	try{
    		
    		rs=st.executeQuery(s);
    		if (rs==null){
    			JOptionPane.showMessageDialog(null,"aucun réultat");
    		}
    		else{
    		
    		rsmd=rs.getMetaData();
    		nbc= rsmd.getColumnCount();
    		colonne= new String[nbc];
    		for(int i=0;i<nbc;i++){
    		
    			colonne[i]=rsmd.getColumnName(i+1);
    
    	//	colonne[i].setFont(titreFont);
    		}
    			while(rs.next())
    				nbligne++;
    				rs=st.executeQuery(s);
    			data=new String[nbligne][nbc];
    			int i=0;
    			while(rs.next())
    			{	for(int j=0;j<nbc;j++){
    				data[i][j]= rs.getString(j+1);
    				//data[i][j].setFont(titreFont);
    				}
    				i++;
    				
    			}
    			
    			jt= new JTable(data,colonne);

    	//	con.close();
    	}}
    			catch(SQLException e1){JOptionPane.showMessageDialog(null ,e1.getMessage());
    			}
      			return jt;
      			
         } // fin fonction JTable
        
         //********************** 
         void executer_req(int id,String deb,String fin)throws SQLException
         { String requete="INSERT INTO EnConge VALUES (?,?,?)";
         
           PreparedStatement sta=con.prepareStatement(requete);
           sta.setInt(1,id);
           sta.setString(2,deb);
           sta.setString(3,fin);
         	 sta.executeUpdate();
         }
         
         //********************
         void effacer(int id)throws SQLException
         {String requete="DELETE FROM DemandeConge WHERE id=?";
         
           PreparedStatement sta=con.prepareStatement(requete);
           sta.setInt(1,id);
          int a= sta.executeUpdate();
           
         }
         
         public String[]  chercher_liste(String rq)
         {
          String  tab[]=null ;
          try{

         rs=st.executeQuery(rq);
        int n=0;
         while (rs.next())
         {
          n++;
         }
         int i=0;
         tab = new String[n];
         rs=st.executeQuery(rq);
         n=0;

         if(rs!=null)

        while(rs.next())
           {
             tab[i]=rs.getString(n+1);
           i++;
                    
         }

     }catch(SQLException e1){JOptionPane.showMessageDialog(null ,e1.getMessage());

      }
                    return tab;
         }
         
         //********** fin recherche avec jTable

         }