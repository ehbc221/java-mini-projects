package rahim.kherrata.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import rahim.kherrata.connexion.MaConnexion;

public class Eleve extends JFrame{
	private JLabel nomLabel,numele,prenoml,datel,poidsl;
	//private JTextField numeleve,nome,prenome, dateT, poids;
	private JFormattedTextField daten =new JFormattedTextField(DateFormat.getDateInstance());
	private JButton b01 = new JButton();
	private JButton b02 = new JButton();
	public Eleve( ) {
		
			this.setVisible(true);
			this. setTitle( "Fen_Ajouter_Elève") ;
			this.setAlwaysOnTop(true);
			this. setSize( 450, 300) ;
			this.setResizable(false);
			this. setLocationRelativeTo( null) ;
			this. getContentPane( ).setLayout( new FlowLayout( )) ;
			JPanel pan = new JPanel( ) ;
			pan. setBackground( Color. white) ;
			pan. setLayout( new BorderLayout( ) ) ;
			this.add(pan);
			JPanel panNom = new JPanel( ) ;
			panNom. setBackground( Color. white) ;
			panNom. setPreferredSize( new Dimension( 420, 250) ) ;
			
			
			JTextField numeleve  = new JTextField();
			numeleve.setPreferredSize(new Dimension(150, 25));
			
		
			JTextField nom  = new JTextField();
			nom.setPreferredSize(new Dimension(150, 25));
			
			
			JTextField prenom  = new JTextField();
			prenom.setPreferredSize(new Dimension(150, 25));
			
			
			JTextField date  = new JTextField();
			date.setPreferredSize(new Dimension(150, 25));
			
			
			JTextField poids  = new JTextField();
			poids.setPreferredSize(new Dimension(150, 25));
		
		
		panNom.setBorder( BorderFactory. createTitledBorder( "Information d'elève : ") ) ;
		numele = new JLabel( "Numero d'elève : ") ;
		numele.setPreferredSize(new Dimension( 150, 25));
		panNom.add(numele);
		panNom.add(numeleve);
		nomLabel = new JLabel("Nom d'elève :    ") ;
		nomLabel.setPreferredSize(new Dimension( 150, 25));
		panNom. add( nomLabel) ;
		panNom. add( nom) ;
		prenoml = new JLabel("Prenom d'elève");
		prenoml.setPreferredSize(new Dimension( 150, 25));
		panNom.add(prenoml);
		panNom.add(prenom);
		datel = new JLabel("Date de Naissance :");
		datel.setPreferredSize(new Dimension( 150, 25));
		panNom.add(datel);
		panNom.add(date);
		poidsl = new JLabel("Poids");
		poidsl.setPreferredSize(new Dimension( 150, 25));
		panNom.add(poidsl);
		panNom.add(poids);
		b01 = new JButton();
		b01.setText("Valider");
		b01.setBackground(Color.WHITE);
		b01.setPreferredSize(new Dimension( 100, 25));
		
		b01.addActionListener( new ActionListener( ) {
		public void actionPerformed( ActionEvent arg0) {
			
			String num = numeleve.getText();
			String no = nom.getText() ;
			String preno = prenom.getText();
			String dn = date.getText();
			float poi = Float.valueOf(poids.getText());
			
			String requete = "INSERT INTO ELEVES VALUES ('"+num+"','"+no+"','"+preno+"','"+dn+"',"+poi+")";
			 long start = System.currentTimeMillis();
		      Statement state;
			try {
				
				state = MaConnexion.getInstance().createStatement();
			int res = state.executeUpdate(requete);
			if(res!=0){
				 //state.execute(requete);
					setVisible( false) ;
					JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès", "info", JOptionPane.INFORMATION_MESSAGE);
					}
				 //state.execute(requete);
					setVisible( false) ;
					//res.close();
					state.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e.getMessage(), "info", JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
			}
		 //On exécute la requête
		     
		}});
		b02 = new JButton();
		b02.setText("Annuler");
		b02.setPreferredSize(new Dimension( 100, 25));
		b02.setBackground(Color.WHITE);
		b02.addActionListener( new ActionListener( ) {
		public void actionPerformed( ActionEvent arg0) {
			 setVisible(false);
		     
		}});
		panNom.add(b01);
		panNom.add(b02);
		pan.add(panNom);
	
	}
		
}
