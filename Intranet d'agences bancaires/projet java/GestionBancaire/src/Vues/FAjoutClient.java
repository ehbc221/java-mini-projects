package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import Modele.Client;

public class FAjoutClient extends JFrame {

	private SpringLayout springLayout;
	private final JLabel codeAgenceLabel = new JLabel();
	private final JLabel nomAgenceLabel = new JLabel();
	private final JLabel nomClientLabel = new JLabel();
	private final JLabel prenomClientLabel = new JLabel();
	private final JLabel adresseClientLabel = new JLabel();
	private final JLabel codeAgenceVlaueLabel = new JLabel();
	private final JLabel nomAgenceValueLabel = new JLabel();
	private final JTextField nomClientTextValue = new JTextField();
	private final JTextField prenomClientTextValue = new JTextField();
	private final JTextField adresseClientTextValue = new JTextField();
	private final JButton ajouterButton = new JButton();
	private final JButton quitterButton = new JButton();
	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	private FListeAgences fenListeAgence;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FAjoutClient frame = new FAjoutClient();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FAjoutClient() {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//
	}
	
	public FAjoutClient(String codeAgence, String nomAgence, FListeAgences fenListAg) {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenListeAgence = fenListAg;
		codeAgenceVlaueLabel.setText(codeAgence);
		nomAgenceValueLabel.setText(nomAgence);
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
		setTitle("Ajout Client");
		
		getContentPane().add(codeAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, codeAgenceLabel, 40, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, codeAgenceLabel, 25, SpringLayout.WEST, getContentPane());
		codeAgenceLabel.setText("Code Agence");
		
		getContentPane().add(nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceLabel, 75, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceLabel, 0, SpringLayout.WEST, codeAgenceLabel);
		nomAgenceLabel.setText("Nom Agence");
		
		getContentPane().add(nomClientLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomClientLabel, 115, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomClientLabel, 0, SpringLayout.WEST, nomAgenceLabel);
		nomClientLabel.setText("Nom Client");
		
		getContentPane().add(prenomClientLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, prenomClientLabel, 165, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, prenomClientLabel, 66, SpringLayout.WEST, nomClientLabel);
		springLayout.putConstraint(SpringLayout.WEST, prenomClientLabel, 0, SpringLayout.WEST, nomClientLabel);
		prenomClientLabel.setText("Prenom Client");
		
		getContentPane().add(adresseClientLabel);
		springLayout.putConstraint(SpringLayout.NORTH, adresseClientLabel, 190, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, adresseClientLabel, 0, SpringLayout.WEST, prenomClientLabel);
		adresseClientLabel.setText("Adresse Client");
		
		getContentPane().add(codeAgenceVlaueLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, codeAgenceVlaueLabel, 56, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, codeAgenceVlaueLabel, 230, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(nomAgenceValueLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, nomAgenceValueLabel, 91, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, nomAgenceValueLabel, 71, SpringLayout.WEST, codeAgenceVlaueLabel);
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceValueLabel, 5, SpringLayout.WEST, codeAgenceVlaueLabel);
		
		getContentPane().add(nomClientTextValue);
		springLayout.putConstraint(SpringLayout.SOUTH, nomClientTextValue, 133, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, nomClientTextValue, 113, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, nomClientTextValue, 71, SpringLayout.WEST, codeAgenceVlaueLabel);
		springLayout.putConstraint(SpringLayout.WEST, nomClientTextValue, 0, SpringLayout.WEST, codeAgenceVlaueLabel);
		nomClientTextValue.setColumns(5);
		
		getContentPane().add(prenomClientTextValue);
		springLayout.putConstraint(SpringLayout.SOUTH, prenomClientTextValue, 167, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, prenomClientTextValue, 71, SpringLayout.WEST, nomClientTextValue);
		springLayout.putConstraint(SpringLayout.WEST, prenomClientTextValue, 0, SpringLayout.WEST, nomClientTextValue);
		prenomClientTextValue.setColumns(5);
		
		getContentPane().add(adresseClientTextValue);
		springLayout.putConstraint(SpringLayout.SOUTH, adresseClientTextValue, 208, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, adresseClientTextValue, 70, SpringLayout.WEST, prenomClientTextValue);
		springLayout.putConstraint(SpringLayout.WEST, adresseClientTextValue, 0, SpringLayout.WEST, prenomClientTextValue);
		adresseClientTextValue.setColumns(5);
		
		getContentPane().add(ajouterButton);
		ajouterButton.addActionListener(new AjouterButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, ajouterButton, 200, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ajouterButton, 0, SpringLayout.EAST, prenomClientLabel);
		springLayout.putConstraint(SpringLayout.NORTH, ajouterButton, 280, SpringLayout.NORTH, getContentPane());
		ajouterButton.setText("Ajouter");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, quitterButton, 306, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 111, SpringLayout.WEST, adresseClientTextValue);
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 5, SpringLayout.WEST, adresseClientTextValue);
		quitterButton.setText("Quitter");
	}
	
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	
	private class AjouterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ajouterButton_actionPerformed(e);
		}
	}
	protected void quitterButton_actionPerformed(ActionEvent e) {
		try{
//			fenListeAgence.setVisible(true);
			 oos.flush();
			 dispose();
			 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	protected void ajouterButton_actionPerformed(ActionEvent e) {
		
		String action = "nouveauClient";
		boolean res;
		try {
			Client client = new Client();
			client.setNom(nomClientTextValue.getText());
			client.setPrenoms(prenomClientTextValue.getText());
			client.setAdresseCli(adresseClientTextValue.getText());
			client.setNomAgence(nomAgenceValueLabel.getText());
			client.setCodeAgence(Integer.parseInt(codeAgenceVlaueLabel.getText()));
			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(client);
			oos.flush();
			
			is = FAccueil.serveur.getInputStream();
	        ois = new ObjectInputStream(is);
	        String raction = (String)ois.readObject();
	        if(action.equals(raction)){
	        	ois = new ObjectInputStream(is);
	        	res = (Boolean)ois.readObject();
	        	if(res == true){
	        		JOptionPane.showMessageDialog(null, "Sauvegarde réussie!", "Information", JOptionPane.INFORMATION_MESSAGE);
	        	}
	        	else{
	        		JOptionPane.showMessageDialog(null, "Erreur de sauvegarde", "Erreur", JOptionPane.ERROR_MESSAGE);
	        	}
	        	nomClientTextValue.setText("");
	        	prenomClientTextValue.setText("");
	        	adresseClientTextValue.setText("");
	        }
		}catch(Exception ex){
//			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
}
