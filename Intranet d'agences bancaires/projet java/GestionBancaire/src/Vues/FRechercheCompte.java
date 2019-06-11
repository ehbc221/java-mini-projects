package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


public class FRechercheCompte extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JLabel numCompteLabel = new JLabel();
	private final JTextField numCompteValueText = new JTextField();
	private final JButton rechercherButton = new JButton();
	private final JButton quitterButton = new JButton();
	private OutputStream os;
	private ObjectOutputStream oos;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FRechercheCompte frame = new FRechercheCompte();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FRechercheCompte() {
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
		setTitle("Recherche Compte");
		
		getContentPane().add(numCompteLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteLabel, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numCompteLabel, 75, SpringLayout.WEST, getContentPane());
		numCompteLabel.setText("Num. compte");
		
		getContentPane().add(numCompteValueText);
		numCompteValueText.setColumns(7);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteValueText, 0, SpringLayout.NORTH, numCompteLabel);
		springLayout.putConstraint(SpringLayout.WEST, numCompteValueText, 240, SpringLayout.WEST, getContentPane());
		
		getContentPane().add(rechercherButton);
		rechercherButton.addActionListener(new RechercherButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, rechercherButton, 150, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, rechercherButton, 0, SpringLayout.WEST, numCompteLabel);
		rechercherButton.setText("Rechercher");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 0, SpringLayout.NORTH, rechercherButton);
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 5, SpringLayout.WEST, numCompteValueText);
		quitterButton.setText("Quitter");
	}
	
	private class RechercherButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			rechercherButton_actionPerformed(e);
		}
	}
	
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	
	protected void rechercherButton_actionPerformed(ActionEvent e) {
		
		String action = "rechercherCompte";
		String numCompte = numCompteValueText.getText();
		int verifNumCompte = 0;
		try{
			verifNumCompte = Integer.parseInt(numCompteValueText.getText());
		}catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(null, "Vous devez saisir un nombre entier \n Retournez en arrière et recommencez", "Erreur", JOptionPane.ERROR_MESSAGE);	
		}
		try {
//			System.out.println(numClient+" l_l");
			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(numCompteValueText.getText());
			oos.flush();
			FReleveCompte fenReleve = new FReleveCompte();
			fenReleve.setVisible(true);
			numCompteValueText.setText("");
			quitterButton.doClick();
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
