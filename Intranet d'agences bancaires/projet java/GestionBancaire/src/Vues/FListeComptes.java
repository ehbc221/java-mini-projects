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

import Modele.Compte;

public class FListeComptes extends JFrame {

	private SpringLayout springLayout;
	private final JButton passerOprerationButton = new JButton();
	private final JButton quitterListButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Compte> listeComptes;
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FListeComptes frame = new FListeComptes();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FListeComptes() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "listeComptes";
		
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
	        	listeComptes = (ArrayList<Compte>)ois.readObject();
	        	if(listeComptes.size() == 0){
	        		JOptionPane.showMessageDialog(null, "Il n'y a aucun compte créé dans la base données", "Erreur", JOptionPane.ERROR_MESSAGE);
	        	}
		        tableau.setModel(new ModeleStatiqueObjetComptesClient(listeComptes));
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
		setTitle("Liste des Comptes");
		
		getContentPane().add(passerOprerationButton);
		passerOprerationButton.addActionListener(new PasserOprerationButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, passerOprerationButton, 290, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, passerOprerationButton, 95, SpringLayout.WEST, getContentPane());
		passerOprerationButton.setText("Passer opération");
		
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
	
	private class PasserOprerationButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			passerOprerationButton_actionPerformed(e);
		}
	}
	
	private class QuitterListButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterListButton_actionPerformed(e);
		}
	}
	
	protected void passerOprerationButton_actionPerformed(ActionEvent e) {
		int ligne = tableau.getSelectedRow();
		int colonnes = tableau.getColumnCount();		       
				
		Object[] tabCompte = new Object[ colonnes ];
		for ( int colonne = 0; colonne < colonnes; colonne++ ) {  
			tabCompte[ colonne ] = tableau.getValueAt( ligne, colonne );
		}
		String numClient = String.valueOf(tabCompte[0]);
				
		FAjoutOperation fenAjoutOp = new FAjoutOperation(Integer.parseInt(numClient));
		fenAjoutOp.setVisible(true);
		quitterListButton.doClick();		
	}
	
	protected void quitterListButton_actionPerformed(ActionEvent e) {
		try{
			dispose();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

