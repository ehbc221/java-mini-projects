package donnee;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import modele.MonServeur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;


public class Base {

	private JFrame frame;
	 private java.sql.PreparedStatement st=null;
		private ResultSet rs=null;
		private Connection con=null;
		private JTextField txtserver;
		private JTextField txtuser;
		private JTextField txtbase;
		private JPasswordField txtpasse;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Base window = new Base();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Base() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 391, 308);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.inactiveCaption));
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 353, 37);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblJbasecreator = new JLabel("MySQL DB Creator by Mouhtar");
		lblJbasecreator.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblJbasecreator.setForeground(new Color(51, 102, 153));
		lblJbasecreator.setBounds(10, 11, 321, 14);
		panel.add(lblJbasecreator);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Param\u00E8tre du serveur MySQL", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 58, 353, 159);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		txtserver = new JTextField();
		txtserver.setBounds(178, 19, 139, 27);
		panel_1.add(txtserver);
		txtserver.setColumns(10);
		
		txtuser = new JTextField();
		txtuser.setColumns(10);
		txtuser.setBounds(178, 51, 139, 27);
		panel_1.add(txtuser);
		
		txtbase = new JTextField();
		txtbase.setColumns(10);
		txtbase.setBounds(178, 117, 139, 27);
		panel_1.add(txtbase);
		
		JLabel lblAdresseOuNom = new JLabel("Adresse IP ou nom du serveur");
		lblAdresseOuNom.setFont(new Font("Calibri", Font.PLAIN, 11));
		lblAdresseOuNom.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAdresseOuNom.setBounds(10, 25, 148, 14);
		panel_1.add(lblAdresseOuNom);
		
		JLabel lblNomDutilisateur = new JLabel("Nom d'utilisateur");
		lblNomDutilisateur.setFont(new Font("Calibri", Font.PLAIN, 11));
		lblNomDutilisateur.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNomDutilisateur.setBounds(10, 57, 148, 14);
		panel_1.add(lblNomDutilisateur);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe");
		lblMotDePasse.setFont(new Font("Calibri", Font.PLAIN, 11));
		lblMotDePasse.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMotDePasse.setBounds(10, 90, 148, 14);
		panel_1.add(lblMotDePasse);
		
		JLabel lblBaseDeDonner = new JLabel("Base de donner \u00E0 creer");
		lblBaseDeDonner.setFont(new Font("Calibri", Font.PLAIN, 11));
		lblBaseDeDonner.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBaseDeDonner.setBounds(10, 123, 148, 14);
		panel_1.add(lblBaseDeDonner);
		
		txtpasse = new JPasswordField();
		txtpasse.setBounds(178, 85, 139, 27);
		panel_1.add(txtpasse);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new LineBorder(SystemColor.inactiveCaption));
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(10, 225, 353, 34);
		frame.getContentPane().add(panel_2);
		
		JButton btnCrerLaBase = new JButton("Cr\u00E9er la base");
		btnCrerLaBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MonServeur ser=new MonServeur();
				ser.setMpasse(txtpasse.getText());
				ser.setNomserveur(txtserver.getText());
				ser.setUserbase(txtuser.getText());
				ser.setNombase(txtbase.getText());
				Methodes met=new Methodes();
				
				if(met.CreerBase(ser)){
					JOptionPane.showMessageDialog(null, "Votre base de donnée "+ser.getNombase()+" a été crée");
					
				}else {
					JOptionPane.showMessageDialog(null, "Création echoué : Nom de la base invalide ou existant");
				}
			}
		});
		btnCrerLaBase.setBounds(222, 5, 121, 23);
		panel_2.add(btnCrerLaBase);
		
		
		
	}
}
