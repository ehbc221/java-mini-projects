package Bill.partageFichier;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

// @Bile bernard
//            Treatment

public class Ecoute extends Thread
{
	Hashtable<String, String> listeDossierEncours=new Hashtable<String, String>();
    JLabel chaine=null;

 Vector<String> listereception=new Vector<String> ();
  Vector<JProgressBar> listeProsgresbar=new Vector<JProgressBar>();
  JButton pross=null;

    Ecoute(int port,JLabel chaine,JButton pros)
        
    {
    	this.port=port;
    	this.chaine=chaine;
        socket = null;
        this.pross=pros;
        try {
			socket = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        pross.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println("ça marche");
				 final JFrame FET=new JFrame("liste de progression de la reception");
                 FET.setSize(600, 400);
                 FET.setResizable(false);
                 FET.setDefaultCloseOperation(2);
                JPanel pan=new JPanel();
                pan.setLayout(new GridLayout(listeProsgresbar.size(),2));
                for(int j=0;j<listeProsgresbar.size() && j<listereception.size();j++){
                	pan.add(new JLabel(listereception.get(j)));
                	pan.add(listeProsgresbar.get(j));
                }
                 JScrollPane scroller = new JScrollPane( pan);
                 
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
                 FET.add("Center",scroller);
                 final JButton b=new JButton("Quitter");
                 FET.add("South",b);
                 FET.show();
                 b.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						 FET.dispose();
					}
				});
                 
			}
		});
        start();
    }

    public void run()
    {
        do
            try
            {
                java.net.Socket soc = socket.accept();
                JProgressBar pros=new JProgressBar();
                pros.setStringPainted(true);
                this.listeProsgresbar.add(pros);
                new Traitement(soc,listeDossierEncours,chaine,pros,listereception);
            }
            catch(IOException e)
            {
                
            }
        while(true);
    }

    private ServerSocket socket;
   private  int port ;//port d'écoute
}
