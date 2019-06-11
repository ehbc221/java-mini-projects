import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ChangerIdentifiant extends JFrame implements  ActionListener,ItemListener{

	/**
	 * @author BILY&Gaty Corporation
	 */
	private static final long serialVersionUID = 1L;
	JLabel info=new JLabel(),
	labelNpass=new JLabel("veuillez donner votre nouveau mot de Pass:"),
	labelApass=new JLabel("<html><body>veuillez saisir <br> votre ancien mot<br> de pass:</body></html>"),
	labelAlogin=new JLabel("<html><body>veuillez saisir<br> votre ancien <br>login:</body></html>"),
	labelNlogin=new JLabel("<html><body>veuillez donner <br>votre nouveau<br> login:</body></html>");
     JTextField ancienLogin=null,
     nouveauLogin=null;
     TextField  ancienPass=null,
     nouveauPass=null;
     Checkbox passChange=null,loginChange=null;
     JButton Valider=null;
     String login=null;
	ChangerIdentifiant(String login){
		super("BHZMessenger 1.0.0       changement d'identifiant");
		setIconImage((new ImageIcon("images.jpg")).getImage());
		ancienLogin=new JTextField(50);
	     nouveauLogin=new JTextField(50);
	    this.login=login;
	    
	     ancienPass=new TextField(50); 
	     nouveauPass=new TextField(50);
	      passChange=new Checkbox("changer  pass");
	     loginChange=new Checkbox("changer login"); 
	     Valider=new JButton ("Valider");
	     Valider.setEnabled(false);
	     loginChange                                            .setVisible(true);
	     passChange   .setVisible(true);
	     
	     nouveauLogin .setVisible(false);
	     ancienLogin.setVisible(false);
	     labelAlogin.setVisible(false);
		 	labelNlogin.setVisible(false);
		 	
	     nouveauPass.setVisible(false);
	     ancienPass .setVisible(false);
	     labelNpass.setVisible(false);
	 	labelApass.setVisible(false);
	 	this.ancienLogin.setText("");
	 	nouveauPass.setText("");
	 	nouveauLogin .setText("");
	 	 ancienPass.setText("");
	 	nouveauPass.setEchoChar('*');
	 	ancienPass.setEchoChar('*');
	    Valider. setToolTipText("Envoi des informations au serveur");
		Valider.setMnemonic('V');
		this.ancienLogin.setToolTipText("veuillez donner votre ancien login");
		this.nouveauLogin.setToolTipText("tapez le nouveau login");
		//this.ancienPass.setToolTipText("veuillez donner votre ancien pass");
		//this.nouveauPass.setToolTipText("tapez le nouveau mot de pass");
	JPanel panneau=new JPanel();
		
		
panneau.setBorder(
BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(
           BorderFactory.createTitledBorder("CHANGEMENT D'IDENTIFIANT"),
           BorderFactory.createEmptyBorder(5,5,5,5)),      
           panneau.getBorder()));

panneau.setLayout(new GridLayout(0,2));
add("Center",panneau);
JPanel pansouth=new JPanel();
add("South",pansouth);
pansouth.setLayout(new BorderLayout());
pansouth.add("East",Valider);
pansouth.add("Center",info);
panneau.setVisible(true);


JPanel panGauche=new JPanel(),panDroite=new JPanel();





panGauche.setBorder(
BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(
   BorderFactory.createTitledBorder("CHANGEMENT DE LOGIN"),
   BorderFactory.createEmptyBorder(5,5,5,5)),      
   panGauche.getBorder()));


panDroite.setBorder(
BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(
   BorderFactory.createTitledBorder("CHANGEMENT DE PASS"),
   BorderFactory.createEmptyBorder(5,5,5,5)),      
   panDroite.getBorder()));
panneau.add(panGauche);panneau.add( panDroite);
panGauche.setVisible(true);panDroite.setVisible(true);
panGauche.setLayout(new GridLayout(3,2));
panDroite.setLayout(new GridLayout(3,2));
panGauche.add(this.loginChange);
panGauche.add(new Canvas());
panGauche.add(labelAlogin);
panGauche.add(this.ancienLogin);
panGauche.add(labelNlogin);
panGauche.add(this.nouveauLogin);
panDroite.add(this.passChange);
panDroite.add(new Canvas());
panDroite.add(labelApass);
panDroite.add(this.ancienPass);
panDroite.add(labelNpass);
panDroite.add(this.nouveauPass);
this.passChange.addItemListener(this);
this.loginChange.addItemListener(this);
Valider.addActionListener(this);
this.setResizable(false);



setSize(500,290);
Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

this.setLocation(
        (dimensionecran.width-this.getWidth())/2,
        (dimensionecran.height-this.getHeight())/2
        );
setVisible(true);

	}
	
	
	
	
	@SuppressWarnings("unused")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(((this.loginChange.getState() && this.passChange.getState())&&(ancienLogin.getText().compareToIgnoreCase("")==0 ||nouveauLogin.getText().compareToIgnoreCase("")==0 ||ancienPass.getText().compareToIgnoreCase("")==0  ||nouveauPass.getText().compareToIgnoreCase("")==0 ))||( this.loginChange.getState()&& (ancienLogin.getText().compareToIgnoreCase("")==0||this.nouveauLogin.getText().compareToIgnoreCase("")==0) )||(this.passChange.getState() &&(ancienPass.getText().compareToIgnoreCase("")==0  ||nouveauPass.getText().compareToIgnoreCase("")==0 ||this.ancienLogin.getText().compareToIgnoreCase("")==0)  )   )
			info.setText("quelques champs sont vides!!");
		else{
			String message="";
		if(this.loginChange.getState() && this.passChange.getState())
			message=Protocole.ChangeId+":LP:"+this.ancienLogin.getText()+":"+this.nouveauLogin.getText()+":"+this.ancienPass.getText()+":"+this.nouveauPass.getText();
		else{
			if(this.loginChange.getState())
				message=Protocole.ChangeId+":L:"+this.ancienLogin.getText()+":"+this.nouveauLogin.getText();
			else if(this.passChange.getState()){
				
				
				message=Protocole.ChangeId+":P:"+this.ancienLogin.getText().trim()+":"+this.ancienPass.getText()+":"+this.nouveauPass.getText();
			}
		}
		info.setText("envoi en cours...");
		//envoi de message
		Socket messagerie;
		try {
			messagerie = new Socket(Config.IPserveur,Config.portServeur);
			BufferedReader messagerieLecture= new BufferedReader(
					   new InputStreamReader(messagerie .getInputStream()));
			//String mes=CriptageB.CripterChaine(message,0);
			String mes=message;
			 new PrintWriter(
					   new BufferedWriter(
					   new OutputStreamWriter(messagerie .getOutputStream())),
					   true).println(mes);
			 message=messagerieLecture.readLine();
			//message=DecriptageB.decripter(mes,0);
			messagerie.close();
			if(message.compareToIgnoreCase("SUCCESS_LP")==0){
				JOptionPane.showMessageDialog(null, "<html><body>votre login et mot de pass ont été bien modifiés:<br>votre nouveau Login:<h1>"+this.nouveauLogin.getText()+"</h1><br>nouveau pass:<h1>"+this.nouveauPass.getText()+"</h1><br>les notifications seront prises en comptes à la prochaine conexion<br><br>merci!</body></html>");
			this.login=this.nouveauLogin.getText().trim();
				setVisible(false);
			}
			else if(message.compareToIgnoreCase("SUCCESS_P")==0) {
				JOptionPane.showMessageDialog(null, "<html><body>votre mot de pas a été bien modifié<br>votre nouveau pass:<h1>"+this.nouveauPass.getText()+"</h1><br>les notifications seront prises en comptes à la prochaine conexion<br><br>merci!</body></html>");
				setVisible(false);
			}
			else if(message.compareToIgnoreCase("SUCCESS_L")==0){
				JOptionPane.showMessageDialog(null, "<html><body><h1>votre login a été bien modifié<br>votre nouveau login:<h1>"+this.nouveauLogin.getText()+"</h1><br>les notifications seront prises en comptes à la prochaine conexion<br>merci!</body></html>");
				this.login=this.nouveauLogin.getText().trim();
				setVisible(false);
			}
			else{
				info.setText("<html><body>erreur lors du traitement<br>veuillez bien <br>verifier vos informations <br></body> </html>");
				this.nouveauLogin.setText("");
				this.ancienLogin.setText("");
				this.nouveauPass.setText("");
				this.ancienPass.setText("");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			info.setText("erreur lors de l'envoi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			info.setText("erreur lors de l'envoi");
		}
		
		
		}
	}

	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this.loginChange ){
				if(e.getStateChange()==1){
					 nouveauLogin .setVisible(true);
				     ancienLogin.setVisible(true);
				     labelAlogin.setVisible(true);
					 	labelNlogin.setVisible(true);
					 	Valider.setEnabled(true);
				}else{
					 nouveauLogin .setVisible(false);
				     ancienLogin.setVisible(false);
				     labelAlogin.setVisible(false);
					 	labelNlogin.setVisible(false);
					 	Valider.setEnabled(false);
				}
			
		}else if(e.getSource()==this.passChange ){
			if(	e.getStateChange()==1){
				labelAlogin.setVisible(true);
				ancienLogin.setVisible(true);
			nouveauPass.setVisible(true);
		     ancienPass .setVisible(true);
		     labelNpass.setVisible(true);
		 	labelApass.setVisible(true);
		 	Valider.setEnabled(true);
			}else{
				labelAlogin.setVisible(true);
				ancienLogin.setVisible(false);
				nouveauPass.setVisible(false);
			     ancienPass .setVisible(false);
			     labelNpass.setVisible(false);
			     Valider.setEnabled(false);
			}
		}
	}

	

}
