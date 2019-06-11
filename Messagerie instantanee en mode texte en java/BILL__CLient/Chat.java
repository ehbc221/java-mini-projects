import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


@SuppressWarnings("serial")
public class Chat extends JFrame implements KeyListener,WindowListener, ActionListener, ListSelectionListener {
	
	/**
	 * @author  Bile Bernard
	 */
	
	
	JTextField  saisie=new JTextField();
    JList Message=new JList();
    String expediteur=null;
	String destinataire=null,
	
	
	 Etat="OPENED";
	static Color couleur=Color.PINK;
	DefaultListModel model = new DefaultListModel();
	JLabel info=new JLabel();
	//Color coulexpediteur=Color.black ,couldestinataire=Color.pink;
	
	 PrintWriter ecriture=null;
	 BufferedReader lecture=null;
	 @SuppressWarnings("rawtypes")
	Hashtable Discussion=null;
	 String Etatici=null;
	 JButton wizz=null;
	 //code WIZZ pour wizzz.
	@SuppressWarnings({ "deprecation", "rawtypes" })
	Chat(String expediteur,String destinataire,String message,String Etat, Hashtable Discussion) throws IOException{
		super("BZHMessenger 1.0.0   live  "+expediteur +" <==========>"+destinataire);
		this.expediteur=expediteur;
		this.destinataire=destinataire;
		
		this.Discussion=Discussion;
		this.Etat=Etat;
		wizz=	new JButton("wizz");
		Message.addListSelectionListener(this);
		Message.setCellRenderer(new LabelListCellRendererM());
		this.Message.setFont(new Font("Serif",Font.BOLD,20));
		if(( message.compareToIgnoreCase(" ")!=0)&&(message.compareToIgnoreCase("END")!=0)&&(message.compareToIgnoreCase("900")!=0)&&(message.compareToIgnoreCase("800")!=0)&&(message.compareToIgnoreCase("WIZZ")!=0))
		{
			/*StringTokenizer tt=new StringTokenizer(message,"=");
			message=tt.nextToken();
			Object coul=tt.nextElement();//nextToken();
			
			this.couldestinataire=(Color) coul;
			System.out.println(coul+"            "+this.couldestinataire);*/
		JLabel label=new JLabel(message);
		
		
			label.setToolTipText(""+new Date().toLocaleString());
			label.setForeground(Color.CYAN);
			label.setFont(new Font("Serif",Font.BOLD,15));
			
			 model.addElement(label);
			 Message.setModel(model);
		}

		 JScrollPane scroller = new JScrollPane( Message);
           
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
	
	//wizz.setVisible(false);
		
		 setLayout(new BorderLayout());
		 JPanel pan=new JPanel(),
		 pan1=new JPanel();
    	 add("Center", pan);
    	 pan.setLayout(new BorderLayout());
    	 //=================
    	 
    	
    	 //===========================
    	 pan.add("Center",scroller );
    	 Message.setVisible(true);
    	 pan.add("South",info);
    	 add("South",saisie);
    	 saisie.setToolTipText("appuyer control+alt+c pour changer la couleur du texte");
    	 saisie.setForeground(couleur);
    	 add("North",pan1);
    	 wizz.addActionListener(this);
    	// pan1.add(wizz);
    	 addWindowListener(this);
    	 saisie.addKeyListener(this);
    	 {
    	 Font font=new Font("Sanserif",Font.BOLD,18);
    	 saisie.setFont(font);
    	 Message.setFont(font);
    	 }
    	 setSize(500,600);
    	 Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

    		this.setLocation(
    		        ((dimensionecran.width-this.getWidth())/2)-100,
    		        (dimensionecran.height-this.getHeight())/2
    		        );
    	 setVisible(true);
    	 this.setResizable(false);
    	 setIconImage((new ImageIcon("images.gif")).getImage());
    	 Socket tchat=new Socket(Config.IPserveur,Config.portServeur);   
			ecriture = new PrintWriter(
			   new BufferedWriter(
			   new OutputStreamWriter(tchat .getOutputStream())),
			   true);
			
			lecture=(new BufferedReader(
					   new InputStreamReader(tchat .getInputStream())
			   ));
			//saisie.setForeground(coulexpediteur);
			//========================================================
			//String mess=CriptageB.CripterChaine(Protocole.Chat+"="+this.expediteur+":"+this.destinataire,0);
			ecriture.println(Protocole.Chat+"="+this.expediteur+":"+this.destinataire);//pour la discussionncode 4
			//===================================================================================
			new ThreadTchat1( lecture,Message,info,this.destinataire, Etat, Etatici, this.Discussion, model );//,this.couldestinataire);
			new  EstENTrainEcrie(saisie, expediteur, destinataire, ecriture);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void keyPressed(KeyEvent a) {
		wizz.setVisible(true);
		String mess=saisie.getText().trim();
		
		//code de votre correspondant est en train d'écrire
		//if(a.isAltDown()&&a.isControlDown()&(a.getKeyCode()==67||a.getKeyCode()==99)){
		//	this.coulexpediteur=JColorChooser.showDialog(null,
		
	           // "choisir couleur du texte", getBackground());
			//saisie.setForeground(coulexpediteur);}
		if(a.getKeyCode()==10){
			if(mess.compareToIgnoreCase("")!=0){
				
				JLabel label=new JLabel();
				label.setForeground(couleur);
				label.setText("vous:" +mess);
                   label.setToolTipText(""+new Date().toLocaleString());
				
				label.setFont(new Font("Serif",Font.BOLD,15));
				//===========================================================
				 //mess=CriptageB.CripterChaine(expediteur+":"+destinataire+":"+mess,0);
				 mess=expediteur+":"+destinataire+":"+mess;
				ecriture.println(mess);//+"="+this.coulexpediteur);
				//===============================================================
				
				//Message.setForeground(couleur);
				 model.addElement(label);
				 Message.setModel(model);
				
	saisie.setText("");
			}
		}else if(a.getKeyCode()==18) wizz.doClick();
		else if(a.isAltDown() && a.isControlDown()&& (a.getKeyCode()==67||a.getKeyCode()==99)){//alt+ctrl+c ou alt+ctrl+C
			couleur=JColorChooser.showDialog(this,
		            "choix de la couleur du texte à envoyer?",
		            Color.red);
			saisie.setForeground(couleur);
		}
	
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
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub$
		//String mess=CriptageB.CripterChaine(expediteur+":"+destinataire+":END",0);
		String mess=expediteur+":"+destinataire+":END";
		ecriture.println(mess);
		if(Discussion.containsKey(destinataire)) Discussion.remove(destinataire);
		System.out.println(Discussion);
		 Etatici="CLOSED";
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		String mess=expediteur+":"+destinataire+":END";
		ecriture.println(mess);
		if(Discussion.containsKey(destinataire)) Discussion.remove(destinataire);
		//System.out.println(Discussion);
		 Etatici="CLOSED";
		

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
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//====================================================
		String mess=expediteur+":"+destinataire+":WIZZ";
		ecriture.println(mess);
		//================================================
		new  Wiz((Chat)this.Discussion.get(this.expediteur),30,2);
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
