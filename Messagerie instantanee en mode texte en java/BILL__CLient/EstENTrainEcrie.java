import java.io.PrintWriter;

import javax.swing.JTextField;


public class EstENTrainEcrie  extends Thread{
	/**
	 * @author  Bile Bernard
	 */
	JTextField  saisie=null;
	String expediteur=null ,destinataire=null;
	boolean dejasignale=false;
	PrintWriter ecriture=null;
	public EstENTrainEcrie(JTextField  saisie,String expediteur,String destinataire,PrintWriter ecriture){
		this.destinataire=destinataire;
		this.expediteur=expediteur;
		this.ecriture=ecriture;
		this.  saisie=  saisie;
		start();
	}
	public void run(){
		while(true){
			if( saisie.getText().compareToIgnoreCase("")!=0 && !dejasignale){
				String mes=expediteur+":"+destinataire+":800";
					//CriptageB.CripterChaine(expediteur+":"+destinataire+":800",0);
				ecriture.println(mes);
				dejasignale=true;
				
			}
			else if(saisie.getText().compareToIgnoreCase("")==0 ){
				String mes=expediteur+":"+destinataire+":900";
				//CriptageB.CripterChaine(expediteur+":"+destinataire+":800",0);
			ecriture.println(mes);
				dejasignale=false;
			}
		}
	}
}
