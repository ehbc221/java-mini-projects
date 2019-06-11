package jeux.ihm.dialogues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jeux.cartes.*;
import jeux.ihm.*;
import jeux.ihm.etiquettes.*;
import jeux.mains.*;
import jeux.parties.*;
/**
 *
 */
public class DialoguePoignees extends Dialogue {
	private static final long serialVersionUID = 1L;
	private MainTarot poignee=new MainTarot();
	private MainTarot cartesExclues=new MainTarot();
	private byte maxPoignee;
	private static final String conseil="Conseil";
	private static final String valider="Valider";
	private static final String annuler="Annuler";
	public DialoguePoignees(Fenetre fenetre,String titre,MainTarot atouts,byte max)
	{
		super(titre,fenetre,true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		poignee=atouts;
		maxPoignee=max;
	}
	public void setDialogue()
	{
		Container container=new Container();
		container.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		panneau.setLayout(new GridLayout(0,1));
		panneau.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
		JPanel sous_panneau=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		for(byte indice=0;indice<poignee.total();indice++)
		{
			CarteGraphique carte=new CarteGraphique(poignee.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
				carte.setPreferredSize(new Dimension(100,150));
			else
				carte.setPreferredSize(new Dimension(25,150));
			carte.addMouseListener(new EcouteurPoignee(carte.getCarte()));
			sous_panneau.add(carte);
		}
		panneau.add(sous_panneau);
		container.add(panneau,BorderLayout.CENTER);
		panneau=new JPanel();
		JButton bouton=new JButton(conseil);
		bouton.addActionListener(new EcouteurBouton(conseil));
		panneau.add(bouton);
		bouton=new JButton(valider);
		bouton.addActionListener(new EcouteurBouton(valider));
		panneau.add(bouton);
		bouton=new JButton(annuler);
		bouton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		panneau.add(bouton);
		container.add(panneau,BorderLayout.SOUTH);
		setContentPane(container);
		pack();
	}
	private void ajouterUneCarteDansPoignee(Carte carte)
	{
		poignee.ajouter(carte);
		cartesExclues.jouer(carte);
		changerCartes();
		pack();
	}
	private void changerCartes()
	{
		JPanel sous_panneau=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		for(byte indice=0;indice<poignee.total();indice++)
		{
			CarteGraphique carte=new CarteGraphique(poignee.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
				carte.setPreferredSize(new Dimension(100,150));
			else
				carte.setPreferredSize(new Dimension(25,150));
			carte.addMouseListener(new EcouteurPoignee(carte.getCarte()));
			sous_panneau.add(carte);
		}
		((JPanel)getContentPane().getComponent(0)).remove(1);
		((JPanel)getContentPane().getComponent(0)).add(sous_panneau,1);
		sous_panneau=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		for(byte indice=0;indice<cartesExclues.total();indice++)
		{
			CarteGraphique carte=new CarteGraphique(cartesExclues.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
				carte.setPreferredSize(new Dimension(100,150));
			else
				carte.setPreferredSize(new Dimension(25,150));
			carte.addMouseListener(new EcouteurPoignee(carte.getCarte()));
			sous_panneau.add(carte);
		}
		((JPanel)getContentPane().getComponent(0)).remove(0);
		((JPanel)getContentPane().getComponent(0)).add(sous_panneau,0);
	}
	private void retirerUneCarteDansPoignee(Carte carte)
	{
		poignee.jouer(carte);
		cartesExclues.ajouter(carte);
		changerCartes();
		pack();
	}
	private void conseil()
	{
		String[] raison = new String[1];
		JOptionPane.showMessageDialog(this,"Je conseille la poignee "+((PartieTarot)((Fenetre)getOwner()).getPartie()).strategiePoignee((byte)0,raison),"Conseil de poignee",JOptionPane.INFORMATION_MESSAGE);
	}
	private void valider()
	{
		if(poignee.total()==maxPoignee&&(!poignee.contient(new CarteTarot((byte)0))||cartesExclues.estVide()))
			dispose();
		else if(poignee.total()>maxPoignee)
			JOptionPane.showMessageDialog(this,"Il y a "+(poignee.total()-maxPoignee)+" atouts en trop.","Trop d'atouts",JOptionPane.ERROR_MESSAGE);
		else if(poignee.total()<maxPoignee)
			JOptionPane.showMessageDialog(this,"Il manque "+(maxPoignee-poignee.total())+" atouts.","Manque d'atouts",JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showMessageDialog(this,"L'excuse ne peut pas faire partie d'une poignee sauf pour completer une poignee","Mauvais choix de cartes",JOptionPane.ERROR_MESSAGE);
	}
	public MainTarot getPoignee()
	{
		setVisible(true);
		return poignee;
	}
	private class EcouteurBouton implements ActionListener
	{
		private String choix;
		EcouteurBouton(String pchoix)
		{
			choix=pchoix;
		}
		public void actionPerformed(ActionEvent e) {
			if(choix.equals(conseil))
				conseil();
			else
				valider();
		}
	}
	private class EcouteurPoignee extends MouseAdapter {
		private Carte carte;
		EcouteurPoignee(Carte pcarte)
		{
			carte=pcarte;
		}
		public void mousePressed(MouseEvent e)
		{
			if(poignee.contient(carte))
				retirerUneCarteDansPoignee(carte);
			else
				ajouterUneCarteDansPoignee(carte);
		}
	}
}