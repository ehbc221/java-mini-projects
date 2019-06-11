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

import Modele.Client;
import Modele.Compte;

public class FListeCompteClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JLabel numClientLabel = new JLabel();
	private final JButton quitterButton = new JButton();
	private final JLabel numClientValueLabel = new JLabel();
	private InputStream is;
	private ObjectInputStream ois;
	private ArrayList<Compte> listeComptesClient;
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FListeCompteClient frame = new FListeCompteClient();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FListeCompteClient() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String action = "listerComptesClient";
		try {
			is = FAccueil.serveur.getInputStream();
			ois = new ObjectInputStream(is);
			String raction = (String)ois.readObject();
			if(action.equals(raction)){
				ois = new ObjectInputStream(is);
				listeComptesClient = (ArrayList<Compte>)ois.readObject();
				if(listeComptesClient.size() == 0){
					JOptionPane.showMessageDialog(null, "Ce client ne possède pas encore de compte", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println(listeComptesClient.size()+" constructeur");
				tableau.setModel(new ModeleStatiqueObjetComptesClient(listeComptesClient));
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
	
	public FListeCompteClient(String pNumClient) {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		numClientValueLabel.setText(pNumClient);
		String action = "listerComptesClient";
		try {
			is = FAccueil.serveur.getInputStream();
			ois = new ObjectInputStream(is);
			String raction = (String)ois.readObject();
			if(action.equals(raction)){
				ois = new ObjectInputStream(is);
				listeComptesClient = (ArrayList<Compte>)ois.readObject();
				System.out.println(listeComptesClient.size()+" constructeur");
				tableau.setModel(new ModeleStatiqueObjetComptesClient(listeComptesClient));
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
		setTitle("Liste Comptes Client");
		
		getContentPane().add(numClientLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, numClientLabel, 40, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, numClientLabel, 140, SpringLayout.WEST, getContentPane());
		numClientLabel.setText("Num. Client");
		
//		getContentPane().add(quitterButton);
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 260, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 285, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 165, SpringLayout.WEST, getContentPane());
		quitterButton.setText("Quitter");
		
		getContentPane().add(numClientValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numClientValueLabel, 0, SpringLayout.NORTH, numClientLabel);
		springLayout.putConstraint(SpringLayout.WEST, numClientValueLabel, 195, SpringLayout.WEST, getContentPane());
		
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
			FRechercheClient fenRechCli = new FRechercheClient();
			fenRechCli.setVisible(true);
			dispose(); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
