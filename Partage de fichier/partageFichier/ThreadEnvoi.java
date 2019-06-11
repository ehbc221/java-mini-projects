package Bill.partageFichier;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

class ThreadEnvoi  extends Thread{
		String fich=null,add[]=null;
		ThreadEnvoi (String fich,String add[]){
			this.fich=fich;this.add=add;
			start();
		}
		@SuppressWarnings("deprecation")
		public void run(){
			try {
				if(new File(fich).isDirectory()){
					//JOptionPane.showMessageDialog(null,"<html><h1><font color=blue>ce fichier est un <br>dossier !!</font></h1></html>");
					int y = JOptionPane.showConfirmDialog(null, "voulez-vous envoyez le dossier::"+new File(fich).getName()+"?","Confirmation",JOptionPane.YES_NO_OPTION);
					if(y==0)ENVOIFICHIER.envoiDossier(fich, add);
					    
					yield();
					stop();
				}
				int y = JOptionPane.showConfirmDialog(null, "voulez-vous envoyez le fichier::"+new File(fich).getName()+"?","Confirmation",JOptionPane.YES_NO_OPTION);
				
				if(y==0)ENVOIFICHIER. envoiFichier(fich, add,100," "," "," ");//
				yield();
				stop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
			
		}
		}
	
	