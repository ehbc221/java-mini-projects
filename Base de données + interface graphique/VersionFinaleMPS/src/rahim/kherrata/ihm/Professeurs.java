package rahim.kherrata.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rahim.kherrata.connexion.MaConnexion;

public class Professeurs extends JFrame{
	private JLabel nomp,numep,datep,salairep;
	private JTextField numeprof,nom,specialite,datentre, salaire;
	private JButton b01 = new JButton();
	private JButton b02 = new JButton();
	JPanel pan = new JPanel( ) ;
	JPanel panNom = new JPanel( ) ;
	public Professeurs(){
		
		
			setVisible(true);
			setTitle( "Fen_Ajouter_Professeurs") ;
			setAlwaysOnTop(true);
			setSize( 450, 250) ;
			setResizable(false);
			setLocationRelativeTo( null) ;
			initlabel();
			initchamps();
			initboutton();
			initpanel();
			add(pan);
			
		}
	private void initlabel(){
		numep = new JLabel( "Numero de professeur : ") ;
		numep.setPreferredSize(new Dimension( 150, 30));
		
		nomp = new JLabel("Nom de professseurs :    ") ;
		nomp.setPreferredSize(new Dimension( 150, 30));
		
		datep = new JLabel("Date d'entree :");
		datep.setPreferredSize(new Dimension( 150, 30));
		
		salairep = new JLabel("Salaire");
		salairep.setPreferredSize(new Dimension( 150, 30));
	}
	private void initchamps(){
		numeprof = new JTextField();
		nom = new JTextField( ) ;
		datentre = new JTextField( ) ;
		specialite = new JTextField( ) ;
		salaire = new JTextField( ) ;
		
		numeprof. setPreferredSize( new Dimension( 150, 30) ) ;
		nom. setPreferredSize( new Dimension( 150, 30) ) ;
		datentre. setPreferredSize( new Dimension( 150, 30) ) ;
		specialite. setPreferredSize( new Dimension( 150, 30) ) ;
		salaire. setPreferredSize( new Dimension( 150, 30) ) ;
		
		
	}
	private void initboutton(){
		b01 = new JButton();
		b01.setText("Valider");
		b01.setBackground(Color.WHITE);
		b01.setPreferredSize(new Dimension( 150, 30));
		b01.addActionListener( new ActionListener( ) {
		public void actionPerformed( ActionEvent arg0) {
			 long start = System.currentTimeMillis();
		      Statement state;
		      String num = numeprof.getText(); 
			  String no = nom.getText(); 
			  String de = datentre.getText(); 
			  String sp = specialite.getText();
			  double salr = Double.valueOf(salaire.getText()); 
		      String requete = "insert into PROFESSEURS values('"+num+"','"+no+"','"+de+"','"+sp+"',"+salr+")";
			try {
				
				state = MaConnexion.getInstance().createStatement();
				  int res =  state.executeUpdate(requete);
				  if(res!=0){
					 //state.execute(requete);
						setVisible( false) ;
						JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès", "info", JOptionPane.INFORMATION_MESSAGE);
						}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès", "erreur", JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
			}
		 //On exécute la requête
		     
		}});
		b02 = new JButton();
		b02.setText("Annuler");
		b02.setBackground(Color.WHITE);
		b02.setPreferredSize(new Dimension( 150, 30));
		b02.addActionListener( new ActionListener( ) {
		public void actionPerformed( ActionEvent arg0) {
			 setVisible(false);
		     
		}});
		
		
	}
	private void initpanel(){
		pan. setBackground( Color. white) ;
		pan. setLayout( new BorderLayout( ) ) ;
		
		panNom. setBackground( Color. white) ;
		panNom. setPreferredSize( new Dimension( 400, 200) ) ;
		
		panNom.setBorder( BorderFactory. createTitledBorder( "Information de professeur : ") ) ;
		
		panNom.add(numep);
		panNom.add(numeprof);
		
		panNom. add( nomp) ;
		panNom. add( nom) ;
		
		panNom.add(datep);
		panNom.add(datentre);
		
		panNom.add(salairep);
		panNom.add(salaire);
		
		panNom.add(b01);
		panNom.add(b02);
		pan.add(panNom);
	
	}
	
	}


