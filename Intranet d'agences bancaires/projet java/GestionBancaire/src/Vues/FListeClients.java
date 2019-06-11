package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import Modele.Client;

public class FListeClients extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JButton creerCompteButton = new JButton();
	private final JButton quitterListButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Client> listeClients;
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FListeClients frame = new FListeClients();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FListeClients() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "listeClient";
		
		//j'envoie les données au serveur pour exécution de la requête {étape 1}
		try {
	        os = FAccueil.serveur.getOutputStream();
	        oos = new ObjectOutputStream(os);
	        oos.writeObject(action);
	        oos.flush();
	        is = FAccueil.serveur.getInputStream();
	        ois = new ObjectInputStream(is);
	        String raction = (String)ois.readObject();
	        if(action.equals(raction)){
	        	ois = new ObjectInputStream(is);
	        	listeClients = (ArrayList<Client>)ois.readObject();
	        	if(listeClients.size() == 0){
	        		JOptionPane.showMessageDialog(null, "Cette agence ne compte aucun client", "Erreur", JOptionPane.ERROR_MESSAGE);
	        	}
		        tableau.setModel(new ModeleStatiqueObjetClient(listeClients));
				tableau.getSelectionModel().setSelectionInterval(0,0);
	        }
	        
//	        System.out.println("Le nombre d'éléments renvoyés par le serveur est: "+listeAgences.size());
		}catch(IOException ex){
//			System.out.println("°_° "+ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//
	}
	private void jbInit() throws Exception {
		springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		getContentPane().add(creerCompteButton);
		springLayout.putConstraint(SpringLayout.NORTH, creerCompteButton, 280, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, creerCompteButton, 105, SpringLayout.WEST, getContentPane());
		creerCompteButton.setText("Créer Compte");
		
		getContentPane().add(creerCompteButton);
		creerCompteButton.addActionListener(new CreerCompteButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, creerCompteButton, 290, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, creerCompteButton, 95, SpringLayout.WEST, getContentPane());
		creerCompteButton.setText("Créer Compte");
		
		getContentPane().add(quitterListButton);
		springLayout.putConstraint(SpringLayout.EAST, quitterListButton, 390, SpringLayout.WEST, getContentPane());
		quitterListButton.addActionListener(new QuitterListButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterListButton, 264, SpringLayout.NORTH, getContentPane());
		quitterListButton.setText("Quitter");
		
		getContentPane().add(scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 230, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 55, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 15, SpringLayout.WEST, getContentPane());
		
//		tableau.hashCode();
		scrollPane.setViewportView(tableau);
	}
	
	private class CreerCompteButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			creerCompteButton_actionPerformed(e);
		}
	}
	
	private class QuitterListButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	protected void creerCompteButton_actionPerformed(ActionEvent e) {
		int ligne = tableau.getSelectedRow();
		int colonnes = tableau.getColumnCount();		       
				
		Object[] tabAgence = new Object[ colonnes ];
		for ( int colonne = 0; colonne < colonnes; colonne++ ) {  
			tabAgence[ colonne ] = tableau.getValueAt( ligne, colonne );
		}
		String numClient = String.valueOf(tabAgence[0]);
		String nomAgence = (String)tabAgence[4];
				
		FCreerCompte fenCreerCompte = new FCreerCompte(Integer.parseInt(numClient), nomAgence, this);
		fenCreerCompte.setVisible(true);
		quitterListButton.doClick();		
	}
	protected void quitterButton_actionPerformed(ActionEvent e) {
		try{
			dispose();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
