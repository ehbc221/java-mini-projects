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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rahim.kherrata.connexion.MaConnexion;

public class Cours extends JFrame {
	private JLabel numcoursL =new JLabel();
	private JLabel noncoursL =new JLabel();
	private JLabel nbrcoursL =new JLabel();
	private JLabel anneecoursL =new JLabel();
	private Dimension de = new Dimension(150, 30);

	private JTextField numcoursT = new JTextField();
	private JTextField nomcoursT = new JTextField();
	private JTextField nbrcoursT = new JTextField();
	private JTextField anneecoursT = new JTextField();
	
	private Font fenC = new Font("Garamond", 0, 16);
	private Font fenC1 = new Font("Garamond", 1, 16);
	
	private JPanel panC = new JPanel();
	private JPanel panp = new JPanel();
	
	private JButton ok = new JButton();
	private JButton annuler = new JButton();
	
	public Cours(){
		setTitle("Fen-Ajout-Cours");
		setSize(450, 250);
		setVisible(true);
		setLocationRelativeTo(null);
		initLabel();
		initChamps();
		initButton();
		initPanel();
		
	}
	private void initLabel(){
		numcoursL.setText("Numero du cours:");
		numcoursL.setPreferredSize(de);
		numcoursL.setFont(fenC1);
		
		noncoursL.setText("Nom du cours:");
		noncoursL.setPreferredSize(de);
		noncoursL.setFont(fenC1);
		
		nbrcoursL.setText("Nombre d'heurs:");
		nbrcoursL.setPreferredSize(de);
		nbrcoursL.setFont(fenC1);
		
		anneecoursL.setText("Année :");
		anneecoursL.setPreferredSize(de);
		anneecoursL.setFont(fenC1);
		
		
	}
	private void initChamps(){
		numcoursT.setPreferredSize(de);
		numcoursT.setFont(fenC);
		
		nomcoursT.setPreferredSize(de);
		nomcoursT.setFont(fenC);
		
		nbrcoursT.setPreferredSize(de);
		nbrcoursT.setFont(fenC);
		
		anneecoursT.setPreferredSize(de);
		anneecoursT.setFont(fenC);
		
	}
	
	private void initButton(){
				
		ok.setText("OK");
		ok.setPreferredSize(de);
		ok.setBackground(Color.WHITE);
		ok.setFont(fenC1);
		ok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				String num =	numcoursT.getText() ;
				String nom =	nomcoursT.getText() ;
				int nbr =  Integer.valueOf(nbrcoursT.getText());
				String anne = anneecoursT.getText();
				
				String requete = "INSERT INTO COURS VALUES ('"+num+"','"+nom+"',"+nbr+",'"+anne+"')";
				

				long start = System.currentTimeMillis();
			      Statement state = null;
				try {
					
					state = MaConnexion.getInstance().createStatement();
				  int res =  state.executeUpdate(requete);
				  if(res!=0){
					 //state.execute(requete);
						setVisible( false) ;
						JOptionPane.showMessageDialog(null, "Eneregistrement ajouté avec succès", "info", JOptionPane.INFORMATION_MESSAGE);
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
		annuler.setPreferredSize(de);
		annuler.setFont(fenC1);
		annuler.setBackground(Color.WHITE);
		annuler.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				 setVisible(false);
			 //On exécute la requête
			     
			}});
	}
	private void initPanel(){
		panC.setBackground(Color.WHITE);
		panC. setPreferredSize( new Dimension( 400, 200) ) ;
		panC.setBorder( BorderFactory. createTitledBorder( "Information de cours : ") ) ;
		
		panC.add(numcoursL);
		panC.add(numcoursT);
		
		panC.add(noncoursL);
		panC.add(nomcoursT);
		
		panC.add(nbrcoursL);
		panC.add(nbrcoursT);
		
		panC.add(anneecoursL);
		panC.add(anneecoursT);
	
		panC.add(ok);
		panC.add(annuler);
		setContentPane(panC);
		
		
	
	}
}
