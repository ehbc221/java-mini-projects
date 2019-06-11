package Bill.partageFichier;

import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import javax.imageio.stream.FileImageInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;


public class ENVOIFICHIER extends JFrame   implements ActionListener, WindowListener
{
	/*@Author Bile Bernard*/
	  private static final long serialVersionUID = 1L;
	static String  chaine="";
	static long start ,fin;
	static String debit="",Tempsrestant="";
	
	static Vector<Float> listeDebit=new  Vector<Float> ();
	static JLabel chaineAutre=new JLabel();
	   private String  NumPortfich="NumPortfich.txt" ;//fichier qui va memoriser le numero de port d'ecoute  
	   // pour permettre qu'il soit modifier à sa guise
	  private   int portEcoute;
	  private static int limit = 64000;
	  private static final  int portPardefaut=64000;
	  //port d'ecoute pour la reception du fichier par defaut
	  private JButton Ouvrir;
	  private static  JButton Envoyer;
	  private static  JTextArea adresse;
	  private static  JTextField url;//url du fichier à envoyer
	  static JButton pross=new JButton();
	  private JMenuItem historique;
	  private JMenuItem _32000_octet;
	  private JMenuItem _64000_octet;
	  private JMenuItem _16000_octet;
	  private JMenuItem _8000_octet;
	  private JMenuItem _1000_octet;
	  private JMenuItem  changerNumPort;
	  private JMenuItem evolutionReception=new JMenuItem("voir l'evolution de la reception");
	  private   JMenuItem fermer;
	private static JLabel tempslabel=new JLabel();
	private static JLabel debitlabel=new JLabel();
	  static JProgressBar[] barprogression=new JProgressBar[2];
	  static JLabel[] labelprogresbas=new JLabel[2];
	  static Checkbox[] box=new Checkbox[2];
	  private static Hashtable<String,String> controle=new Hashtable<String,String>();
	@SuppressWarnings("unused")
	private static double tempsestimé=0;
	private static float gh;
	private static long compteurd=0;
    @SuppressWarnings("deprecation")
	ENVOIFICHIER(String s, String s2)
        throws IOException
    {
        super((new StringBuilder("B&G FDFTS 1.1.9.0             ")).append((new Date()).toLocaleString()).toString());
       
        
        Ouvrir = new JButton("Selectionner");
        Envoyer = new JButton("Envoyer");
        adresse = new JTextArea();
        
        url = new JTextField();
        JMenuItem chercherserveur=new JMenuItem("cherche s'il y'a d'autre serveur");
       /* chercherserveur.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					Vector<String> vect =listeHoteLocal();
					int j=0;
					while(j<vect.size()){
						Socket soc=new Socket(vect.get(j),ENVOIFICHIER.this.portEcoute);
						soc.close();
						System.out.println("un serveur du mème type tourne sur cette machine");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
        
       adresse.addKeyListener(new KeyListener() {
		
		@Override
		public void  keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("clicked"+e.isControlDown()+ e.getKeyCode()+":"+e.getKeyChar());
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void  keyPressed(KeyEvent e) {
			
			// TODO Auto-generated method stub
			boolean t=(e.getKeyCode()=='H' || e.getKeyCode()=='h')&& e.isControlDown();
			if(t){
				
				
				try {
					Vector<String> tt=listeHoteLocal();//pour recuperer la liste des ordi du resea local
					if(tt.size()>0){
						adresse.setText(tt.get(0));
						for(int j=1;j<tt.size();j++) adresse.setText(adresse.getText()+";"+tt.get(j));
						String s=adresse.getText();
						
						if(s.indexOf(';')<=1) adresse.setText(s.substring(s.indexOf(';'+1,s.length())));
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	});
        historique = new JMenuItem("Historique d'envoi reception");
        fermer = new JMenuItem("quitter");
        changerNumPort=new JMenuItem("changer le port d'ecoute");
        _64000_octet=new JMenuItem("taille maxi du paquet:64KO");
        _32000_octet=new JMenuItem("taille maxi du paquet:32KO");
        _16000_octet=new JMenuItem("taille maxi du paquet:16KO");
        _8000_octet=new JMenuItem("taille maxi du paquet:8KO");
        _1000_octet=new JMenuItem("taille maxi du paquet: 1KO");
         final JMenu  host=new JMenu("listedes hosts");
         
			
			
        JMenu changerLimitTranfert=new JMenu("changer la taille du paquet");
        changerLimitTranfert.add(_1000_octet);
        changerLimitTranfert.add( _8000_octet);
        changerLimitTranfert.add( _32000_octet);
        changerLimitTranfert.add( _64000_octet);
        changerLimitTranfert.add(host);
        
        {
				
        }
		
        setLayout(new GridLayout(2, 1));
        JPanel pan1 = new JPanel();
        add(pan1);
        pan1.setLayout(new GridLayout(2, 1));
        pan1.setBackground(new Color(27, 228, 203));
        JPanel deb=new JPanel();
        deb.setLayout(new GridLayout(2, 1));
        pan1.add(deb);
       deb.add(new JLabel("<html><h3><font color=red>Entrez le(s) nom(s) ou l'IP <br>de(s) (la) machine(s) destination:a.b.c.d ;a'.b'.c'.d'</font></h1></html>"));
       JPanel pane=new JPanel();
       deb.add(pane);
       pane.setLayout(new GridLayout(1,2));
       pane.add(debitlabel);
       pane.add(tempslabel);
       pan1.add(adresse);
        JPanel pan = new JPanel();
        JPanel panhaut = new JPanel();
        JPanel pansud = new JPanel();
        add(pan);
        pan.setLayout(new GridLayout(2, 1));
        pan.add(panhaut);
        pan.add(pansud);
        pansud.setLayout(new GridLayout(1, 1));
        pansud.setBackground(new Color(167, 88, 165));
        panhaut.setLayout(new GridLayout(2, 1));
        JPanel  panprogress=new JPanel();
        panprogress.setLayout(new GridLayout(2,2));
        
      pansud.add(panprogress);
      CheckboxGroup boxgroup=new CheckboxGroup();
     
      for(int j=0;j<barprogression.length;j++){
    	   barprogression[j]=new JProgressBar();
    	  // barprogression[j].setBackground(Color.GREEN);
    	   box[j]=new Checkbox(((j==0)?"envoi de dossier":"envoi de fichier"),false,boxgroup);
    	   barprogression[j].setStringPainted(true);
    	   barprogression[j].setVisible(true);
    	   //barprogression[j].setSize(new Dimension(panprogress.getWidth(),panprogress.getHeight()/3));
    	   labelprogresbas[j]=new JLabel();
    	   panprogress.add( labelprogresbas[j]);
    	   panprogress.add(  barprogression[j]);
       }
     
        JPanel ss = new JPanel();
        panhaut.add(ss);
        ss.setLayout(new GridLayout(1, 2));
        ss.add(url);
        ss.add(Ouvrir);
       
        JPanel tt = new JPanel();
        panhaut.add(tt);
        tt.setLayout(new GridLayout(1, 3));
        tt.add(box[0]);
        tt.add(box[1]);
        tt.add(Envoyer);
        box[0].addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(url.getText().compareToIgnoreCase("")!=0 && url.getText()!=null ) {
					url.setText(new File(url.getText()).getParent());
					box[1].setState(true);
				}
			}
		});
        panhaut.setBackground(new Color(123, 200, 230));
        adresse.setFont(new Font("SanSerif", 1, 20));
        adresse.setToolTipText("<html>donner l'adresse IP de la machine distante<br>Utiliser des <h1>;<br></h1> pour cas d'envoi multipe<br>vous pouvez specifier le numero de port destination <br>par<h1> @ip:numport</h1><br>CTRL +H pour ajouter <br> les ordi du reseau local</html>");
        Ouvrir.setFont(new Font("SanSerif", 1, 15));
        Envoyer.setFont(new Font("SanSerif", 1, 15));
        Envoyer.addActionListener(this);
        addWindowListener(this);
        Ouvrir.addActionListener(this);
        fermer.addActionListener(this);
        historique.addActionListener(this);
        _1000_octet.addActionListener(this);
        _8000_octet.addActionListener(this);
        _16000_octet.addActionListener(this);
        _64000_octet.addActionListener(this);
        _32000_octet.addActionListener(this);
        changerNumPort.addActionListener(this);
        JMenuBar bar = new JMenuBar();
        
        JMenu Fichier = new JMenu("FIchier");
        JMenu HISTORIQUE = new JMenu("HIstorique");
        Fichier.add(fermer);
        Fichier.add(chercherserveur);
        fermer.setToolTipText("quitter l'application");
        Fichier.add(changerNumPort);
        Fichier.add( changerLimitTranfert);
        changerNumPort.setToolTipText("changer le port d'écoute");
        HISTORIQUE.add(historique);
        HISTORIQUE.addSeparator();
        HISTORIQUE.add( evolutionReception);
        evolutionReception.addActionListener(this);
        historique.setToolTipText("ouvrir l'historique d'envoi et reception");
        url.setToolTipText("selectionner un fichier");
        bar.add(Fichier);
        bar.add(HISTORIQUE);
        setJMenuBar(bar);
        Fichier.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("ça marche iic pressed");
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				Vector<String> hoste;
				try {
					hoste = listeHoteLocal();
					final JCheckBoxMenuItem[] men=new JCheckBoxMenuItem[hoste.size()];
					for(int j=0;j<men.length;j++){
						men[j]=new JCheckBoxMenuItem(hoste.get(j));
						host.add(men[j]);
						final int k=j;
						men[j].addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								String texte=men[k].getText();
								if(adresse.getText()!=""){
									adresse.setText(adresse.getText()+";"+texte);
								}else
									adresse.setText(texte);
							}
						});
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        setResizable(false);
        setIconImage((new ImageIcon("bi.jpg")).getImage());//pour modifier l'icone ;mettez l'adresse d'une image existante
        //108.128
        {
        	File f=new File("HISTORIQUE.txt");
        	if(!f.exists())  new FileOutputStream(f);
        	
        	
        }
        setVisible(false);
        if(s != null && s2 != null)//au cas ou on fait appel a partir d'une lingne de commande  avec des argument
        {
            url.setText(s);
            adresse.setText(s2);
            Envoyer.doClick();
           
        } else
         if(s != null){
            url.setText(s); 
            System.out.println("donner l'adresse du (des) destinataire separer par\n des points virgules:");
            adresse.setText(new Scanner(System.in).nextLine());
            Envoyer.doClick();
        }else{
        	 setVisible(true);
        }
        //new searchBGFTSHost();}
    }

    public static void main(String args[]) throws IOException
    {
    	//listeHoteLocal();
       try
        {
            if(args.length > 1)
                new ENVOIFICHIER(args[0], args[1]);
            else if(args.length==1)
                new ENVOIFICHIER(args[0], null);
            else new ENVOIFICHIER(null, null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    	
    	
    }

    @SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
    {
    	if(e.getSource() == _1000_octet) limit=1000;
    	else if(e.getSource()==evolutionReception) pross.doClick();
    	else if(e.getSource() == _8000_octet) limit=8000;
    	else if(e.getSource() == _32000_octet) limit=32000;
    	else if(e.getSource() == _64000_octet) limit=64000;
        if(e.getSource() == fermer){
        	try {
        		new PrintStream(new FileOutputStream(this.NumPortfich)).println((""+this.portEcoute).trim());
				 new PrintStream(new FileOutputStream("HISTORIQUE.TXT")).println((chaine+chaineAutre.getText()))	;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("erreur d'enregistrement du num port");
			}
            System.exit(0);
        }
        if(e.getSource() == this.changerNumPort)
        {
        	String g=JOptionPane.showInputDialog(this,"<html>donner le nouveau port (1 à 65536)<br>port actuel:<h3>"+this.portEcoute+"</h3></html>");
        	if(g!=null && !g.equals("")) {
        		int h=Integer.parseInt(g);
        		if(h>0 && h<=65536) this.portEcoute=h;
        	}
        	System.out.println(this.portEcoute);
        }
        if(e.getSource() == historique)
        {
            File f = new File("HISTORIQUE.txt");
            if(f.exists()){
                // Scanner scan = new Scanner(f);
                    String texte="<html>"+chaine+chaineAutre.getText()+"</html>";
                    /*BufferedReader scan=new BufferedReader(new FileReader(f));
                    String h;
                    while( (h=scan.readLine())!=null)
                    	texte+=h;
                    
                    //while(scan.hasNext()) texte+="<br>"+scan.nextLine();
                    //texte+="</font></html>";*/
                    final JFrame FET=new JFrame("Informations Historique");//pour afficher l'historique d'envoi et reception
                    FET.setSize(600, 600);
                    FET.setResizable(false);
                   
                    JScrollPane scroller = new JScrollPane( new JLabel(texte));
                    
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
                    
                    b.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							FET.dispose();
							
						}
					});
                    FET.show();
                 /// FET.getGraphics().drawOval( FET.WIDTH/2,  FET.HEIGHT/2,  FET.WIDTH,  FET.WIDTH);
                    //JOptionPane.showMessageDialog(this, texte);
               }      
        }
        if(e.getSource() == Ouvrir)
        {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            JFileChooser fileChooserSaveImage = new JFileChooser(fsv.getRoots()[0]);
            if( box[0].getState()){//pour envoyer un dossier
            	 box[1].setState(true);
            	fileChooserSaveImage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            	
            }
            int retour = fileChooserSaveImage.showOpenDialog(this);
            if(retour == 0)
            {
                File url1 = fileChooserSaveImage.getSelectedFile();
                if(url1.exists()){
                	if(box[0].getState())
                	 url.setText(url1.getParent());
                	else
                		url.setText(url1.getAbsolutePath());
                }
                   
                else
                    JOptionPane.showMessageDialog(null, "fichier corrompu!!");
            }
        } else
        if(e.getSource() == Envoyer)
            if(adresse.getText().compareToIgnoreCase("") == 0)
                JOptionPane.showMessageDialog(null, "<html><h1>le champ adresse est vide!!</h1></html>");
            else
            if(url.getText().compareToIgnoreCase("") == 0)
            {
                JOptionPane.showMessageDialog(null, "<html><h1>aucun fichier n'a \351t\351 selectionn\351!!</h1></html>");
            } else
            {
                String adr = adresse.getText();
                String add[] = (String[])null;
                if(adr.indexOf(";") >0)// cas d'envoi à plusieurs destinataires
                {
                    StringTokenizer tt = new StringTokenizer(adr, ";");
                    int l = tt.countTokens();
                    add = new String[l];
                    for(int j = 0; tt.hasMoreTokens(); j++)
                        add[j] = tt.nextToken();

                } else
                {
                    add = new String[1];
                    add[0] = adr;
                }
                
                new ThreadEnvoi (url.getText(), add);//pour eviter le mode bloquant
            }
        System.out.println(limit);
    }
//============================fonction envoyer=========
    



 
 
 
 
 
 
 
 
 
 
 
 
 
	

	public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent arg0)
    {
    	
        System.exit(0);
    }

    public void windowClosing(WindowEvent arg0)
    {
    	//on enregistre le numero de port
    	
    		
			this.fermer.doClick();
			 //new PrintWriter(new FileOutputStream()).println(chaine);
		
        System.exit(0);
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent arg0)
    {
    	
    	//this.setVisible(false);
        //JOptionPane.showMessageDialog(null, "<html><h1><font color=blue><br><br>WELCOME IN MY PROGRAMME<br><BR></font></h1></html>");
       
        //on recupère e numero de port 
       try {
    	   
    	   int po=Integer.parseInt(new BufferedReader(new FileReader(this.NumPortfich)).readLine());
    	   chaine=new BufferedReader(new FileReader("HISTORIQUE.txt")).readLine();
        	this.portEcoute=po;//.nextInt();
        	System.out.println(po);
        }catch(Exception e){
        	System.out.println("ERREUR dOnc par defaut");
        	this.portEcoute=portPardefaut;// en cas d'erreur de lecture on utilise le port par defaut
        }
       for(int h=0;h<10;h++){};
        setSize(600, 500);// la taille de la fenetre
       // pour centrer la fenetre
        Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();
    	this.setLocation(
    	        (dimensionecran.width-this.getWidth())/2,
    	        (dimensionecran.height-this.getHeight())/2
    	        );
        this.setVisible(true);
       // this.pack();
        new Ecoute(this.portEcoute,chaineAutre, pross);
       // pross.doClick();
    }

    @SuppressWarnings("deprecation")
	public  static boolean envoiFichier(String fich, String host[],int type,String etat,String dossier,String sous) //type:100 fichier ,101 dossier
	        throws IOException
	    {
    	//pross.doClick();
    	if(!new File(fich).isDirectory()&&!controle.containsKey("controle")){// pour tester s'il n'y a pas un fichier en cours d'envoi
    		boolean testfile=true;
    		//je voulais que ça supporte bcp de protocole  mais c'est http et file qui sont implementer
			if(fich.startsWith("http")) testfile=false; //www.exemple.com/file.pdf

	         controle.put("controle", "true");
    	      // adresse.setVisible(false);
    	        if(testfile)	 labelprogresbas[1].setText("<html><font color=blue>evolution de l'envoi du fichier:<br>"+new File(fich).getName()+"</font></html>");
    	        
    	        
    
    	      
	        int nbrhost = host.length;
	        float evolution=0;
	   
	        File fichier=null;
	     if(testfile)    fichier=new File(fich);
	     
	        	 
	            Socket s[] = new Socket[nbrhost];
	            OutputStream fluxsortie[] = new OutputStream[nbrhost];
	            BufferedInputStream fluxentree[] = new BufferedInputStream[nbrhost];
	            for(int j = 0; j < nbrhost; j++)//l'nevoi paralelle par creation des differents flux d'entrée sortie
	            {try{
	            	int portdest = portPardefaut;
	            	host[j]=host[j].trim();
	            	if(host[j].indexOf(":")>0)// on teste si on a indiqué un port d'ecoute  sur l'hote
	            	{
	            		StringTokenizer tt=new StringTokenizer(host[j],":");//on recuppère l'adresse et le num port
	            		host[j]=tt.nextToken();
	            		portdest=Integer.parseInt(tt.nextToken());
	            	//	System.out.println(host[j]+":"+portdest);
	            		
	            	}
	                s[j] = new Socket(host[j], portdest);
	                fluxentree[j] = new BufferedInputStream(s[j].getInputStream());
	                fluxsortie[j] = s[j].getOutputStream();
	            }catch(Exception e){
	            	JOptionPane.showMessageDialog(null, "cette ERREUR s'est produiteI!!"+e);
	            }
	                
	            }
	            long taillefichier = 0;
	            if(testfile)taillefichier =fichier.length();
	        
	        long blocEnvoi = taillefichier / 64000L;
	       // System.out.println((new StringBuilder("nombre de pas suppos\351:")).append(blocEnvoi).toString());
	       
	        
	    
	    
	        String extension =null;
	        boolean envoid=false;
	        if(type==100){//cas d'un fichier
	        	 tempslabel.setText("transfert en cours...");
	        	if(testfile)
	            extension ="F_:"+fichier.getName()+":"+taillefichier;
	        	else
	        		extension ="F_:"+new URL(fich).getFile().substring(new URL(fich).getFile().lastIndexOf("/")+1)+":"+taillefichier;
	        }else{
	        	 extension ="DOS_:"+etat+":"+dossier+":"+sous+":"+fichier.getName()+":"+taillefichier;//
	        	 envoid=true;
	        }
	        System.out.println("paquet à envoyer:"+extension);
	        byte lo[] = extension.getBytes();//recuperation du nom du fichier suivie de l'envoi aux hotes
	        
	        for(int i = 0; i < nbrhost; i++)
	           if(s[i].isConnected()) 
	        	   fluxsortie[i].write(lo,0,lo.length);

	      fluxentree[(int) ((nbrhost-1)*Math.random())].read();//ici on peux juste attendre un petit temps avant  de commencer à envoyer 
	        
	      // System.out.println("pret à envoyer////"+x+"  nbr:"+nbrhost);
	       if(type!=101)  JOptionPane.showMessageDialog(null, "envoi  en cours.....");
	        byte donnee[] = new byte[limit];
	        long compteurcourant = 0;
	      
	        int lu;
	       start=System.currentTimeMillis();//pour le calcul du debit
	      
	    	 InputStream in=null; 
	  	     if(testfile)in=fichier.toURL().openStream();
	  	     else in=new URL(fich).openStream();
	        while((lu = in.read(donnee)) != -1) //tant que c'est pas la fin du fichier
	        {
	        	debitlabel.setText("<html>"+debit +"<br> </html>"  );
	        	if(thereNotConnected (s)) break;//test si aucun des hote n'est joingnable
	            compteurcourant += lu;
	            for(int j = 0; j < nbrhost; j++)//envoi des bloc d'octets aux destinataires
	            	if(s[j].isConnected()) 
	            		 fluxsortie[j].write(donnee, 0, lu);
	            if(envoid) compteurd+=lu;
	            //fin=System.currentTimeMillis();
		        //debit=String.valueOf(((float)   8*compteurcourant/(1000* (fin - start)))+"   Mbits/S");
		       // tempsestimé=(float)((8*compteurcourant)/Integer.parseInt((new StringTokenizer(debit).nextToken())));
	           if(testfile) evolution=( (100*compteurcourant)/taillefichier);//au cas ou on utiliseur un JProgressBar
	           else evolution=0;
	            barprogression[1].setValue((int) evolution);
	            barprogression[1].setString(barprogression[1].getValue()+"%");
	         //  System.out.println("envoi:"+evolution+"%");
	        }
	        in.close();
	       // adresse.setVisible(true);
	       
	        fin=System.currentTimeMillis();
	        long T=fin - start;//temps d'envoi des octets pour le calcul du debit
	        System.out.println("temps ecoulé:"+T);
	       if(T>0){
	    	   float gh1=(float) (  (8*compteurcourant)*1./(1000* (T)));//Mbits 
	      
	        listeDebit.add(gh1);// memoire sur le debit pour une moyenne 
	        if(gh1<0.1)
	        debit=(gh1*1000)+"   .Kbits/S";
	        else
	        	 debit=(gh1)+"   Mbits/S";
	       }
	       gh=debitMoyen( listeDebit);
	       if(!envoid)
	    	   tempslabel.setText("fin");/*{
	    	   tempsestimé= (double) (.000001d*((8*fichier.length())*1./gh));
				 int ss=(int) (tempsestimé %60),min=(int) ((tempsestimé/60) %60),h=min/60;
				 
				 Tempsrestant =((h>0)?h +"  h  ":"")+((min>0)?  min+" mn   ":"")+((ss>0)?  ss +" S":"");
				 tempslabel.setText("<html><h3>temps estimé: "+Tempsrestant+"</h3></html>");
	       }*/
	       // tempsestimé=(float)((8*(taillefichier-compteurcourant))*1./(1000000*Integer.parseInt((new StringTokenizer(debit).nextToken()))));
	        debitlabel.setText("<html>"+debit +"<br>"+"</html>"  );
	        //tempslabel.setText("");
	       
	        //fin de l'envoi ... je libère les ressource
	        for(int j = 0; j < nbrhost; j++)
	        {
	            fluxsortie[j].flush();
	            s[j].close();
	        }
	      
	       // System.out.println("fin d'envoi");
	         chaine += (new StringBuilder("<h1>NOM FICHIER:")).append("<font color=red>"+fich+"</font>").append("<br>DESTINATAIRE:").append("<font color=blue>"+adresse.getText()+"</font>").append("<br>Date d'envoi:").append("<font color=blue>"+(new Date()).toGMTString()+"</font>").append("</h1></br>").toString();
	        
	        controle.clear();
	      
	        barprogression[1].setValue(0);
	        barprogression[1].setString(barprogression[1].getValue()+"%");
	        labelprogresbas[1].setText("");
	        return true;
    	}
    	return false;
	}
	private static boolean thereNotConnected(Socket[] s){

		boolean rs=true;
		for(int j=0;j<s.length;j++) rs=rs && (!s[j].isConnected());
		return rs;
	}

	public static boolean envoiDossier(String dossier,String host[]) throws IOException  {
		labelprogresbas[0].setText("chargement des fichiers...");
		try{
		 Vector<String> fichiers=
		 lister( dossier);
		 String[] listedossier=retournerBase(fichiers,new File(dossier).getName());
		 long tailletotal=tailleDossier(fichiers,0);
		
		 //==================================
		// afficher(fichiers);
		 int n=fichiers.size();
		 int j=0;
		
		
		
		 labelprogresbas[0].setText("<html><font color=red>dossier:"+new File(dossier).getName()+"  \t taille :"+taillepro(tailletotal)+" <br><u> evolution:</u> \t<br>taille envoyée:"+taillepro(compteurd)+"</font></html>");
		while(j<n){
			 boolean ff=!controle.containsKey("controle");
			 
			 tempsestimé= (double) (.000001d*((8*tailleDossier(fichiers,j))*1./gh));
			 int s=(int) (tempsestimé %60),min=(int) ((tempsestimé/60) %60),h=min/60;
			 
			 Tempsrestant =((h>0)?h +"  h  ":"")+((min>0)?  min+" mn   ":"")+((s>0)?  s +" S":"");
			 tempslabel.setText("<html><h3>temps estimé: "+Tempsrestant+"</h3></html>");
			 barprogression[0].setValue((int) ((100*compteurd)*1./tailletotal));
			 barprogression[0].setString(barprogression[0].getValue()+"%");
			 if(ff){
				// long debutE = System.currentTimeMillis();
				 System.out.println("envoi des  "+n+ " fichiers==:: num fichier courant:"+(j+1));
				 String debut=null;
				 if(j==0)  debut="DEBUT";
				 else if(j<n-1) debut="EN_COURS";
				 else debut="FIN";
				 //String hos[]={host};
				 try{
					
				 envoiFichier(fichiers.get(j), host,101,debut,new File(dossier).getName(), listedossier[j]) ;
				
				
				// barprogression[0].setString(barprogression[0].getValue()+"%");
				 labelprogresbas[0].setText("<html><font color=red>dossier:"+new File(dossier).getName()+"    taille :"+taillepro(tailletotal)+"     <u> evolution:</u> \t<br>taille envoyée:"+taillepro(compteurd)+"</font></html>");
					
				 }catch(Exception e){
					 controle.clear();//pour pouvoir continuer l'envoi en cas d'erreur
				 }
				System.out.println("envoi du fichier numero"+(j+1));
				 j++;
				//System.out.println("envoi du fichier numero"+n+":fichier courant:"+j+);
				 if(j>=n-1) System.out.println("envoi du dernier element du dossier"+dossier);
				
			 }else
				
				 System.out.println("un fichier est en cours d'envoi!!!!");
			 }
		
		 barprogression[0].setValue((int) ((100*compteurd)*1./tailletotal));
	     JOptionPane.showMessageDialog(null, "<html><strong>FIN D'enVOI du dossier</strong>:<h1><font color=red>"+dossier+"<br>à :"+host+"</font></h1></html>");
	     barprogression[0].setValue(0); 
	     barprogression[0].setString(barprogression[0].getValue()+"%");
	     labelprogresbas[0].setText("");
	     System.out.println("debit moyen:"+debitMoyen( listeDebit)+  "Mbts");
	     debitlabel.setText("");//<html>"+debit +"<br>"+"</html>"  );
	        tempslabel.setText("");
	        compteurd=0;
		return true;
		
		} catch(Exception e){
			 labelprogresbas[0].setText("une erreur s'est produite!!");
			 return false;
		 }
		 
		 
	 }

	private static float debitMoyen(Vector<Float> _) {
		// TODO Auto-generated method stub
		int n=_.size();
		float s=0;
		int j=0;while(j<n){s+=_.get(j);j++; System.out.println(s);}
		return ((float)(s*1./n));
	}

	private static String taillepro(long compteur) {
		String res = null;
		// TODO Auto-generated method stub
		if(compteur<1024)  res= (compteur+ "\t Octets");
		else if(compteur<1048576)  res= ((float)(compteur*1./(1024))+ "\t KO");
		else if(compteur<1073741824)  res= ((float)(compteur*1./(1048576))+ "\t MO");
		else
			res= ((float)(compteur*1./(1073741824))+ "\t GO");
		return res;
		
	}

	private static long tailleDossier(Vector<String> f,int offset) {
		// TODO Auto-generated method stub
		long x=0;
		for(int j=offset;j<f.size();j++) x+=new File(f.get(j)).length();
		return x;
	}

	private static Vector<String> lister(String dossier) {
	// TODO Auto-generated method stub
		Vector<String> fichiers=new Vector<String>();
		    lister(dossier,fichiers);
	return fichiers;
}

	

	private static String[] retournerBase(Vector<String> fichiers,String dossier) {
	// TODO Auto-generated method stub
		int n=fichiers.size();
		String chaine[]=new String[n];
		
		for(int j=0;j<n;j++){
			  String s=new File(fichiers.get(j)).getParent();
			    if(s.endsWith(dossier)) chaine[j]=dossier;
			      else
			       {  boolean test=false;
			           String val="";
				      StringTokenizer tt=new StringTokenizer(s,"\\");
				      while(tt.hasMoreTokens()){
				       String p=tt.nextToken();
				       if(p.compareToIgnoreCase(dossier)==0) test=true;
				       if(test) val+=p+File.separator;
				      
				      }
				      chaine[j]=val.substring(0, val.length()-1);
			        }
			
			//System.out.println(chaine[j]);
			
			
			    }
		
	return chaine;
}
	private static void lister(String dossier,Vector<String> lisfichier){
		File f=new File(dossier);
		     File[] ll=f.listFiles();
		       for(int j=0;j<ll.length;j++){
		    	      if(ll[j].isFile()) lisfichier.add(ll[j].getAbsolutePath());
		    	      else{
		    	    	   if(ll[j].canRead()) lister(ll[j].getAbsolutePath(),lisfichier);
		    	      }
		    	    	  
		           }
		     
	}

	
	
	
	
public static Vector<String> listeHoteLocal() throws IOException	{
	Vector<String> l=new Vector<String>();
	Process pb = Runtime.getRuntime().exec("net view");//net view :command dos pour recuperer les ordi du reseau local
	
			BufferedReader in = new BufferedReader(new InputStreamReader(pb
					
					.getInputStream()));

			

			
			String s;
			
			while (!procDone(pb)) {
				while ((s = in.readLine()) != null) {
					if(s.startsWith("\\\\")){
						StringTokenizer tok=new StringTokenizer(s );
						while(tok.hasMoreTokens()){s=tok.nextToken();s=s.substring(s.lastIndexOf("\\")+1);
						l.add(s);
						System.out.println(s);break;
						}
						
					}
					
				}
			}
			
			
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		
	return l;
	
}
	
private static boolean procDone(Process p) {
	try {
		int v = p.exitValue();
		return true;
	} catch (IllegalThreadStateException e) {
		return false;
	}
	
	
  
}
}
