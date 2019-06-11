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
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import Modele.Client;

public class FClientsAgence extends JFrame {

	private SpringLayout springLayout;
	private final JLabel nomAgenceLabel = new JLabel();
	private final JLabel nomAgenceValueLabel = new JLabel();
	private final JButton quitterButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private ArrayList<Client> listeClientsAgence;
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FClientsAgence frame = new FClientsAgence();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FClientsAgence() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "listeClientsAgence";
		try {
	        is = FAccueil.serveur.getInputStream();
	        ois = new ObjectInputStream(is);
	        String raction = (String)ois.readObject();
	        if(action.equals(raction)){
	        	ois = new ObjectInputStream(is);
	        	listeClientsAgence = (ArrayList<Client>)ois.readObject();
	        	System.out.println(listeClientsAgence.size());
		        tableau.setModel(new ModeleStatiqueObjetClientsAgence(listeClientsAgence));
				tableau.getSelectionModel().setSelectionInterval(0,0);
	        }
	        
//	        System.out.println("Le nombre d'éléments renvoyés par le serveur est: "+listeAgences.size());
		}catch(IOException ex){
			System.out.println("°_° "+ex.getMessage());
			//JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "Alert", JOptionPane.ERROR_MESSAGE);
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
	
	public FClientsAgence(String pNomAgence) {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "listeClientsAgence";
		nomAgenceValueLabel.setText(pNomAgence);
		try {
	        is = FAccueil.serveur.getInputStream();
	        ois = new ObjectInputStream(is);
	        String raction = (String)ois.readObject();
	        if(action.equals(raction)){
	        	ois = new ObjectInputStream(is);
	        	listeClientsAgence = (ArrayList<Client>)ois.readObject();
		        tableau.setModel(new ModeleStatiqueObjetClientsAgence(listeClientsAgence));
				tableau.getSelectionModel().setSelectionInterval(0,0);
	        }
	        
//	        System.out.println("Le nombre d'éléments renvoyés par le serveur est: "+listeAgences.size());
		}catch(IOException ex){
			System.out.println("°_° "+ex.getMessage());
			//JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "Alert", JOptionPane.ERROR_MESSAGE);
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
		setTitle("Clients Agence");
		
		getContentPane().add(nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceLabel, 35, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceLabel, 35, SpringLayout.WEST, getContentPane());
		nomAgenceLabel.setText("Nom Agence");
		
		getContentPane().add(nomAgenceValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceValueLabel, 35, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceValueLabel, 200, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 220, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 250, SpringLayout.NORTH, getContentPane());
		quitterButton.setText("Quitter");
		
		getContentPane().add(scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 230, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 55, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 15, SpringLayout.WEST, getContentPane());
		
//		tableau.hashCode();
		scrollPane.setViewportView(tableau);
	}
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
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
