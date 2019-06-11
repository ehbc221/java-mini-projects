import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class ChoixAtout extends JDialog{
	
	JRadioButton coeur,pique,trefle,carreau;
	JButton ok=new JButton("ok");
	String latout="";
	
	public ChoixAtout(JFrame fenetre,String atout){
		super(fenetre,true);
		latout=atout;
		setSize(200,200);
		coeur=new JRadioButton("Coeur",true);
		pique=new JRadioButton("Pique");
		trefle=new JRadioButton("Trefle");
		carreau=new JRadioButton("Carreau");
		Container contenu = getContentPane();
		Box ligne = Box.createVerticalBox();
		contenu.add(ligne);
		ligne.add(coeur);
		ligne.add(pique);
		ligne.add(trefle);
		ligne.add(carreau);
		ButtonGroup groupe=new ButtonGroup();
		groupe.add(coeur);
		groupe.add(pique);
		groupe.add(trefle);
		groupe.add(carreau);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(coeur.isSelected()) latout="Coeur";
				if(pique.isSelected()) latout="Pique";
				if(trefle.isSelected()) latout="Trefle";
				if(carreau.isSelected()) latout="Carreau";
				setVisible(false);
			}
		});
		ligne.add(ok);
		setVisible(true);	
	}
	
	String getTexte(){
		return latout;
	}

}
