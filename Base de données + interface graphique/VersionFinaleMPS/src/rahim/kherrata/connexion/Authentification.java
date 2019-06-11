package rahim.kherrata.connexion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Authentification extends JFrame {
	
	private JPanel pan1 = new JPanel();
	private JPanel pan = new JPanel();
	
	private JLabel loginl = new JLabel();
	private JLabel passl = new JLabel();
	
	private JTextField logint = new JTextField();
	private JPasswordField passt = new JPasswordField();
	
	private JButton ok = new JButton();
	private JButton nok = new JButton();
	
	private Font fe = new Font("Garamond", 0, 16);
	
	private Dimension de = new Dimension(150, 30);
	public Authentification(){
		setTitle("Authentification");
		setSize(400,200);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initLabel();
		initChamps();
		initButton();
		initPanel();
		
	}
	private void initLabel(){
		loginl.setText("Numero du cours:");
		loginl.setPreferredSize(de);
		loginl.setFont(fe);
		
		passl.setText("Nom du cours:");
		passl.setPreferredSize(de);
		passl.setFont(fe);
		
		
		
	}
	private void initChamps(){
		logint.setPreferredSize(de);
		logint.setFont(fe);
		
		passt.setPreferredSize(de);
		passt.setFont(fe);
		
	}
	
	private void initButton(){
		 Statement state = null;	
		ok.setText("OK");
		ok.setPreferredSize( new Dimension(100, 30));
		ok.setBackground(Color.WHITE);
		ok.setFont(fe);
		ok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				
			     
			}});
		
		nok.setText("Annuler");
		nok.setPreferredSize( new Dimension(100, 30));
		nok.setFont(fe);
		nok.setBackground(Color.WHITE);
		nok.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent arg0) {
				 setVisible(false);
			 //On exécute la requête
			     
			}});
	}
	private void initPanel(){
		pan1.setBackground(Color.WHITE);
		pan1. setPreferredSize( new Dimension( 400, 200) ) ;
		pan1.setBorder( BorderFactory. createTitledBorder( "Authentification : ") ) ;
		
		pan1.add(loginl);
		pan1.add(logint);
		
		pan1.add(passl);
		pan1.add(passt);
		
		pan1.add(ok);
		pan1.add(nok);
		
		
		
		setContentPane(pan1);
		
		
	
	}
	}
