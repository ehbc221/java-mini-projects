package jeux.ihm.panneaux;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import jeux.*;
import jeux.cartes.*;
import jeux.enumerations.*;
import jeux.ihm.etiquettes.*;
import jeux.mains.*;
/**
 *
 */
public class Tapis extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String ch_v=Lettre.ch_v;
	private static final String p2=ch_v+Lettre.p2;
	private static final char st=Lettre.st;
	/**sens de distribution des cartes*/
	private Sens sens;
	public Tapis(Jeu j,int nombreDeJoueurs,Vector<String> infos,Vector<String> pseudos)
	{
		this(j,nombreDeJoueurs,infos,pseudos,0);
	}
	/**Cree un tapis de cartes pour un jeu non solitaire*/
	public Tapis(Jeu j,int nombreDeJoueurs,Vector<String> infos,Vector<String> pseudos,int nombre)//infos sert pour le sens(horaire ou antihoraire)
	{
		setLayout(new GridLayout(0,3));
		sens=Sens.valueOf(infos.get(2).split(p2)[1]);
		if(nombreDeJoueurs==4)
			for(int i=0;i<9;i++)
			{
				JPanel sur_panneau=new JPanel();
				sur_panneau.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
				if(i%2==1)
				{
					JPanel panneau=new JPanel();
					panneau.setLayout(new BorderLayout());
					JTextArea jta=new JTextArea(ch_v);
					jta.setRows(3);
					jta.setEditable(false);
					jta.setFocusable(false);
					if(i==1)
					{
						jta.append(pseudos.get(2)+st);
					}
					else if(i==3)
					{
						if(sens==Sens.Horaire)
							jta.append(pseudos.get(1)+st);
						else
							jta.append(pseudos.get(3)+st);
					}
					else if(i==5)
					{
						if(sens==Sens.Horaire)
							jta.append(pseudos.get(3)+st);
						else
							jta.append(pseudos.get(1)+st);
					}
					else
					{
						jta.append(pseudos.get(0)+st);
					}
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==4&&j==Jeu.Tarot)
				{
					sur_panneau.setPreferredSize(new Dimension(100+25*(nombre-1),150));
				}
				sur_panneau.setBackground(new Color(0,125,0));
				add(sur_panneau);
			}
		else if(nombreDeJoueurs==3)
			for(int i=0;i<9;i++)
			{
				JPanel sur_panneau=new JPanel();
				sur_panneau.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
				JPanel panneau=new JPanel();
				panneau.setLayout(new BorderLayout());
				JTextArea jta=new JTextArea(ch_v);
				jta.setRows(3);
				jta.setEditable(false);
				jta.setFocusable(false);
				if(i==0)
				{
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(1)+st);
					else
						jta.append(pseudos.get(2)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==2)
				{
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(2)+st);
					else
						jta.append(pseudos.get(1)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==7)
				{
					jta.append(pseudos.get(0)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==4)
				{
					sur_panneau.setPreferredSize(new Dimension(100+25*(nombre-1),150));
				}
				sur_panneau.setBackground(new Color(0,125,0));
				add(sur_panneau);
			}
		else
			for(int i=0;i<9;i++)
			{
				JPanel sur_panneau=new JPanel();
				JPanel panneau=new JPanel();
				panneau.setLayout(new BorderLayout());
				JTextArea jta=new JTextArea(ch_v);
				jta.setRows(3);
				jta.setEditable(false);
				jta.setFocusable(false);
				if(i==0)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(2)+st);
					else
						jta.append(pseudos.get(3)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==2)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(3)+st);
					else
						jta.append(pseudos.get(2)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==3)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(1)+st);
					else
						jta.append(pseudos.get(4)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==5)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
					if(sens==Sens.Horaire)
						jta.append(pseudos.get(4)+st);
					else
						jta.append(pseudos.get(1)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==7)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
					jta.append(pseudos.get(0)+st);
					JScrollPane ascenseur=new JScrollPane(jta);
					ascenseur.setPreferredSize(new Dimension(100,50));
					panneau.add(ascenseur,BorderLayout.NORTH);
					JLabel carte=new Place(j,JLabel.RIGHT,false);
					carte.setPreferredSize(new Dimension(100,150));
					panneau.add(carte,BorderLayout.CENTER);
					sur_panneau.add(panneau);
				}
				else if(i==4)
				{
					sur_panneau.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
					sur_panneau.setPreferredSize(new Dimension(100+25*(nombre-1),150));
				}
				sur_panneau.setBackground(new Color(0,125,0));
				add(sur_panneau);
			}
	}

	public void mettreAjourTexteDansPanneau(String s,byte joueur,byte nombreDeJoueurs)
	{
		setTexte(numero_panneau(joueur, nombreDeJoueurs),s);
	}
	public void ajouterTexteDansPanneau(String s,byte joueur,byte nombreDeJoueurs)
	{
		ajouterTexte(numero_panneau(joueur, nombreDeJoueurs),s);
	}
	private void ajouterTexte(int position,String s)
	{
		((JTextArea)((JScrollPane)((JPanel)((JPanel)getComponent(position)).getComponent(0)).getComponent(0)).getViewport().getComponent(0)).append(s+st);
	}
	private void setTexte(int position,String s)
	{
		((JTextArea)((JScrollPane)((JPanel)((JPanel)getComponent(position)).getComponent(0)).getComponent(0)).getViewport().getComponent(0)).setText(s);
	}
	public void setTalon(Main m,Jeu j)
	{
		if(j==Jeu.Belote)
		{
			CarteGraphique cg=new Place(m.carte(0),JLabel.RIGHT,false);
			cg.setPreferredSize(new Dimension(100,150));
			((JPanel)getComponent(4)).add(cg);
		}
		else
		{
			((JPanel)getComponent(4)).setBackground(new Color(0,125,0));
			for(byte b=0;b<m.total();b++)
			{
				JLabel cg=new Place(Jeu.Tarot,JLabel.RIGHT,b!=0);
				if(b==0)
					cg.setPreferredSize(new Dimension(100,150));
				else
					cg.setPreferredSize(new Dimension(25,150));
				((JPanel)getComponent(4)).add(cg);
			}
		}
	}
	public void retirerCartes()
	{
		((JPanel)getComponent(4)).removeAll();
		((JPanel)getComponent(4)).repaint();
	}
	public void setEcart(Main m,Jeu j)
	{
		((JPanel)getComponent(4)).removeAll();
		setTalon(m,j);
	}
	public void supprimerCarteTalon()
	{
		((JPanel)getComponent(4)).remove(0);
		((JPanel)getComponent(4)).repaint();
	}
	/**A une place donnee dans le tapis on montre le dos d'une carte*/
	private void setCarte(int numero,Jeu j)
	{
		Place place=(Place)((JPanel)((JPanel)getComponent(numero)).getComponent(0)).getComponent(1);
		place.setJeu(j);
		place.setCarte(null);
		place.repaint();
	}
	/**A une place donnee dans le tapis on montre la face d'une carte*/
	private void setCarte(int numero,Carte m)
	{
		Place place=(Place)((JPanel)((JPanel)getComponent(numero)).getComponent(0)).getComponent(1);
		place.setCarte(m);
		place.setJeu(null);
		place.repaint();
	}
	/**Place les dos des cartes (une pour chaque joueur) sur le tapis avant et apres chaque pli*/
	public void setCartes(byte nombreDeJoueurs,Jeu j)
	{
		for(byte joueur=0;joueur<nombreDeJoueurs;joueur++)
		{
			setCarte(numero_panneau(joueur, nombreDeJoueurs),j);
		}
	}
	/**Met a jour la carte a jouer d'un joueur
	 * donne en fonction du nombre de joueurs*/
	public void setCarte(byte joueur,byte nombreDeJoueurs,Carte m)
	{
		setCarte(numero_panneau(joueur, nombreDeJoueurs),m);
	}
	private byte numero_panneau(byte joueur,byte nombreDeJoueurs)
	{
		if(joueur==0)
		{
			return 7;
		}
		if(sens!=Sens.Horaire)
		{
			joueur=(byte)(nombreDeJoueurs-joueur);
		}
		if(joueur==1)
		{
			if(nombreDeJoueurs<4)
			{
				return 0;
			}
			return 3;
		}
		if(joueur==2)
		{
			if(nombreDeJoueurs<4)
			{
				return 2;
			}
			if(nombreDeJoueurs<5)
			{
				return 1;
			}
			return 0;
		}
		if(joueur==3)
		{
			if(nombreDeJoueurs<5)
			{
				return 5;
			}
			return 2;
		}
		return 5;
	}
}