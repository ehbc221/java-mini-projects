import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;


 @SuppressWarnings("serial")
class NouveauCompte  extends JFrame  implements ActionListener,KeyListener{
	 /**
		 * @author  Bile Bernard
		 */
	JTextField login=new JTextField (),
	tel=new JTextField (),
	ville=new JTextField ();
	//pays=new JTextField ();
	TextField pass=new TextField();
	
	JLabel info=new JLabel();
	

	 JButton valider=new JButton("VALIDER");;
	 NouveauCompte(){
		 super("BZHMessenger 1.0.0");
		 setIconImage((new ImageIcon("images.gif")).getImage());
		
		 JPanel pan=new JPanel();
		 pan.setBorder(
		         BorderFactory.createCompoundBorder(
		              BorderFactory.createCompoundBorder(
		                            BorderFactory.createTitledBorder("NOUVEAU COMPTE "),
		                            BorderFactory.createEmptyBorder(5,5,5,5)),       //pour éloigné la bordure
		         pan.getBorder()));
		pan. setLayout(new GridLayout(5,2,10,10));
		 
		pan. add(new JLabel("           IDENTIFIANT:"));
		pan. add(login);
		pan. add(new JLabel("           PASSWORD:"));
		pass.setEchoChar('*');
		pan. add(pass);
		pan. add(new JLabel("           TEL:"));
		pan. add(tel);
		pan. add(new JLabel("          SERVICE:"));
		pan. add(ville);
		//pan. add(new JLabel("          Pays:"));
		//pan. add(pays);
         Font font =new Font("Sanserif",Font.BOLD,15);
         login.setFont(font);
         pass.setFont(font);
         tel.setFont(font);
         ville.setFont(font);
         //pays.setFont(font);
         
         pan. add(info); pan.add(valider);valider.setToolTipText("envoyer les données  au serveur");
         add(pan);
         
         setSize(400,400);
         Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();

     	this.setLocation(
     	        ((dimensionecran.width-this.getWidth())/2)-100,
     	        (dimensionecran.height-this.getHeight())/2
     	        );
         setVisible(true);
         this.setResizable(false);
         ville.addKeyListener(this);
         valider.addActionListener(this);
         
}
	 

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==10)  {
			valider.doClick();
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent f) {
		
		if(f.getSource()==valider){
			if(login.getText().compareToIgnoreCase("")==0|| pass.getText().compareToIgnoreCase("")==0) {
				info.setForeground(Color.red);
				info.setText("login ou mot de pass incorect");
			}
			else{
				Socket socket;
				try {
					socket = new Socket(Config.IPserveur,Config.portServeur);
					//System.out.println(socket.getSoTimeout());
					PrintWriter ecriture= new PrintWriter(new BufferedWriter(  new OutputStreamWriter(socket .getOutputStream())), true);
					 
					 BufferedReader lecture=new BufferedReader(
							   new InputStreamReader(socket .getInputStream()));
					
					 if(tel.getText().compareToIgnoreCase("")==0) tel.setText("X");
					 if(ville.getText().compareToIgnoreCase("")==0) ville.setText("X");
					 //if(pays.getText().compareToIgnoreCase("")==0) pays.setText("X");
					 String mess=Protocole.NouveauCompte+":"+login.getText()+":"+pass.getText()+":"+tel.getText()+":"+ville.getText();//+":"+pays.getText();
					//mess=CriptageB.CripterChaine(mess,0);
					 
					 ecriture.println(mess);
					 mess=lecture.readLine();
					// mess=DecriptageB.decripter(mess,0);
					 
					 lecture.close();
					 ecriture.close();
					 socket.close();
					
					 if(mess.compareToIgnoreCase("login_error")==0) 
						 info.setText("ce login existe dejà!");
					 else{
						 JOptionPane.showMessageDialog(null, mess);
						this. setVisible(false);
					 }
						
					 
					 
					 
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					info.setForeground(Color.green);
					
					info.setText("verifier votre connexion");
				} catch (IOException e) {
					// TODO Auto-generated catch block
               info.setForeground(Color.blue);
					
					info.setText("erreur de connexion");
				}
				
				
				
				
				
				
				
			}
			
			
			
			
			
			
		}
		
	}
	

}
