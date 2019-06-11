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

import Modele.Agence;

@SuppressWarnings("serial")
public class FListeAgences extends JFrame {

	private SpringLayout springLayout;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JButton creerClientButton = new JButton();
	private final JButton quitterListButton = new JButton();
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private ArrayList<Agence> listeAgences;
	
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JTable tableau = new JTable();
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FListeAgences frame = new FListeAgences();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FListeAgences() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//String module = "agence";
		String action = "listeAgence";
		
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
		        tableau.setModel(new ModeleStatiqueObjetAgence(listeAgences));
				tableau.getSelectionModel().setSelectionInterval(0,0);
	        }
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
		setTitle("Liste Agences");
		
		getContentPane().add(scrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 75, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 45, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(creerClientButton);
		creerClientButton.addActionListener(new CreerClientButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, creerClientButton, 290, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, creerClientButton, 95, SpringLayout.WEST, getContentPane());
		creerClientButton.setText("Créer Client");
		
		getContentPane().add(quitterListButton);
		springLayout.putConstraint(SpringLayout.EAST, quitterListButton, 390, SpringLayout.WEST, getContentPane());
		quitterListButton.addActionListener(new QuitterListButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterListButton, 264, SpringLayout.NORTH, getContentPane());
		quitterListButton.setText("Quitter");
		
		getContentPane().add(scrollPane_1);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_1, 210, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane_1, 25, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane_1, 410, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane_1, 65, SpringLayout.WEST, getContentPane());
		
		scrollPane_1.setViewportView(tableau);
		
	}
	private class CreerClientButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			creerClientButton_actionPerformed(e);
		}
	}
	private class QuitterListButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterListButton_actionPerformed(e);
		}
	}
	protected void creerClientButton_actionPerformed(ActionEvent e) {
		
		int ligne = tableau.getSelectedRow();
		int colonnes = tableau.getColumnCount();		       
		
		Object[] tabAgence = new Object[ colonnes ];
		for ( int colonne = 0; colonne < colonnes-1; colonne++ ) {  
			tabAgence[ colonne ] = tableau.getValueAt( ligne, colonne );
		}
		String codeAgence = (String)tabAgence[0];
		String nomAgence = (String)tabAgence[1];
		FAjoutClient fenAjoutClient = new FAjoutClient(codeAgence, nomAgence, this);
		fenAjoutClient.setVisible(true);
		
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
