package rahim.kherrata.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import rahim.kherrata.connexion.MaConnexion;

public class ACTIVITESPRATIQUES extends JFrame{
	private JLabel niveauL =new JLabel();
	private JLabel numeleL =new JLabel();
	private JLabel nomL =new JLabel();

	private JComboBox niveauAC =new JComboBox();
	private JComboBox  numeleveC =new JComboBox();
	private JComboBox nomAC =new JComboBox();
	
	private Font fenC = new Font("Garamond", 0, 16);
	private Font fenC1 = new Font("Garamond", 0, 16);
	
	private JPanel panC = new JPanel();
	private JPanel panp = new JPanel();
	
	private JButton ok = new JButton();
	private JButton annuler = new JButton();
	
	private String req1E = " select DISTINCT NIVEAU from  ACTIVITES";
	private String req2A = " select DISTINCT numeleve from ELEVES";
	public ACTIVITESPRATIQUES(){
		setTitle("Fen-Ajout-ActivitésPratiquées");
		setSize(450, 200);
		setVisible(true);
		setLocationRelativeTo(null);
		initLabel();
		initCombo();
		initButton();
		initPanel();
		
	}
	private void initLabel(){
		niveauL.setText("Niveau:");
		niveauL.setPreferredSize(new Dimension(150, 25));
		niveauL.setFont(fenC1);
		
		numeleL.setText("Numero eleve:");
		numeleL.setPreferredSize(new Dimension(150, 25));
		numeleL.setFont(fenC1);
		
		nomL.setText("Nom Activité pratiquées:");
		nomL.setPreferredSize(new Dimension(150, 25));
		nomL.setFont(fenC1);
		
	}
	private void initCombo(){
		niveauAC.setPreferredSize(new Dimension(150, 25));
		niveauAC.setFont(fenC);
		niveauAC.setBackground(Color.WHITE);
		 Statement state;ResultSet res;
		try {
			state = MaConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			state.executeQuery(req1E);
			
			res = state.getResultSet();
			while(res.next()){
				niveauAC.addItem(res.getString("NIVEAU"));
			}res.close();state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
		numeleveC.setPreferredSize(new Dimension(150, 25));
		numeleveC.setFont(fenC);
		numeleveC.setBackground(Color.WHITE);
		try {
			state = MaConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			state.executeQuery(req2A);
			
			res = state.getResultSet();
			while(res.next()){
				numeleveC.addItem(res.getString("numeleve"));
			}res.close();state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		nomAC.setPreferredSize(new Dimension(150, 25));
		nomAC.setFont(fenC);
		nomAC.setBackground(Color.WHITE);
		nomAC.addItem( "gymnastique") ;
		nomAC.addItem("footing");
		nomAC. addItem("football");
		nomAC. addItem( "hand-ball");
		nomAC. addItem( "basket-ball");
	}
	
	private void initButton(){
		ok.setText("OK");
		ok.setPreferredSize(new Dimension( 100, 25));
		ok.setBackground(Color.WHITE);
		ok.setFont(fenC1);
		ok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				String niv =	 (String) niveauAC.getSelectedItem() ;
				String numele =	(String) numeleveC.getSelectedItem() ;
				String nom =	(String) nomAC.getSelectedItem() ;
				
				String requete = "INSERT INTO ACTIVITES VALUES ('"+niv+"','"+numele+"','"+nom+"')";
				

				long start = System.currentTimeMillis();
			      Statement state = null;
				try {
					
					state = MaConnexion.getInstance().createStatement();
				  int res =  state.executeUpdate(requete);
				  if(res!=0){
					 //state.execute(requete);
						setVisible( false) ;
						JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès"+start, "info", JOptionPane.INFORMATION_MESSAGE);
						}
						//res.close();
						state.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e.getMessage(), "info", JOptionPane.INFORMATION_MESSAGE);
					e.printStackTrace();
				}	 
			 //On exécute la requête
			     
			}});
		
		annuler.setText("Annuler");
		annuler.setFont(fenC1);
		annuler.setPreferredSize(new Dimension( 100, 25));
		annuler.setBackground(Color.WHITE);
		annuler.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				 setVisible(false);
			 //On exécute la requête
			     
			}});
	}
	private void initPanel(){
		panC.setBackground(Color.WHITE);
		panC. setPreferredSize( new Dimension( 400, 180) ) ;
		panC.setBorder( BorderFactory. createTitledBorder( "Information d'activités : ") ) ;
		
		panC.add(niveauL);
		panC.add(niveauAC);
		
		panC.add(numeleL);
		panC.add(numeleveC);
		
		panC.add(nomL);
		panC.add(nomAC);
		
		panC.add(ok);
		panC.add(annuler);
		setContentPane(panC);
		
		
	
	}
}
