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


public class FRechercheClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;
	private final JLabel numClientLabel = new JLabel();
	private final JTextField numClientValueText = new JTextField();
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
			FRechercheClient frame = new FRechercheClient();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FRechercheClient() {
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
		setTitle("Recherche Client");
		
		getContentPane().add(numClientLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numClientLabel, 65, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numClientLabel, 60, SpringLayout.WEST, getContentPane());
		numClientLabel.setText("Num. Client");
		
		getContentPane().add(numClientValueText);
		springLayout.putConstraint(SpringLayout.EAST, numClientValueText, 360, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numClientValueText, 190, SpringLayout.WEST, getContentPane());
		numClientValueText.setColumns(7);
		springLayout.putConstraint(SpringLayout.NORTH, numClientValueText, 0, SpringLayout.NORTH, numClientLabel);
		
		getContentPane().add(rechercherButton);
		rechercherButton.addActionListener(new RechercherButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, rechercherButton, 200, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, rechercherButton, 280, SpringLayout.NORTH, getContentPane());
		rechercherButton.setText("Rechercher");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.SOUTH, quitterButton, 306, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 111, SpringLayout.WEST, numClientValueText);
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 5, SpringLayout.WEST, numClientValueText);
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
		
		String action = "rechercherClient";
		int numClient = 0;
		try{
			numClient = Integer.parseInt(numClientValueText.getText());
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
			oos.writeObject(numClient);
			oos.flush();
			FListeCompteClient fenListeCptCli = new FListeCompteClient(numClientValueText.getText());
			fenListeCptCli.setVisible(true);
			
			numClientValueText.setText("");
			quitterButton.doClick();
		}catch(Exception ex){
//			ex.printStackTrace();
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
