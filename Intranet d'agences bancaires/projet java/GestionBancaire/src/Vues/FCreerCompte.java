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

import Modele.Compte;

public class FCreerCompte extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JLabel numClientLabel = new JLabel();
	private final JLabel numClientValueLabel = new JLabel();
	private final JLabel nomAgenceLabel = new JLabel();
	private final JLabel nomAgenceValueLabel = new JLabel();
	private final JLabel numCompteLabel = new JLabel();
	private final JTextField numCompteValueText = new JTextField();
	private final JLabel sensLabel = new JLabel();
	private final JLabel sensValueLabel = new JLabel();
	private final JLabel soldeLabel = new JLabel();
	private final JTextField soldeValueText = new JTextField();
	private FListeClients fenListeClient;
	private final JButton ajouterButton = new JButton();
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
			FCreerCompte frame = new FCreerCompte();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FCreerCompte() {
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
	
	public FCreerCompte(int pNumClient, String pNomAgence, FListeClients fenListCli) {
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenListeClient = fenListCli;
		numClientValueLabel.setText(String.valueOf(pNumClient));
		nomAgenceValueLabel.setText(pNomAgence);
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
		setTitle("Création de compte");
		
		getContentPane().add(numClientLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numClientLabel, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numClientLabel, 25, SpringLayout.WEST, getContentPane());
		numClientLabel.setText("Num. Client");
		
		getContentPane().add(numClientValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numClientValueLabel, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numClientValueLabel, 190, SpringLayout.WEST, getContentPane());
//		numClientValueLabel.setText("");
		
		getContentPane().add(nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceLabel, 95, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceLabel, 0, SpringLayout.WEST, numClientLabel);
		nomAgenceLabel.setText("Nom Agence");
		
		getContentPane().add(nomAgenceValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, nomAgenceValueLabel, 0, SpringLayout.NORTH, nomAgenceLabel);
		springLayout.putConstraint(SpringLayout.WEST, nomAgenceValueLabel, 0, SpringLayout.WEST, numClientValueLabel);
//		nomAgenceValueLabel.setText();
		
		getContentPane().add(numCompteLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteLabel, 150, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numCompteLabel, 0, SpringLayout.WEST, nomAgenceLabel);
		numCompteLabel.setText("Num Compte");
		
		getContentPane().add(numCompteValueText);
		springLayout.putConstraint(SpringLayout.SOUTH, numCompteValueText, 170, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, numCompteValueText, 64, SpringLayout.WEST, nomAgenceValueLabel);
		springLayout.putConstraint(SpringLayout.WEST, numCompteValueText, 5, SpringLayout.WEST, nomAgenceValueLabel);
		numCompteValueText.setColumns(5);
		
		getContentPane().add(sensLabel);
		springLayout.putConstraint(SpringLayout.NORTH, sensLabel, 200, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, sensLabel, 0, SpringLayout.WEST, numCompteLabel);
		sensLabel.setText("Sens");
		
		getContentPane().add(sensValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, sensValueLabel, 0, SpringLayout.NORTH, sensLabel);
		springLayout.putConstraint(SpringLayout.WEST, sensValueLabel, 0, SpringLayout.WEST, nomAgenceValueLabel);
		sensValueLabel.setText("CR");
		
		getContentPane().add(soldeLabel);
		springLayout.putConstraint(SpringLayout.NORTH, soldeLabel, 250, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, soldeLabel, 0, SpringLayout.WEST, sensLabel);
		soldeLabel.setText("Solde");
		
		getContentPane().add(soldeValueText);
		springLayout.putConstraint(SpringLayout.NORTH, soldeValueText, 248, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, soldeValueText, 64, SpringLayout.WEST, sensValueLabel);
		springLayout.putConstraint(SpringLayout.WEST, soldeValueText, 5, SpringLayout.WEST, sensValueLabel);
		soldeValueText.setColumns(5);
		
		getContentPane().add(ajouterButton);
		ajouterButton.addActionListener(new AjouterButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, ajouterButton, 200, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ajouterButton, 0, SpringLayout.EAST, numCompteLabel);
		springLayout.putConstraint(SpringLayout.NORTH, ajouterButton, 280, SpringLayout.NORTH, getContentPane());
		ajouterButton.setText("Ajouter");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, quitterButton, 306, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 111, SpringLayout.WEST, soldeValueText);
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 5, SpringLayout.WEST, soldeValueText);
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
			fenListeClient.setVisible(true);
			this.setVisible(false);
			oos.flush();
			dispose();

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	protected void ajouterButton_actionPerformed(ActionEvent e) {
		
		boolean res;
		String action = "nouveauCompte";
		try {
			Compte compte = new Compte();
			compte.setNumClient(Integer.parseInt(numClientValueLabel.getText()));
			compte.setNumComte(numCompteValueText.getText());
			compte.setSensCompte(sensValueLabel.getText());
			compte.setSoldeCompte(Integer.parseInt(soldeValueText.getText()));
			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(compte);
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
	        	numCompteValueText.setText("");
	        	soldeValueText.setText("");
	        }
		}catch(Exception ex){
			//		ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

}
