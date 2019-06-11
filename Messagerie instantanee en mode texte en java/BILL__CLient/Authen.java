import java.awt.Canvas;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.net.*;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.*;
@SuppressWarnings("serial")
public class Authen extends JFrame  implements ActionListener,KeyListener,MouseListener,WindowListener{
JTextField login=null;
TextField password=null;
JLabel info=null;
//static Authen principal=null;
JButton valider=null;
JLabel nouveauCompte=null;
JMenuItem ajouter=new JMenuItem("configurateur du serveur");
@SuppressWarnings({ "deprecation" })
public  Authen(){
	/**
	 * @author  Bile Bernard
	 */
	super("BHZMessenger                            "+new Date().toLocaleString());
	login=new JTextField(50);
	password=new TextField(50);
	password.setEchoChar('*');
	Font font =new Font("Verdana",Font.BOLD,13);
	password.setFont(font);
	login.setFont(font);
	//===============
	
	
	JMenu fichier=new JMenu("fichier");
	fichier.add(ajouter);
	JMenuBar bar =new JMenuBar();
	bar.add(fichier);
	setJMenuBar(bar);
	ajouter.addActionListener(this);
	//===========================
	valider=new JButton("Valider");
	nouveauCompte=new JLabel("<html><body>Cliquez <a href=\"\">ICI</a></body></html>");
	
	info=new JLabel();
	JPanel panneaucentral=new JPanel(),
	pan=new JPanel();
	pan.setLayout(new BorderLayout());
	
	JPanel pann=new JPanel();
	 pann.setBorder(
	         BorderFactory.createCompoundBorder(
	              BorderFactory.createCompoundBorder(
	                            BorderFactory.createTitledBorder("ACCES A LA MESSAGERIE "),
	                            BorderFactory.createEmptyBorder(5,5,5,5)),       //pour éloigné la bordure
	         pann.getBorder()));
	 
	panneaucentral.setLayout(new GridLayout(3,2,6,6));
	add(pann);
	pann.setLayout(new BorderLayout());
	pann.add("Center",panneaucentral);
	pann.add("West",pan);
	

	//(pan.getGraphics()).drawImage((new ImageIcon("titre.gif")).getImage(), pan.getX(), pan.getY(),pan.getWidth() ,pan.getHeight(), pan.getBackground(), this);
	pann.add("South",new JLabel("<html><body><code>copyrght BILL&Gaty corporation inc  BZHMessenger 1.0.0 (c)2011</code></body></html>"));
	JPanel panneauinterne=new JPanel();
	panneauinterne.setLayout(new GridLayout(4,0,6,6));
	
	panneauinterne.add(new JLabel("ENTREZ VOTRE LOGIN"));
	panneauinterne.add(login);//login.setToolTipText("saisisez votre login");
	panneauinterne.add(new JLabel("ENTREZ VOTRE MOT DE PASS"));
	panneauinterne.add(password);login.setToolTipText("<html>saisissez votre login:<br>si vous n'avez pas de compte<br> inscrivez vous!</html>");
	
	panneaucentral.add(new Canvas());
	panneaucentral.add(panneauinterne);
	panneaucentral.add(new Canvas());
	
	
	JPanel pan1=new JPanel();
	pan1.setLayout(new GridLayout(3,2,10,10));
	
	
	pan1.add(new Canvas());pan1.add(valider);
	 valider.setToolTipText("envoyer les donner au serveur");
	 valider.setMnemonic('E');

	pan1.add(new  JLabel("<html><body>si vous n'avez pas de compte,</body></html>"));
	pan1.add(this.nouveauCompte);
	panneaucentral.add(pan1);
	
	//JPanel pan2=new JPanel();
	
	
	panneaucentral.add(info);panneaucentral.add(new Canvas());
	/*pan2.add(nouveauCompte);pan2.add(new Canvas());
	pan2.add(new Canvas());pan2.add(new Canvas());
	add(pan2);add(new Canvas());*/
	
	valider.addActionListener(this);
	nouveauCompte .addMouseListener(this);
	password.addKeyListener(this);
	
	ImageIcon ico=new ImageIcon("images.gif");
	JLabel img = new JLabel(ico);
	pan.add(img);
	Image image=ico.getImage();
	setIconImage(image);
	setSize(700,400);
	//============================
	//centrer la fenetre
	Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

	this.setLocation(
	        (dimensionecran.width-this.getWidth())/2,
	        (dimensionecran.height-this.getHeight())/2
	        );

	
	addWindowListener(this);
	//====================
	//this.setLocationRelativeTo(this.getParent());
	//String res=CriptageB .CripterChaine("BERNARD*:/\\ =+",0);
	//JOptionPane.showMessageDialog(null,"<html>BERNARD= "+res+  "<br>  decodage="+DecriptageB.decripter(res,0)+"</html>");
	setVisible(true);
	
	this.setResizable(false);
	
     
}
	
	
	public static void main(String[] _) {
		new Authen();
	}

	
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource()==valider) {
			info.setText("<body><html><h1>traitement en cours...<br>veuillez patienter!!</h1></body></html>");
			String pass=password.getText(),log=login.getText();
			if(pass.compareToIgnoreCase("")==0 || log.compareToIgnoreCase("")==0) info.setText("login ou mot de pass incorrect!!");
			else
			{ Socket socket=null;
				try {
				 socket =new Socket(Config.IPserveur,Config.portServeur);
				BufferedReader lecture = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter  ecriture = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				
				String mes=Protocole.Auth+":"+log+":"+pass;
					//mes=CriptageB.CripterChaine(Protocole.Auth+":"+log+":"+pass,0);
				ecriture.println(mes);
				System.out.println(mes);
				mes=lecture.readLine();
				//String status=DecriptageB.decripter(mes,0);
				String status=mes;
				socket.close();
				lecture.close();
				ecriture.close();
				if(status.compareToIgnoreCase("OK")==0){
					new Messagerie(log);
					this.setVisible(false);
					
				}
				else{
					info.setForeground(Color.red);
					info.setText("<html><body><h3 fgcolor=red>"+status+"!</h3></body></html>");
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				info.setText("erreur de connexion!verifier votre connexion!");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				info.setText("<html><body>erreur d'ouverture de flux .<br>Veuillez configurer l'adresse du serveur dans menu Fichier</body></html>");
			}
				
				
				
				
				
				
			
			}
		}else if(e.getSource()==ajouter) {
			String ff=
			JOptionPane.showInputDialog("entrez l'adresse du serveur");
			if(ff!=null && ff.compareToIgnoreCase("")!=0) Config.IPserveur=ff;
		}
	}

	
	public void keyPressed(KeyEvent arg) {
		// TODO Auto-generated method stub
		if(arg.getKeyCode()==10) valider.doClick();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==nouveauCompte) new  NouveauCompte();
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	

}
