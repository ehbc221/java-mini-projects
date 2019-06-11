import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;


 class ThreadTchat1 extends Thread {
	 /**
		 * @author  Bile Bernard
		 */
	BufferedReader lecture=null;
	JList liste=null;
	JLabel info=null;
	String dest="";
	String Etat=null;
	String Etatici=null;
	 DefaultListModel model =null;
	@SuppressWarnings("rawtypes")
	Hashtable Discussion=null;
	//Color coulexpediteur=null;
@SuppressWarnings("rawtypes")
ThreadTchat1( BufferedReader lecture,JList liste,JLabel info,String dest,String Etat,String Etatici,Hashtable Discussion, DefaultListModel model){//,Color coulexpediteur){
		this.dest=dest;
		this.lecture=lecture;
		this.liste=liste;
		this.info=info;
		this.Etat=Etat;
		this. Etatici= Etatici;
		this.Discussion=Discussion;
		this.model=model;
		//this.coulexpediteur=coulexpediteur;
		start();
}

@SuppressWarnings("deprecation")
public void run(){
	while(true){
		
		String mes="";
		try {
			mes=lecture.readLine();
			//mes = DecriptageB.decripter(mes,0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(mes.compareToIgnoreCase("800")==0){
			info.setText(dest+" est en train d'écrire...");
			info.setVisible(true);
		}
		else if(mes.compareToIgnoreCase("900")==0) 
			info.setVisible(false);
		
		if((mes.compareToIgnoreCase("END")==0))//|| Etat.compareToIgnoreCase("CLOSED")==0||Etatici.compareToIgnoreCase("CLOSED")==0)
			stop(); 
		 if((mes.compareToIgnoreCase("null")!=0) && (mes.compareToIgnoreCase("800")!=0) &&( mes.compareToIgnoreCase("END")!=0 &&mes.compareToIgnoreCase("WIZZ")!=0&&mes.compareToIgnoreCase("900")!=0)){
			/*StringTokenizer tt=new StringTokenizer(mes,"=");
			String mess=tt.nextToken();
			Object couleur=tt.nextElement();//nextElement();
			System.out.println("couleur reçue:"+couleur);
			 
			// label.setForeground((Color) couleur);*/
			 JLabel label=new JLabel( );
			 label.setText(dest+":"+mes);
			// this.coulexpediteur=(Color) couleur;
				label.setToolTipText(""+new Date().toLocaleString());
				label.setFont(new Font("Serif",Font.BOLD,15));
								 model.addElement(label);
								 //liste.setForeground((Color) couleur);
		liste.setModel(model);
		 }
		 if ((mes.compareToIgnoreCase("WIZZ")==0) ) new  Wiz((Chat)this.Discussion.get(dest),30,2);
	}
}

 }
