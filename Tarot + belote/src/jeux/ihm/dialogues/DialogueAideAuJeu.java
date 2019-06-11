package jeux.ihm.dialogues;
import javax.swing.*;
import jeux.*;
import jeux.cartes.*;
import jeux.encheres.*;
import jeux.enumerations.*;
import jeux.ihm.*;
import jeux.mains.*;
import java.awt.*;
import java.util.*;
/**
 *
 */
public class DialogueAideAuJeu extends JDialog
{private static final long serialVersionUID = 1L;
	private static final String ch_v=Lettre.ch_v;
	private static final char P=Lettre.P;
	private static final char C=Lettre.C;
	private static final char J=Lettre.J;
	private static final char st=Lettre.st;
	private static final char es=Lettre.es;
	private static final char tb=Lettre.tb;
	public DialogueAideAuJeu(String titre,Fenetre fenetre,boolean mod)
	{
		super(fenetre,titre,mod);
		setLocationRelativeTo(fenetre);
	}
	/**Cartes possibles et certaines &#224 la belote et au tarot*/
	public void setDialogue(Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,Vector<MainTarot> repartition_jouees,Vector<String> pseudos)
	{
		Container c=new Container();
		c.setLayout(new FlowLayout());
		JPanel panneau2=new JPanel();
		JPanel panneau3;
		JTextArea zone;
		panneau2.setLayout(new BorderLayout());
		Main tout=new MainTarot();
		for(byte carte=0;carte<78;carte++)
		{
			tout.ajouter(new CarteTarot(carte));
		}
		panneau3=new JPanel();
		byte couleur;
		byte couleur_memo=-1;
		for(int indice_pseudo=1;indice_pseudo<pseudos.size()+1;indice_pseudo++)
		{
			zone=new JTextArea(ch_v,84,15);
			zone.setRows(84);
			zone.setEditable(false);
			if(indice_pseudo<pseudos.size())
			{
				zone.append(pseudos.get(indice_pseudo)+st);
			}
			for(Carte carte:tout)
			{
				couleur=carte.couleur();
				if(couleur!=couleur_memo&&couleur>0)
				{
					zone.append(Couleur.values()[couleur-1].toString()+st);
				}
				zone.append(tb+ch_v);
				if(couleur==0)
				{
					zone.append(carte.toString()+es);
				}
				else
				{
					zone.append(carte.toString().split(es+ch_v)[0]+es);
				}
				if(cartes_possibles.get(couleur).get(indice_pseudo).contient(carte))
				{
					zone.append(P+ch_v);
				}
				if(cartes_certaines.get(couleur).get(indice_pseudo).contient(carte))
				{
					zone.append(C+ch_v);
				}
				if(repartition_jouees.get(couleur).contient(carte))
				{
					zone.append(J+ch_v);
				}
				zone.append(st+ch_v);
				couleur_memo=carte.couleur();
			}
			panneau3.add(zone);
		}
		panneau2.add(panneau3,BorderLayout.CENTER);
		JScrollPane ascenseur=new JScrollPane(panneau2);
		ascenseur.setPreferredSize(new Dimension(600,600));
		c.add(ascenseur);
		setContentPane(c);
		pack();
	}
	public void voir()
	{
		setResizable(false);
		setVisible(true);
	}
	public void setDialogue2(Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines,Vector<MainBelote> repartition_jouees,byte couleur_demandee,byte couleur_atout,Contrat contrat,Vector<String> pseudos)
	{
		Container c=new Container();
		c.setLayout(new FlowLayout());
		JPanel panneau2=new JPanel();
		JPanel panneau3;
		JTextArea zone;
		panneau2.setLayout(new BorderLayout());
		Main tout=new MainBelote();
		for(byte carte=0;carte<32;carte++)
		{
			tout.ajouter(new CarteBelote(carte));
		}
		for(int indice_carte=0;indice_carte<tout.total();indice_carte++)
		{
			for(int indice_carte_2=indice_carte+1;indice_carte_2<tout.total();indice_carte_2++)
			{
				if(((CarteBelote)tout.carte(indice_carte)).force(couleur_atout,couleur_demandee,contrat)<((CarteBelote)tout.carte(indice_carte_2)).force(couleur_atout,couleur_demandee,contrat))
				{
					tout.echanger(indice_carte,indice_carte_2);
				}
			}
		}
		if(couleur_atout>1&&couleur_atout!=couleur_demandee)
		{
			for(int indice_carte=16;indice_carte<tout.total();indice_carte++)
			{
				for(int indice_carte_2=indice_carte+1;indice_carte_2<tout.total();indice_carte_2++)
				{
					if(tout.carte(indice_carte).couleur()>tout.carte(indice_carte_2).couleur())
					{
						tout.echanger(indice_carte,indice_carte_2);
					}
					else if(tout.carte(indice_carte).couleur()==tout.carte(indice_carte_2).couleur())
					{
						byte couleur=tout.carte(indice_carte).couleur();
						if(((CarteBelote)tout.carte(indice_carte)).force(couleur_atout,couleur,contrat)<((CarteBelote)tout.carte(indice_carte_2)).force(couleur_atout,couleur,contrat))
						{
							tout.echanger(indice_carte,indice_carte_2);
						}
					}
				}
			}
		}
		else
		{
			for(int indice_carte=8;indice_carte<tout.total();indice_carte++)
			{
				for(int indice_carte_2=indice_carte+1;indice_carte_2<tout.total();indice_carte_2++)
				{
					if(tout.carte(indice_carte).couleur()>tout.carte(indice_carte_2).couleur())
					{
						tout.echanger(indice_carte,indice_carte_2);
					}
					else if(tout.carte(indice_carte).couleur()==tout.carte(indice_carte_2).couleur())
					{
						byte couleur=tout.carte(indice_carte).couleur();
						if(((CarteBelote)tout.carte(indice_carte)).force(couleur_atout,couleur,contrat)<((CarteBelote)tout.carte(indice_carte_2)).force(couleur_atout,couleur,contrat))
						{
							tout.echanger(indice_carte,indice_carte_2);
						}
					}
				}
			}
		}
		panneau3=new JPanel();
		byte couleur;
		byte couleur_memo=-1;
		for(int indice_pseudo=1;indice_pseudo<pseudos.size();indice_pseudo++)
		{
			zone=new JTextArea(ch_v,37,15);
			zone.setRows(37);
			zone.setEditable(false);
			zone.append(pseudos.get(indice_pseudo)+st);
			for(Carte carte:tout)
			{
				couleur=carte.couleur();
				if(couleur!=couleur_memo)
				{
					zone.append(Couleur.values()[couleur-1].toString()+st);
				}
				zone.append(tb+ch_v);
				zone.append(carte.toString().split(es+ch_v)[0]+es);
				if(cartes_possibles.get(couleur-2).get(indice_pseudo).contient(carte))
				{
					zone.append(P+ch_v);
				}
				if(cartes_certaines.get(couleur-2).get(indice_pseudo).contient(carte))
				{
					zone.append(C+ch_v);
				}
				if(repartition_jouees.get(couleur-2).contient(carte))
				{
					zone.append(J+ch_v);
				}
				zone.append(st+ch_v);
				couleur_memo=carte.couleur();
			}
			panneau3.add(zone);
		}
		panneau2.add(panneau3,BorderLayout.CENTER);
		JScrollPane ascenseur=new JScrollPane(panneau2);
		ascenseur.setPreferredSize(new Dimension(600,600));
		c.add(ascenseur);
		setContentPane(c);
		pack();
	}
}