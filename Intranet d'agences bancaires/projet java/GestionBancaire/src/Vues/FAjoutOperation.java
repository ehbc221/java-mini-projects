package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import Modele.Operation;

public class FAjoutOperation extends JFrame {

	private SpringLayout springLayout;
	private final JLabel numCompteLabel = new JLabel();
	private final JLabel numCompteValueLabel = new JLabel();
	private final JLabel libeleOperationLabel = new JLabel();
	private final JLabel sensLabel = new JLabel();
	private final JLabel montantOperationLabel = new JLabel();
	private final JTextField libeleOperationValueText = new JTextField();
	private final JComboBox<String> comboBox = new JComboBox<String>();
	private final JTextField montantOperationValueText = new JTextField();
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
			FAjoutOperation frame = new FAjoutOperation();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FAjoutOperation() {
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
	
	public FAjoutOperation(int pNumCompte){
		
		super();
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		numCompteValueLabel.setText(String.valueOf(pNumCompte));
		comboBox.addItem("CR");
		comboBox.addItem("DB");
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
		setTitle("Ajout Operation");
		
		getContentPane().add(numCompteLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteLabel, 30, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numCompteLabel, 60, SpringLayout.WEST, getContentPane());
		numCompteLabel.setText("Num. compte");
		
		getContentPane().add(numCompteValueLabel);
		springLayout.putConstraint(SpringLayout.NORTH, numCompteValueLabel, 30, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, numCompteValueLabel, 215, SpringLayout.WEST, getContentPane());
//		numCompteValueLabel.setText();
		
		getContentPane().add(libeleOperationLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, libeleOperationLabel, 95, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, libeleOperationLabel, 66, SpringLayout.WEST, numCompteLabel);
		springLayout.putConstraint(SpringLayout.WEST, libeleOperationLabel, 0, SpringLayout.WEST, numCompteLabel);
		libeleOperationLabel.setText("Libele operation");
		
		getContentPane().add(sensLabel);
		springLayout.putConstraint(SpringLayout.NORTH, sensLabel, 125, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, sensLabel, 66, SpringLayout.WEST, libeleOperationLabel);
		springLayout.putConstraint(SpringLayout.WEST, sensLabel, 0, SpringLayout.WEST, libeleOperationLabel);
		sensLabel.setText("Sens");
		
		getContentPane().add(montantOperationLabel);
		springLayout.putConstraint(SpringLayout.EAST, montantOperationLabel, 0, SpringLayout.EAST, numCompteLabel);
		springLayout.putConstraint(SpringLayout.WEST, montantOperationLabel, 0, SpringLayout.WEST, sensLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, montantOperationLabel, 185, SpringLayout.NORTH, getContentPane());
		montantOperationLabel.setText("Montant operation");
		
		getContentPane().add(libeleOperationValueText);
		libeleOperationValueText.setColumns(7);
		springLayout.putConstraint(SpringLayout.NORTH, libeleOperationValueText, 0, SpringLayout.NORTH, libeleOperationLabel);
		springLayout.putConstraint(SpringLayout.WEST, libeleOperationValueText, 0, SpringLayout.WEST, numCompteValueLabel);
		
		getContentPane().add(comboBox);
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 0, SpringLayout.NORTH, sensLabel);
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 0, SpringLayout.WEST, libeleOperationValueText);
		
		getContentPane().add(montantOperationValueText);
		montantOperationValueText.setColumns(7);
		springLayout.putConstraint(SpringLayout.NORTH, montantOperationValueText, 0, SpringLayout.NORTH, montantOperationLabel);
		springLayout.putConstraint(SpringLayout.WEST, montantOperationValueText, 0, SpringLayout.WEST, comboBox);
		
		getContentPane().add(enregistrerButton);
		enregistrerButton.addActionListener(new EnregistrerButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, enregistrerButton, 245, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, enregistrerButton, 95, SpringLayout.WEST, getContentPane());
		enregistrerButton.setText("Enregistrer");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.EAST, quitterButton, 50, SpringLayout.EAST, comboBox);
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 245, SpringLayout.NORTH, getContentPane());
		quitterButton.setText("Quitter");
	}
	
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	private class EnregistrerButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			enregistrerButton_actionPerformed(e);
		}
	}
	
	protected void quitterButton_actionPerformed(ActionEvent e) {
		try{
			FListeComptes fenListeCpt = new FListeComptes();
			fenListeCpt.setVisible(true);
			oos.flush();
			dispose();

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	protected void enregistrerButton_actionPerformed(ActionEvent e) {
		
		String action = "ajoutOperation";
		boolean res;
		try {
			Operation operation = new Operation();
			operation.setNumCompteOp(numCompteValueLabel.getText());
			operation.setSensOp(comboBox.getSelectedItem().toString());
			operation.setMontantOp(Integer.parseInt(montantOperationValueText.getText()));
			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(operation);
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
	        	montantOperationValueText.setText("");
	        	quitterButton.doClick();
	        }
		}catch(Exception ex){
		ex.printStackTrace();
//		JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}

}
