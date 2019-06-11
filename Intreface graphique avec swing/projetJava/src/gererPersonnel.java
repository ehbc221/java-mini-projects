



import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

 class gererPersonnel extends JPanel  implements ActionListener
{base b= new base();
 JLabel titr,noml,prenoml,dated,datef,idl; 
 
 
 
 JTextField cod,nom, prenom,id;
 
 
 JButton ajouter;
 JButton annuler;
 JComboBox jour, mois, annee,jour1, mois1, annee1;
 public gererPersonnel() 
 	{
 	
 idl=new JLabel("identificateur");
 	titr= new JLabel("espace personnel");
 	noml=new JLabel("nom");
 	prenoml=new JLabel("prenom");
 	dated=new JLabel("date de naisance");
 	datef=new JLabel("date d'embauche");
 	nom= new JTextField();
 	id= new JTextField();
 	prenom= new JTextField();
 	
 	JPanel p1=new JPanel();
 	JPanel p2=new JPanel();
 	JPanel p3=new JPanel();
 	ajouter= new JButton("ajouter ");
 	annuler= new JButton("annuler ");
 	Font titreFont=new Font("Arial",2, 20);
 	Font titre=new Font("Arial",1, 30);
 	jour = new JComboBox();
    mois = new JComboBox();
    annee = new JComboBox();
    jour1 = new JComboBox();
    mois1 = new JComboBox();
    annee1 = new JComboBox();
    int i = 0;
    for (i = 1; i <= 31; i++) {
      jour.addItem("" + i);
      jour1.addItem("" + i);
    }
    jour.setSelectedIndex(0); //definir l'element selectionné par defaut (le 1er element)

    //initialisation de la liste des mois
    for (i = 1; i <= 12; i++) {
      mois.addItem("" + i);
      mois1.addItem("" + i);
    }
    mois.setSelectedIndex(0); //definir l'element selectionné par defaut (le 1er element)
    mois1.setSelectedIndex(0);
    //initialisation de la liste des annees
    for (i = 1960; i <= 2010; i++) {
      annee.addItem("" + i);
      annee1.addItem( (i+40)+"");
    }
    annee.setSelectedIndex(0);
    annee1.setSelectedIndex(0);
 	idl.setFont(titreFont);
 	 noml.setFont(titreFont);
 	 datef.setFont(titreFont);
 	 prenoml.setFont(titreFont);
 	  // nom.setFont(titreFont);
 	   titr.setFont(titre);
 	    dated.setFont(titreFont);
 	   ajouter.setFont(titreFont);
 	  annuler.setFont(titreFont);
 	 

 	  titr.setForeground(Color.magenta);
 	p1.add(titr);
 	p2.setLayout(new GridLayout(3,2,10,10));
 p2.add(idl);
 p2.add(id);
 	p2.add(noml);
 	p2.add(nom);
 	p2.add(prenoml);
 	p2.add(prenom);
 
 	
 	JPanel p6= new JPanel();
 	p6.add(jour);
 	p6.add(mois);
 	p6.add(annee);	
 	JPanel p8= new JPanel();
 	p8.add(jour1);
 	p8.add(mois1);
 	p8.add(annee1);
 	JPanel p7= new JPanel();
 	p7.setLayout(new GridLayout(2,1,5,5));
 	p7.add(dated);
 	p7.add(p6);
 	JPanel p5= new JPanel();
 	p5.setLayout(new GridLayout(2,1,5,5));
 	p5.add(datef);
 	p5.add(p8);
 //	p3.setLayout(new GridLayout(1,2,5,5));
 	p3.add(ajouter);
 	p3.add(annuler);
 	
 
 	JPanel p4= new JPanel();
 	p4.setLayout(new GridLayout(5,1,10,10));
 	p4.add(p1);
 	p4.add(p2);
 	p4.add(p7);
 	p4.add(p5);
 	p4.add(p3);
 	 
this.add(p4);
   this.show(); 
   	this.setName("Gestion congé");
     this.setSize(600,700);
 	ajouter.addActionListener(this);
 	annuler.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) 
    { if(e.getSource()==ajouter)
    	try{
    
    { String j="";
 String m="";
 String a="";
 j=(String)jour.getSelectedItem();  // 
 m=(String) mois.getSelectedItem();//
 a=(String)annee.getSelectedItem(); 
 	String v=j+"/"+m+"/"+a;
 	String f=jour1.getSelectedItem()+"/"+mois1.getSelectedItem()+"/"+annee1.getSelectedItem();
 	
b.ajout_personnel(Integer.parseInt(id.getText()),nom.getText()+" "+prenom.getText(),v,f);

nom.setText("");
prenom.setText("");
id.setText("");
    }
    	}
    	catch (Exception c){JOptionPane.showMessageDialog(null,c.getMessage());
    	}
    if(e.getSource()==annuler)
    {
    
  nom.setText("");
  prenom.setText("");
id.setText("");
    	
    }	
    	
    }
    	/*public static void main(String[] args) {
deposer_conge b=new deposer_conge();
}*/
 	
    
    
}
