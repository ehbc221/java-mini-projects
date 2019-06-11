import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.*;
//import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


@SuppressWarnings("serial")
public class Messagerie extends JFrame implements ActionListener,WindowListener,ListSelectionListener {
	/**
	 * @author  Bile Bernard
	 */
	JList listeAmi=new JList ();
	//public Authen  authentification=null;
	 static String Etat="OPENED";
	@SuppressWarnings("rawtypes")
	Hashtable Discussion=new Hashtable();
	JMenuItem ajouter=new JMenuItem("ajouter un contact");
	JMenuItem chatgroup=new JMenuItem("discussion de group"),
	IDChange=new  JMenuItem("modifier vos identifiant.."),
	Deconnexion=new JMenuItem("Deconnexion");
	String login=null;
	Socket messagerie=null;
	 BufferedReader messagerieLecture=null;
	 JLabel info=new JLabel();
	 
	 
	Messagerie(String login) throws IOException{
		super("BZHMessenger 1.0.0 "+ "    "+login+"@BZHMessenger");
		this.login=login;
		//this.authentification=authentification;
		add("South",info);
		info.setVisible(false);
		JMenu fichier=new JMenu("fichier");
		fichier.add(ajouter);
		fichier.addSeparator();
		fichier.add(this.chatgroup);
		fichier.addSeparator();
		fichier. add(IDChange);
		fichier.addSeparator();
		fichier.add(this.Deconnexion);
		JMenuBar bar =new JMenuBar();
		bar.add(fichier);
		setJMenuBar(bar);
		//JScrollPane scroller = new JScrollPane(listeAmi);
		listeAmi.setCellRenderer(new LabelListCellRendererM());
		JScrollPane scroller = new JScrollPane( listeAmi);
        
        scroller.setVerticalScrollBarPolicy(
       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
scroller.setHorizontalScrollBarPolicy(
  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

scroller.setPreferredSize ( new Dimension(300,150));
scroller.setBorder(
BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(
           BorderFactory.createTitledBorder(""),
           BorderFactory.createEmptyBorder(1,1,1,1)),       //pour éloigné la bordure
           scroller.getBorder()));
		add("Center",scroller );
		listeAmi.setVisible(true);
		this.listeAmi.setFont(new Font("Serif",Font.BOLD,15));
		ajouter.addActionListener(this);
		
		Deconnexion.addActionListener(this);
		chatgroup.addActionListener(this);
		
		listeAmi.addListSelectionListener(this);
		
		
		this.IDChange.addActionListener(this);
		addWindowListener(this);
		setIconImage((new ImageIcon("images.gif")).getImage());
		messagerie=new Socket(Config.IPserveur,Config.portServeur);
		messagerieLecture= new BufferedReader(
				   new InputStreamReader(messagerie .getInputStream()));
		PrintWriter ecriture = new PrintWriter(
				   new BufferedWriter(
				   new OutputStreamWriter(messagerie .getOutputStream())),
				   true);
		//================================
		//String mess=CriptageB.CripterChaine(Protocole.Connexion+":"+login,0);
		//System.out.println(mess);
		ecriture.println(Protocole.Connexion+":"+login);
		//==========================================================
		new MessagerieEcoute(messagerieLecture,login,Etat,this.Discussion);
		
		setBackground(new Color(167,20,171));
		setSize(500,600);
		Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(
		        (dimensionecran.width-this.getWidth())/2,
		        (dimensionecran.height-this.getHeight())/2
		        );
		setVisible(true);
		this.setResizable(false);
		
		new Causerie(listeAmi,this.login,Etat);
	}
	


	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ajouter){
			new AJouter(login);
		}
		else if(e.getSource()==this.chatgroup) new GroupChat(login ,Etat);
		else if(e.getSource()==this.Deconnexion) {
			deconnexion();
			//System.exit(0);
		}else if(e.getSource()==this.IDChange) {new ChangerIdentifiant(this.login);}
		
	}
	
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		Deconnexion.doClick();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//int x=JOptionPane.showConfirmDialog(this,"voulez-vous quittez vraiment!");
		
		Deconnexion.doClick();
		//this.principal.setVisible(true);
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
	@SuppressWarnings("deprecation")
	public void deconnexion(){
		
		
		Socket envoi=null;
				
				Etat="CLOSED";
				//JOptionPane.showMessageDialog(this,"deconnexion!!");
				
				try {
					envoi = new Socket(Config.IPserveur,Config.portServeur);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   

				   PrintWriter ecriture=null;
				try {
					ecriture = new PrintWriter(
					   new BufferedWriter(
					   new OutputStreamWriter(envoi .getOutputStream())),
					   true);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				//==============================================
				String mess=Protocole.Deconnexion+":"+login;
					//CriptageB.CripterChaine(Protocole.Deconnexion+":"+login,0);
				System.out.println(mess);
				ecriture.println(mess);
				//===============================================================
				ecriture.close();
				
				
				try {
					envoi.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.exit(0);
					e1.printStackTrace();
				}
				Etat="CLOSED";
				
				System.exit(0);
			}



	


	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void valueChanged(ListSelectionEvent a) {
		// TODO Auto-generated method stub
		JLabel label=(JLabel)((JList)(a.getSource())).getSelectedValue();
		if(label.getToolTipText().compareToIgnoreCase("votre contact est en ligne")==0){
		String destinataire=label.getText();
		if(!Discussion.containsKey(destinataire)){
			info.setVisible(false);	
		Chat ii;
		try {
			ii = new Chat(login,destinataire," ",Etat,Discussion);
			Discussion.put(destinataire, ii);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		else{
			info.setVisible(true);	
			info.setText("Discussion dejà en cours!");
			((Chat) Discussion.get(destinataire)).show();
		}
		}else{
			info.setVisible(true);	
			info.setText("<html><body><h3><code>votre contact est<font color=\"red\" hors ligne </font>impossible de discuter avec lui!<code></h3></body></html>");
		}
	}



	
	}



	

