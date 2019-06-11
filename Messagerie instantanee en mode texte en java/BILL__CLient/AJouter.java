import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.*;

public class AJouter  extends JFrame implements ActionListener,KeyListener{
	
	
	/**
	 * @author  Bile Bernard
	 */
	
 JTextField loginContact=new JTextField(50),
 nomcontact=new JTextField(50),
 prenomContact=new JTextField(50);
 JButton valider=new JButton("valider");
 JLabel info=new JLabel();
 String expediteur=null;
 AJouter(String expediteur){
	 super("BZHMessenger 1.0.0  ajout d'un contact");
	 this.expediteur=expediteur;
	 //============================================================
	 JPanel pan=new JPanel();
	 pan.setBorder(
	         BorderFactory.createCompoundBorder(
	              BorderFactory.createCompoundBorder(
	                            BorderFactory.createTitledBorder("AJOUT D'UN CONTACT "),
	                            BorderFactory.createEmptyBorder(5,5,5,5)),       //pour éloigné la bordure
	         pan.getBorder()));
	 pan.setLayout(new GridLayout(0,2,10,10));
	 pan. add(new JLabel("   login du contact:"));pan.add(loginContact);loginContact.setToolTipText("le login de v otre contact");
	 pan. add(new JLabel("    nom du contact:")); pan.add(nomcontact);nomcontact.setToolTipText("le nom de votre contact ,facultatif");
	 pan. add(new JLabel("    prenom du contact:"));pan.add( prenomContact);prenomContact.setToolTipText("prenom de votre contact ,facultatif");
	 pan.add(info); pan.add(valider);
	 add(pan);
	 valider.setToolTipText("envoyer les donner au serveur");
	 valider.setMnemonic('E');
	 //====================================================
	 valider.addActionListener(this);
	 prenomContact.addKeyListener(this);
	 setIconImage((new ImageIcon("images.gif")).getImage());
	 setSize(300,300);
	 Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(
		        ((dimensionecran.width-this.getWidth())/2)-100,
		        (dimensionecran.height-this.getHeight())/2
		        );
	 setBackground(new Color(167,20,171));
		setVisible(true);
		this.setResizable(false);
 }
	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource()==valider){
			
			
			     if(loginContact.getText().compareToIgnoreCase("")==0)
			    	 info.setText("Login Contact incorrect!!!");
			    else if(loginContact.getText().trim().compareToIgnoreCase(this.expediteur)==0)	  JOptionPane.showMessageDialog(null, "<html><h1><blink>vous pouvez pas vous ajouter vous mème!!</blink></h1><html>");
			     else{
			    	 if(this.nomcontact.getText().compareToIgnoreCase("")==0) this.nomcontact.setText("X");
			    	 if(this.prenomContact.getText().compareToIgnoreCase("")==0) this.prenomContact.setText("X");
			info.setText("traitement en cours...");
			
				String text="";
				Socket envoi=null;
				try {
					envoi = new Socket(Config.IPserveur,Config.portServeur);
				} catch (UnknownHostException e3) {
					// TODO Auto-generated catch block
					info.setText("erreur de connexion!verifier que vous bien connecté(e) au reseau!");
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					info.setText("erreur de connexion!verifier que vous bien connecté(e) au reseau!");
				}
				   

				   BufferedReader lecture=null;
				try {
					lecture = new BufferedReader(
					   new InputStreamReader(envoi .getInputStream())
					   );
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					info.setText("erreur de connexion!verifier que vous bien connecté(e) au reseau!");
				}

				   PrintWriter ecriture=null;
				try {
					ecriture = new PrintWriter(
					   new BufferedWriter(
					   new OutputStreamWriter(envoi .getOutputStream())),
					   true);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					info.setText("erreur de connexion!verifier que vous bien connecté(e) au reseau!");//code 3 pour l'ajout d'un contact
				}
				String mes=Protocole.Ajout+":"+expediteur+":"+loginContact.getText()+":"+nomcontact.getText()+":"+prenomContact.getText();
				 //mes=CriptageB.CripterChaine(Protocole.Ajout+":"+expediteur+":"+loginContact.getText()+":"+nomcontact.getText()+":"+prenomContact.getText(),0);
				ecriture.println(mes);//, InetAddress.getByName("BILL-GATY"), 5776);
		try {
			text=lecture.readLine();
				//text=DecriptageB.decripter(text,0);//reponse du serveur 
				envoi.close();
				lecture.close();
				ecriture.close();
		} catch (IOException e1) {
				// TODO Auto-generated catch block
			info.setText("erreur de donnée");
		}
				//0:cet login n'existe pas//vot
				if(text.charAt(0)!='0') {JOptionPane.showMessageDialog(null, text);
				setVisible(false);
				}
				else info.setText(text);
			}
		}
			
		}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==10) valider.doClick();
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		
	
	

}
