import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

//pour ouvrir la connexion de façon automatique;
public class MessagerieEcoute  extends Thread {
	/**
	 * @author  Bile Bernard
	 */
	 BufferedReader messagerieLecture=null;
	 String source=null;
	 String Etat=null;
	 @SuppressWarnings("rawtypes")
	Hashtable Discussion=null;
	 @SuppressWarnings("rawtypes")
	MessagerieEcoute (BufferedReader messagerieLecture,String source,String Etat,Hashtable Discussion){
		 this.source=source;
		 this.messagerieLecture=messagerieLecture;
		 this.Etat=Etat;
		 this.Discussion=Discussion;
		 start();
	 }
	 @SuppressWarnings({ "deprecation", "unchecked" })
	public void run(){
		 while (true){
			 if(Etat.compareToIgnoreCase("CLOSED")==0) {
				 
				 stop();
			 }
			 try {
				 //=======================================
				 String mess=messagerieLecture.readLine();//billatosco:bonjour hive
				//String mess=DecriptageB.decripter(mes,0);
				//===================================================
				System.out.println("serv:"+mess);
				if(mess.compareToIgnoreCase("END")==0){JOptionPane.showMessageDialog(null, "UNE ERREUR FATALE S'est PRODUITE!!") ;System.exit(0);}
				if(!(mess.trim().endsWith("800")) && !(mess.trim().endsWith("900") )){
				StringTokenizer t=new StringTokenizer(mess,":");
				String dest=t.nextToken().trim();
				Chat chat=new Chat(source,dest,mess,Etat, Discussion);
				Discussion.put(dest, chat);
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
	 }
}
