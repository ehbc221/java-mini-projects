package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import Modele.Compte;
import Modele.Operation;

public class FReleveCompte extends JFrame {

	private SpringLayout springLayout;
	private final JLabel numCompteLabel = new JLabel();
	private final JLabel numCompteValueLabel = new JLabel();
	private final JLabel soldeLabel = new JLabel();
	private final JLabel sensLabel = new JLabel();
	private final JLabel soldeValueLabel = new JLabel();
	private final JLabel sensValueLabel = new JLabel();
	private final JButton quitterButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private ArrayList<Operation> listeOperations;
	private Compte compte;
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FReleveCompte frame = new FReleveCompte();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FReleveCompte() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "releveOperations";
		try {
			is = FAccueil.serveur.getInputStream();
			ois = new ObjectInputStream(is);
			String raction = (String)ois.readObject();
			if(action.equals(raction)){
				ois = new ObjectInputStream(is);
				listeOperations = (ArrayList<Operation>)ois.readObject();
				if(listeOperations.size() == 0){
					JOptionPane.showMessageDialog(null, "Aucune opération n'a été effectué sur ce compte", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				ois = new ObjectInputStream(is);
				compte = (Compte)ois.readObject();
				
				tableau.setModel(new ModeleStatiqueObjetOperation(listeOperations));
				tableau.getSelectionModel().setSelectionInterval(0,0);
				soldeValueLabel.setText(String.valueOf(compte.getSoldeCompte()));
				sensValueLabel.setText(compte.getSensCompte());
			}

			//	        System.out.println("Le nombre d'éléments renvoyés par le serveur est: "+listeAgences.size());
		}catch(IOException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
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
		setTitle("Releve Compte Bancaire");
		
		getContentPane().add(numCompteLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteLabel, 35, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numCompteLabel, 75, SpringLayout.WEST, getContentPane());
		numCompteLabel.setText("Num. compte");
		
		getContentPane().add(numCompteValueLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, numCompteValueLabel, 51, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, numCompteValueLabel, 290, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(soldeLabel);
		springLayout.putConstraint(SpringLayout.NORTH, soldeLabel, 245, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, soldeLabel, 0, SpringLayout.WEST, numCompteLabel);
		soldeLabel.setText("Solde");
		
		getContentPane().add(sensLabel);
		springLayout.putConstraint(SpringLayout.EAST, sensLabel, 66, SpringLayout.WEST, soldeLabel);
		springLayout.putConstraint(SpringLayout.WEST, sensLabel, 0, SpringLayout.WEST, soldeLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, sensLabel, 280, SpringLayout.NORTH, getContentPane());
		sensLabel.setText("Sens");
		
		getContentPane().add(soldeValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, soldeValueLabel, 0, SpringLayout.NORTH, soldeLabel);
		springLayout.putConstraint(SpringLayout.WEST, soldeValueLabel, 0, SpringLayout.EAST, sensLabel);
		
		getContentPane().add(sensValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, sensValueLabel, 0, SpringLayout.NORTH, sensLabel);
		springLayout.putConstraint(SpringLayout.WEST, sensValueLabel, 125, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 295, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 170, SpringLayout.WEST, getContentPane());
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
