package Vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class FAccueil extends JFrame {

	private SpringLayout springLayout;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem itemCreerAgence = new JMenuItem();
	private final JMenuItem itemListerClients = new JMenuItem();
	private final JMenuItem itemListerAgence = new JMenuItem();
	private final JMenu agenceMenu = new JMenu();
	private final JMenu clientMenu = new JMenu();
	private final JMenuItem itemListerClientAgence = new JMenuItem();
	private final JMenu compteMenu = new JMenu();
	private final JMenuItem itemListerCompteClient = new JMenuItem();
	private final JMenuItem itemListerCompte = new JMenuItem();
	private final JMenuItem itemReleveCompte = new JMenuItem();
	private final JButton quitterButton = new JButton();
	public static Socket serveur;
	public static String ip;
	public static int port;
	private OutputStream os;
	private ObjectOutputStream oos;
	/**
	 * Launch the application'"dx"  
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FAccueil frame = new FAccueil();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public FAccueil() {
		super();
		setBounds(100, 100, 585, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			String SaisieIp = JOptionPane.showInputDialog("Veuillez saisir l'adresse ip du serveur:");
			ip = SaisieIp;
			port = 2000;
			serveur = new Socket(ip, port);
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//
	}
	private void jbInit() throws Exception {
		springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setTitle("Menu Principal");
		
		setJMenuBar(menuBar);
		
		menuBar.add(agenceMenu);
		agenceMenu.setText("Agence");
		
		agenceMenu.add(itemCreerAgence);
		itemCreerAgence.addActionListener(new ItemCreerAgenceActionListener());
		itemCreerAgence.setText("Créer Agence");
		
		agenceMenu.add(itemListerAgence);
		itemListerAgence.addActionListener(new ItemListerAgenceActionListener());
		itemListerAgence.setText("Lister Agences");
		
		menuBar.add(clientMenu);
		clientMenu.setText("Client");
		
		clientMenu.add(itemListerClients);
		itemListerClients.addActionListener(new ItemListerClientsActionListener());
		itemListerClients.setText("Lister Clients");
		
		clientMenu.add(itemListerClientAgence);
		itemListerClientAgence.addActionListener(new ItemListerClientAgenceActionListener());
		itemListerClientAgence.setText("Lister Clients par Agence");
		
		menuBar.add(compteMenu);
		compteMenu.setText("Compte");
		
		compteMenu.add(itemListerCompteClient);
		itemListerCompteClient.addActionListener(new ItemListerCompteClientActionListener());
		itemListerCompteClient.setText("Liste des Comptes Client");
		
		compteMenu.add(itemListerCompte);
		itemListerCompte.addActionListener(new ItemListerCompteActionListener());
		itemListerCompte.setText("Liste des Comptes");
		
		compteMenu.add(itemReleveCompte);
		itemReleveCompte.addActionListener(new ItemReleveCompteActionListener());
		itemReleveCompte.setText("Relevé Compte Bancaire");
		
		getContentPane().add(quitterButton);
		quitterButton.addActionListener(new QuitterButtonActionListener());
		springLayout.putConstraint(SpringLayout.NORTH, quitterButton, 270, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, quitterButton, 240, SpringLayout.WEST, getContentPane());
		quitterButton.setText("Quitter");
	}
	private class ItemCreerAgenceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemCreerAgence_actionPerformed(e);
		}
	}
	private class ItemListerAgenceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemListerAgence_actionPerformed(e);
		}
	}
	private class ItemListerClientsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemListerClients_actionPerformed(e);
		}
	}
	private class ItemListerClientAgenceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemListerClientAgence_actionPerformed(e);
		}
	}
	private class ItemListerCompteClientActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemListerCompteClient_actionPerformed(e);
		}
	}
	private class ItemListerCompteActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemListerCompte_actionPerformed(e);
		}
	}
	private class ItemReleveCompteActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			itemReleveCompte_actionPerformed(e);
		}
	}
	private class QuitterButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quitterButton_actionPerformed(e);
		}
	}
	protected void itemCreerAgence_actionPerformed(ActionEvent e) {
		
		FAjoutAgence fenAjoutAgence = new FAjoutAgence();
		fenAjoutAgence.setVisible(true);
	}
	protected void itemListerAgence_actionPerformed(ActionEvent e) {
		
		FListeAgences fenListAgences = new FListeAgences();
		fenListAgences.setVisible(true);
	}
	protected void itemListerClients_actionPerformed(ActionEvent e) {
		
		FListeClients fenListClients = new FListeClients(); 
		fenListClients.setVisible(true);
	}
	protected void itemListerClientAgence_actionPerformed(ActionEvent e) {
		
		FSelectionAgence fenSelectionAgence = new FSelectionAgence();
		fenSelectionAgence.setVisible(true);
	}
	protected void itemListerCompteClient_actionPerformed(ActionEvent e) {
		
		FRechercheClient fenRechCli = new FRechercheClient();
		fenRechCli.setVisible(true);
	}
	protected void itemListerCompte_actionPerformed(ActionEvent e) {
		
		FListeComptes fenListCpt = new FListeComptes();
		fenListCpt.setVisible(true);
	}
	protected void itemReleveCompte_actionPerformed(ActionEvent e) {
		
		FRechercheCompte fenRechCpt = new FRechercheCompte();
		fenRechCpt.setVisible(true);
	}
	protected void quitterButton_actionPerformed(ActionEvent e) {
		
		try {
			String action="quitter";
			os = FAccueil.serveur.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(action);
			oos.flush();
			dispose();
			System.exit(0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
