package rahim.kherrata.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rahim.kherrata.connexion.MaConnexion;

public class Activites extends JFrame{
	private JLabel niveauL =new JLabel();
	private JLabel nonActivL =new JLabel();
	private JLabel equipeL =new JLabel();

	private JTextField niveauT = new JTextField();
	private JComboBox  nonActivT = new JComboBox ();
	private JTextField equipeT = new JTextField();
	
	private Font fenC = new Font("Garamond", 0, 16);
	private Font fenC1 = new Font("Garamond", 1, 16);
	
	private JPanel panC = new JPanel();
	private JPanel panp = new JPanel();
	
	private JButton ok = new JButton();
	private JButton annuler = new JButton();
	
	public Activites(){
		setTitle("Fen-Ajout-Activités");
		setSize(450, 200);
		setVisible(true);
		setLocationRelativeTo(null);
		initLabel();
		initChamps();
		initButton();
		initPanel();
		
	}
	private void initLabel(){
		niveauL.setText("Niveau:");
		niveauL.setPreferredSize(new Dimension(150, 25));
		niveauL.setFont(fenC1);
		
		nonActivL.setText("Nom d'activités:");
		nonActivL.setPreferredSize(new Dimension(150, 25));
		nonActivL.setFont(fenC1);
		
		
		equipeL.setText("Equipe:");
		equipeL.setPreferredSize(new Dimension(150, 25));
		equipeL.setFont(fenC1);
		
	}
	private void initChamps(){
		niveauT.setPreferredSize(new Dimension(150, 25));
		niveauT.setFont(fenC);
		
		nonActivT.setPreferredSize(new Dimension(150, 25));
		nonActivT.setFont(fenC);
		nonActivT. addItem( "gymnastique") ;
		nonActivT. addItem("footing");
		nonActivT. addItem("football");
		nonActivT. addItem( "hand-ball");
		nonActivT. addItem( "basket-ball");
		
		equipeT.setPreferredSize(new Dimension(150, 25));
		equipeT.setFont(fenC);
		
	}
	
	private void initButton(){
		ok.setText("OK");
		ok.setPreferredSize(new Dimension( 100, 25));
		ok.setBackground(Color.WHITE);
		ok.setFont(fenC1);
		ok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				String num =	niveauT.getText() ;
				String eqip =  equipeT.getText();
				String nom =	(String) nonActivT.getSelectedItem() ;
				
				String requete = "INSERT INTO ACTIVITES VALUES ('"+num+"','"+eqip+"','"+nom+"')";
				

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
		panC.add(niveauT);
		
		panC.add(nonActivL);
		panC.add(nonActivT);
		
		panC.add(equipeL);
		panC.add(equipeT);
		
		panC.add(ok);
		panC.add(annuler);
		setContentPane(panC);
		
		
	
	}
}
