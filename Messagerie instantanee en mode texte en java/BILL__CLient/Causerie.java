import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

class Causerie extends Thread{
	
	/**
	 * @author  Bile Bernard
	 */
 JList liste;
	String log;
	public String Etat=null;
	
	Causerie(JList liste, String log,String Etat){
		this.liste=liste;
		this.log=log;
		this.Etat=Etat;
		start();
		
	}
	@SuppressWarnings("deprecation")
	public void run(){
		ImageIcon vert=new ImageIcon("enligne.jpg"),rouge=new ImageIcon("horsligne.jpg");
		while(true){
	Socket envoi=null;
	try {
		System.out.println(this.Etat);
		if(Etat.compareToIgnoreCase("CLOSED")==0){stop();}
		String text=Protocole.Ami+":"+log;//mise à jour de la liste d'amis 
		envoi =new Socket(Config.IPserveur,Config.portServeur);
		  // System.out.println("SOCKET = " + envoi);

		   PrintWriter ecriture = new PrintWriter(
		   new BufferedWriter(
		   new OutputStreamWriter(envoi .getOutputStream())),
		   true);
		   BufferedReader lecture = new BufferedReader(
		   new InputStreamReader(envoi .getInputStream())
		   );
//text=CriptageB.CripterChaine(text,0);
		   ecriture.println(text);
		   text=lecture.readLine();
	// text=DecriptageB.decripter(text,0);
		System.out.println("message reçu"+text);
		try {
			lecture.close();
			envoi.close();
			ecriture.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringTokenizer t=new StringTokenizer(text,"=");
		liste.removeAll();
		DefaultListModel model = new DefaultListModel();
		try{
		while(t.hasMoreTokens()){
			StringTokenizer t1=new StringTokenizer(t.nextToken());
			String lo=t1.nextToken(),status=t1.nextToken();
		if(lo.compareToIgnoreCase("null")!=0) {
			JLabel Label=new JLabel(lo);
			
			
			        if(status.compareToIgnoreCase("en_ligne")==0){
			        	Label.setToolTipText("votre contact est en ligne");
			        	Label.setIcon(vert);
			        }
			        else{
			        	Label.setToolTipText("votre contact est hors ligne");
			        	 Label.setIcon(rouge);
			        }
			        model.addElement(Label);
		}
		}
		
		//liste.removeAll();
		try{
		 liste.setModel(model);
		}catch (Exception e) {
			//JOptionPane.showMessageDialog(null, e);
			
		}
		
		}catch(Exception e){
			
		} 		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		Thread.sleep(3000);//il dort pendant 3s
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
	
	
}
