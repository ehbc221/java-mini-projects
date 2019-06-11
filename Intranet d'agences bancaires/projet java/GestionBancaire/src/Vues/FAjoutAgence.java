package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import Modele.Agence;


public class FAjoutAgence extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4524811266901595822L;
	private SpringLayout springLayout;
	private final JLabel nomAgenceLabel = new JLabel();
	private final JTextField nomAgenceValueText = new JTextField();
	private final JLabel adresseAgenceLabel = new JLabel();
	private final JTextArea adresseTextArea = new JTextArea();
	private final JButton enregistrerButton = new JButton();
	private final JButton quitterButton = new JButton();
	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FAjoutAgence frame = new FAjoutAgence();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FAjoutAgence() {
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
	private void jbInit() throws Exception {
		springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setTitle("Ajout Agence");
		
		getContentPane().add(nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceLabel, 35, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceLabel, 35, SpringLayout.WEST, getContentPane());
		nomAgenceLabel.setText("Nom Agence");
		
		getContentPane().add(nomAgenceValueText);
		springLayout.putConstraint(SpringLayout.EAST, nomAgenceValueText, 345, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceValueText, 205, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceValueText, 33, SpringLayout.NORTH, getContentPane());
		
		getContentPane().add(adresseAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, adresseAgenceLabel, 145, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, adresseAgenceLabel, 0, SpringLayout.WEST, nomAgenceLabel);
		adresseAgenceLabel.setText("Adresse Agence");
		
		getContentPane().add(adresseTextArea);
		springLayout.putConstraint(SpringLayout.EAST, adresseTextArea, 0, SpringLayout.EAST, nomAgenceValueText);
		springLayout.putConstraint(SpringLayout.SOUTH, adresseTextArea, 195, SpringLayout.NORTH, getContentPane());
		adresseTextArea.setRows(5);
		adresseTextArea.setColumns(12);
		
		getContentPane().add(enregistrerButton);
		enregistrerButton.addActionListener(new EnregistrerButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, enregistrerButton, 280, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, enregistrerButton, 80, SpringLayout.WEST, getContentPane());
		enregistrerButton.setText("Enregistrer");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 370, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 265, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 254, SpringLayout.NORTH, getContentPane());
		quitterButton.setText("Quitter");
	}
	private class EnregistrerButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			enregistrerButton_actionPerformed(e);
		}
	}
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	protected void enregistrerButton_actionPerformed(ActionEvent e) {
		
		String action = "nouvelleAgence";
		boolean res;
		try {
			Agence agence = new Agence(nomAgenceValueText.getText(), adresseTextArea.getText());

			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(agence);
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
	        	nomAgenceValueText.setText("");
	        	adresseTextArea.setText("");
	        }
		}catch(Exception ex){
			//ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	protected void quitterButton_actionPerformed(ActionEvent e) {
		try{
			if(oos != null)
				oos.flush();
			dispose();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
