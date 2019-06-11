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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

import Modele.Agence;

public class FSelectionAgence extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JComboBox<String> comboBox = new JComboBox<String>();
	private final JLabel nomAgenceLabel = new JLabel();
	private final JButton validerButton = new JButton();
	private final JButton quitterButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Agence> listeAgences;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FSelectionAgence frame = new FSelectionAgence();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FSelectionAgence() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String action = "selectionAgence";
		
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
	        	listeAgences = (ArrayList<Agence>)ois.readObject();
	        	if(listeAgences.size() == 0){
	        		JOptionPane.showMessageDialog(null, "Il n'y a aucune agence créée dans la base données", "Erreur", JOptionPane.ERROR_MESSAGE);
	        	}
	        	for(Agence ag : listeAgences){
	        		comboBox.addItem(ag.getLibAgence());
	    		}
	        }
		}catch(IOException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage().toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
		setTitle("Selection Agence");
		
		getContentPane().add(comboBox);
		springLayout.putConstraint(SpringLayout.SOUTH, comboBox, 80, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 215, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceLabel, 59, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceLabel, 40, SpringLayout.WEST, getContentPane());
		nomAgenceLabel.setText("Nom Agence");
		
		getContentPane().add(validerButton);
		validerButton.addActionListener(new ValiderButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, validerButton, 290, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, validerButton, 95, SpringLayout.WEST, getContentPane());
		validerButton.setText("Valider");
		
		getContentPane().add(validerButton);
		springLayout.putConstraint(SpringLayout.NORTH, validerButton, 225, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, validerButton, 75, SpringLayout.WEST, getContentPane());
		validerButton.setText("Valider");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, quitterButton, 290, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 95, SpringLayout.WEST, getContentPane());
		quitterButton.setText("Quitter");
		
		getContentPane().add(quitterButton);
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 0, SpringLayout.NORTH, validerButton);
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 0, SpringLayout.WEST, comboBox);
		quitterButton.setText("Quitter");
	}
	
	
	private class ValiderButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			validerButton_actionPerformed(e);
		}
	}
	
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	protected void validerButton_actionPerformed(ActionEvent e) {
		String action = "listeClientsAgence";
		String nomAgence = comboBox.getSelectedItem().toString();
		try{
			os = FAccueil.serveur.getOutputStream();
	        oos = new ObjectOutputStream(os);
	        oos.writeObject(action);
	        oos.flush();
	        oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(nomAgence);
			oos.flush();
			FClientsAgence fenClientsAgence = new FClientsAgence(nomAgence);
			fenClientsAgence.setVisible(true);
			quitterButton.doClick();
		}catch(Exception ex){
			ex.printStackTrace();
		}
				
	}
	protected void quitterButton_actionPerformed(ActionEvent e) {
		try{
			dispose();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
