import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
class GroupChat extends JFrame implements KeyListener ,WindowListener,Runnable{
	/**
	 * @author  Bile Bernard
	 */
JList message=new JList();

JTextField text=new JTextField();
Socket socket=null;
int port=60000;
BufferedReader  lecture=null;
PrintWriter ecriture=null;
String Ipserveur=Config.IPserveur;
//JMenuItem menu1=new JMenuItem("config serveur");
String login="";
String Etat=null;

GroupChat(String login,String Etat){
	super("BZHMessenger 1.0.0 Discussion de group!");
	 JScrollPane scroller = new JScrollPane( message);
     
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
	add("Center", scroller);
	message.setVisible(true);
	message.setCellRenderer(new LabelListCellRendererM());
	this.message.setFont(new Font("Serif",Font.BOLD,20));
	add("South",text);
	text.addKeyListener(this);
	addWindowListener(this);
	setSize(500,600);
	Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

	this.setLocation(
	        (dimensionecran.width-this.getWidth())/2,
	        (dimensionecran.height-this.getHeight())/2
	        );
	this.setResizable(false);
	setIconImage((new ImageIcon("images.jpg")).getImage());
	this.login=login;
	this.Etat=Etat;
	 try {
		socket=new Socket(this.Ipserveur,this.port);
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 try {
		 ecriture= new PrintWriter(new BufferedWriter(  new OutputStreamWriter(socket .getOutputStream())), true);
		//=======================================
		// String mes=CriptageB.CripterChaine(login,0);
		 ecriture.println(login);
		 //============================================
		lecture=new BufferedReader(
				   new InputStreamReader(socket .getInputStream()));
		new GroupChatRecevoir(lecture,message,socket,this.Etat);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	setVisible(true);
}
@SuppressWarnings("deprecation")
@Override
public void run() {
	// TODO Auto-generated method stub
	if(Etat.compareToIgnoreCase("CLOSED")==0)  {
		new Thread().stop();
	}
}
@Override
public void windowActivated(WindowEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowClosed(WindowEvent e) {
	// TODO Auto-generated method stub
	//String mes=CriptageB.CripterChaine("END",0);
	 ecriture.println("END");
	
}
@Override
public void windowClosing(WindowEvent e) {
	// TODO Auto-generated method stub
	//String mes=CriptageB.CripterChaine("END",0);
	 ecriture.println("END");
}
@Override
public void windowDeactivated(WindowEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowDeiconified(WindowEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowIconified(WindowEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowOpened(WindowEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	if(e.getKeyCode()==10){
		//String mes=CriptageB.CripterChaine(text.getText(),0);
		 ecriture.println(text.getText());
		
		text.setText("");
	}
}
@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}



}
