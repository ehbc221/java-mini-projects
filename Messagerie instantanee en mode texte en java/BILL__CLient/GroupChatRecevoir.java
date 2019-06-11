import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;


public class GroupChatRecevoir extends Thread {
	/**
	 * @author  Bile Bernard
	 */
	BufferedReader  lecture=null;
	JList message=null;
	Socket socket=null;
	String Etat=null;
	DefaultListModel model = new DefaultListModel();
	GroupChatRecevoir(BufferedReader  lecture,JList message,Socket socket,String Etat){
		this.lecture=lecture;
		this.message=message;
		this.socket=socket;
		this.Etat=Etat;
		start();
	}
	@SuppressWarnings("deprecation")
	public void run(){
		while(true){
			try {
				
				if(socket.isClosed()|| Etat.compareToIgnoreCase("CLOSED")==0) {lecture.close();stop();}
				//===================================
				String mess=lecture.readLine();
					//mess=DecriptageB.decripter(mess,0);
				//=========================================================	
				if(mess.startsWith("info")){
					StringTokenizer t=new StringTokenizer(mess,":");t.nextToken();
					JOptionPane.showMessageDialog(null, t.nextToken());
				}
				else {
					model.addElement(mess);
					message.setModel(model);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
