package Bill.partageFichier;


import java.awt.Desktop;
import java.io.*;
import java.net.Socket;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;

// Referenced classes of package TCP:
//            ThreadEnvoi

public class Traitement extends Thread
{private  Socket soc;
private OutputStream ecriture;
  JLabel chaine=null;
  JProgressBar pros=null;
private BufferedInputStream lecture;
private String extension;
private String nomFichier;
private String dossierenregistrement=null;
Hashtable<String, String> listeDossierEncours=null;
Vector<String> listereception=null;long taillefichier = 0;
    Traitement(Socket soc,Hashtable<String, String> listeDossierEncours,JLabel  chaine,JProgressBar pros,Vector<String> listereception)
        throws IOException
    {
        this.listereception=listereception;
       this.pros=pros;
        this.soc = soc;
        lecture = new BufferedInputStream(this.soc.getInputStream());
        ecriture = soc.getOutputStream();
        this.listeDossierEncours=listeDossierEncours;
       this.chaine=chaine;
        start();
    }

    @SuppressWarnings("deprecation")
	public void run()
    {
        try{
        byte loextension[] = new byte[1000];
        try
        {
            lecture.read(loextension);
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
        
        //selection du dossier d'enregistrement
        String etat=null;
		extension = (new String(loextension, 0, loextension.length)).trim();
       // System.out.println(extension);
		if(extension.lastIndexOf(":")<2) {yield();stop();}
        StringTokenizer tt = new StringTokenizer(extension,":");
        if(extension.startsWith("DOS_:"))
        {
        	tt.nextToken();
        	 etat=tt.nextToken();
        	if(etat.compareToIgnoreCase("DEBUT")==0){
        		//FileSystemView fsv = FileSystemView.getFileSystemView();
        		try{
        		JFileChooser fileChooserSaveImage = new JFileChooser(FileSystemView.getFileSystemView().getRoots()[0]);
        		fileChooserSaveImage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//pour afficher que les dossiers
        		int retour = fileChooserSaveImage.showSaveDialog(null);
        		if (retour == JFileChooser.APPROVE_OPTION) {
        				File url = fileChooserSaveImage.getSelectedFile();
        				
        				String dossier=tt.nextToken();
        				
        				
        				 dossierenregistrement=url.getAbsolutePath()+File.separator;
        				 listeDossierEncours.put(dossier,  dossierenregistrement);
        						String Sous =tt.nextToken();
        						Sous+=File.separator;
        						nomFichier=Sous;	 
        			
        						
        						 
        				 
        		}
        		}catch(Exception e){//
        			try {
						soc.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				yield();stop();
				}
        	}else if(etat.compareToIgnoreCase("EN_COURS")==0){
        		dossierenregistrement= this.listeDossierEncours.get(tt.nextToken());
        		nomFichier=
        		 tt.nextToken();
        	}else if(etat.compareToIgnoreCase("FIN")==0){
        		
        	String dossier=tt.nextToken();
        	
        	dossierenregistrement=this.listeDossierEncours.get(dossier);
        		
        		listeDossierEncours.remove(dossier);
        		nomFichier=tt.nextToken();
        	}
       
        String fi=tt.nextToken();
        
        taillefichier = Integer.parseInt(tt.nextToken());
        String doc=dossierenregistrement+nomFichier;
        
        File file=new File(doc);
        if(!file.exists()){
        	String hh=dossierenregistrement;
        	 tt=new StringTokenizer(nomFichier,"\\");
        	 while(tt.hasMoreTokens()){
        		 hh+=tt.nextToken();
        		if(! new File(hh).exists()) new File(hh).mkdir();
        		 hh+=File.separator;
        		 System.out.println(hh);
        	 }
        }
        nomFichier=dossierenregistrement+nomFichier+File.separator+fi;
       // System.out.println(this.nomFichier);
        
        	}    	
        else{
        	FileSystemView fsv = FileSystemView.getFileSystemView();
    		JFileChooser fileChooserSaveImage = new JFileChooser(fsv.getRoots()[0]);
    		fileChooserSaveImage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    		int retour = fileChooserSaveImage.showSaveDialog(null);
    		if (retour == JFileChooser.APPROVE_OPTION) {
    				File url = fileChooserSaveImage.getSelectedFile();
    				if(url==null)
						try {
							soc.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				String f=null;
    				if(!extension.startsWith("F_:"))
    				f=tt.nextToken();//pour la version anterieure
    				else{
    					tt.nextToken();
    					f=tt.nextToken();
    				}
    				
    				  dossierenregistrement=url.getAbsolutePath()+File.separator;
    				  nomFichier=dossierenregistrement+f;
    				 
    				 taillefichier = Integer.parseInt(tt.nextToken());
    		}
        }
       
        
   
        listereception.add(new File(nomFichier).getName());
       try {
      FileOutputStream outBuffer = null;
      
        	outBuffer= new FileOutputStream(nomFichier);
 	      
            byte data[] = new byte[64000];
        	
         
        	
        	  int tail=123;
			ecriture.write((int) tail);//pour signaler qu'on est pret à recevoir
         
           long compteur=0;
            int taille=0;
           
            
            while(((taille = lecture.read(data)) != -1)) {
            	
            	compteur+=taille;
            	
                outBuffer.write(data, 0, taille);
               pros.setValue((int) ((100*compteur)/((taillefichier!=0)?taillefichier:1)));
               pros.setString(pros.getValue()+"%");
            }
            outBuffer.write(taille);
          
            outBuffer.close();
             chaine.setText(chaine.getText()+(new StringBuilder("<br><h1>NOM FICHIER  Rec\347u:")).append(nomFichier).append("<br>Expediteur:").append(soc.getInetAddress()).append(" <br>Date de reception:").append((new Date()).toGMTString()).append("</h1>").toString());
           
           
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, ("<html>cette erreur s'est produite:<br>"+e+"<br>appuyer <h1>OK</h1> pour  continuer"));
            new File(nomFichier).delete();
            try {
				this.soc.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        if(!extension.startsWith("DOS_:")){
        JOptionPane.showMessageDialog(null, (new StringBuilder("<html><h3>reception termin\351e</h3><br><h3>nom du fichier :</h3><h2>")).append((new File(nomFichier)).getAbsolutePath()).append("</h2></html>").toString());
       
        int y = JOptionPane.showConfirmDialog(null, (new StringBuilder("voulez vous ouvrir le fichier:")).append((new File(nomFichier)).getName()).append("?").toString());
        if(y == 0 && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN))
            try
            {
                Desktop.getDesktop().open(new File(nomFichier));// pour l'ouvrir
            }
            catch(IOException ioexception) { }
        	
        }else if(etat.compareToIgnoreCase("FIN")==0){
        	int y = JOptionPane.showConfirmDialog(null, (new StringBuilder("voulez vous ouvrir la dossier d'enregistrement:")).append((new File(nomFichier).getParent())));
            if(y == 0 && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN))
                try
                {
                    Desktop.getDesktop().open(new File( dossierenregistrement));// pour l'ouvrir
                }
                catch(IOException ioexception) { }
            	
        }
       // pros.setValue(0);
        yield() ;
        }catch(Exception e){
        	try {
				this.soc.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        	}
        
       
		
        
    }

   
   

