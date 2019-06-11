package jeux.mains;
import java.util.*;
import jeux.cartes.*;
import jeux.encheres.*;
import jeux.enumerations.*;
/**
 *
 */
public class MainBelote extends MainTriable{
	private static final long serialVersionUID = 1L;
	private transient Ordre ordre;
	public MainBelote() {
		ordre=Ordre.Atout;
	}
	public MainBelote(Ordre pordre) {
		this();
		ordre=pordre;
	}
	public MainBelote(Carte[] cartes) {
		super(cartes);
	}
	public byte nombre_plis_assures_min_atout()
	{
		byte nombre_plis=0;
		byte nombre_plis_assures=0;
		byte cartes_manquantes=0;
		byte cartes_possedees=0;
		byte indice_carte=0;
		for(byte valeur:CarteBelote.cartesAtout)
		{
			if(indice_carte<total()&&carte(indice_carte).valeur()==valeur)
			{
				if(nombre_plis>0)
				{
					nombre_plis++;
				}
				cartes_possedees++;
				indice_carte++;
			}
			else
			{
				if(nombre_plis>0)
				{
					nombre_plis_assures+=nombre_plis;
					cartes_manquantes=0;
					cartes_possedees=0;
				}
				nombre_plis=0;
				cartes_manquantes++;
			}
			if(cartes_possedees>cartes_manquantes)
			{
				nombre_plis=(byte)(cartes_possedees-cartes_manquantes);
			}
		}
		return (byte)(nombre_plis_assures+nombre_plis);
	}
	public Vector<MainBelote> eclater(Vector<MainBelote> repartitionCartesJouees,byte couleur_atout)
	{
		if(estVide())
		{
			return new Vector<MainBelote>();
		}
		byte couleur=carte(0).couleur();
		Vector<MainBelote> suites=new Vector<MainBelote>();
		MainBelote union=new MainBelote();
		union.ajouterCartes(repartitionCartesJouees.get(couleur-2));
		for(int i=0;i<total();i++)
		{
			CarteBelote c=(CarteBelote)carte(i);
			if(union.estVide()||((CarteBelote)union.derniereCarte()).force(couleur_atout,couleur)>c.force(couleur_atout,couleur))
			{
				union.ajouter(c);
			}
			else
			{
				int j=0;
				for(;((CarteBelote)union.carte(j)).force(couleur_atout,couleur)>c.force(couleur_atout,couleur);j++);
				union.ajouter(c,j);
			}
		}
		int cte=((CarteBelote)union.carte(0)).force(couleur_atout,couleur);
		for(int i=0;i<union.total();i++)
		{
			if(i+((CarteBelote)union.carte(i)).force(couleur_atout,couleur)==cte)
			{
				if(contient(union.carte(i)))
				{
					if(suites.isEmpty())
					{
						suites.addElement(new MainBelote());
					}
					suites.lastElement().ajouter(union.carte(i));
				}
			}
			else if(contient(union.carte(i)))
			{
				suites.addElement(new MainBelote());
				suites.lastElement().ajouter(union.carte(i));
				cte=i+((CarteBelote)union.carte(i)).force(couleur_atout,couleur);
			}
		}
		return suites;
	}
	public Vector<MainBelote> eclater_atout()
	{
		if(estVide())
		{
			return new Vector<MainBelote>();
		}
		byte couleur=carte(0).couleur();
		Vector<MainBelote> suites=new Vector<MainBelote>();
		MainBelote suite=new MainBelote();
		byte cte=((CarteBelote)carte(0)).force(couleur,couleur);
		for(byte indice_carte=0;indice_carte<total();indice_carte++)
		{
			if(((CarteBelote)carte(indice_carte)).force(couleur,couleur)+indice_carte==cte)
			{
				suite.ajouter(carte(indice_carte));
			}
			else
			{
				suites.addElement(suite);
				suite=new MainBelote();
				cte=(byte)(((CarteBelote)carte(indice_carte)).force(couleur,couleur)+indice_carte);
				suite.ajouter(carte(indice_carte));
			}
		}
		suites.addElement(suite);
		return suites;
	}
	public Vector<MainBelote> eclater_couleur()
	{
		if(estVide())
		{
			return new Vector<MainBelote>();
		}
		byte couleur=carte(0).couleur();
		Vector<MainBelote> suites=new Vector<MainBelote>();
		MainBelote suite=new MainBelote();
		byte couleur_bis=0;
		byte cte=((CarteBelote)carte(0)).force(couleur_bis,couleur);
		for(byte indice_carte=0;indice_carte<total();indice_carte++)
		{
			if(((CarteBelote)carte(indice_carte)).force(couleur_bis,couleur)+indice_carte==cte)
			{
				suite.ajouter(carte(indice_carte));
			}
			else
			{
				suites.addElement(suite);
				suite=new MainBelote();
				cte=(byte)(((CarteBelote)carte(indice_carte)).force(couleur_bis,couleur)+indice_carte);
				suite.ajouter(carte(indice_carte));
			}
		}
		suites.addElement(suite);
		return suites;
	}
	/**Permet de trier les cartes selon les parametres
	 * @param couleurs une chaine de couleurs espacee par des tirets
	 * @param sens croissant ou decroissant
	 * @param ordre "atout" ou "couleur"*/
	public void trier(String couleurs, String sens) {
		if(couleurs.contains(":"))
		couleurs=couleurs.split(":")[1].split(";")[0];
		if(sens.contains(";"))
		sens=sens.split(";")[1];
		for(int i=0;i<total();i++)
			for(int j=i+1;j<total();j++)
				if(((Triable)carte(j)).vientAvant((Triable)carte(i),sens,ordre.toString(),couleurs))
					echanger(i,j);
	}
	public Vector<MainBelote> couleurs(byte couleurAtout,Contrat contrat)
	{
		Vector<MainBelote> main=new Vector<MainBelote>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			MainBelote cartes=new MainBelote();
			for(byte indice_carte=0;indice_carte<total();indice_carte++)
			{
				CarteBelote carte=(CarteBelote)carte(indice_carte);
				if(carte.couleur()==couleur)
				{
					if(cartes.estVide()||carte.force(couleurAtout,couleur,contrat)<((CarteBelote)cartes.derniereCarte()).force(couleurAtout,couleur,contrat))
					{
						cartes.ajouter(carte);
					}
					else
					{
						byte place=0;
						for(;carte.force(couleurAtout,couleur,contrat)<((CarteBelote)cartes.carte(place)).force(couleurAtout,couleur,contrat);place++);
						cartes.ajouter(carte,place);
					}
				}
			}
			main.addElement(cartes);
		}
		return main;
	}
	public Vector<MainBelote> couleurs(Contrat contrat)
	{
		Vector<MainBelote> main=new Vector<MainBelote>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			MainBelote cartes=new MainBelote();
			for(byte indice_carte=0;indice_carte<total();indice_carte++)
			{
				CarteBelote carte=(CarteBelote)carte(indice_carte);
				if(carte.couleur()==couleur)
				{
					if(cartes.estVide()||carte.force(couleur,couleur,contrat)<((CarteBelote)cartes.derniereCarte()).force(couleur,couleur,contrat))
					{
						cartes.ajouter(carte);
					}
					else
					{
						byte place=0;
						for(;carte.force(couleur,couleur,contrat)<((CarteBelote)cartes.carte(place)).force(couleur,couleur,contrat);place++);
						cartes.ajouter(carte,place);
					}
				}
			}
			main.addElement(cartes);
		}
		return main;
	}
	public int maxAtout(byte couleurAtout,byte couleurDemandee)
	{
		int max=0;
		byte force;
		for(int i=0;i<total();i++)
		{
			force=((CarteBelote)carte(i)).force(couleurAtout,couleurDemandee, new Contrat(EncheresBelote.Couleur));
			if(carte(i).couleur()==couleurAtout&&force>max)
			{
				max=force;
			}
		}
		return max;
	}
	public void setOrdre(Ordre pordre)
	{
		ordre=pordre;
	}
	public void trier()
	{
		byte couleur;
		if(ordre==Ordre.Atout)
		{
			for(int indice_carte=0;indice_carte<total();indice_carte++)
			{
				for(int indice_carte_2=indice_carte+1;indice_carte_2<total();indice_carte_2++)
				{
					couleur=carte(indice_carte).couleur();
					if(((CarteBelote)carte(indice_carte)).force(couleur,couleur)<((CarteBelote)carte(indice_carte_2)).force(couleur,couleur))
					{
						echanger(indice_carte,indice_carte_2);
					}
				}
			}
		}
		else
		{
			couleur=0;
			for(int indice_carte=0;indice_carte<total();indice_carte++)
			{
				for(int indice_carte_2=indice_carte+1;indice_carte_2<total();indice_carte_2++)
				{
					byte couleur_carte=carte(indice_carte).couleur();
					if(((CarteBelote)carte(indice_carte)).force(couleur,couleur_carte)<((CarteBelote)carte(indice_carte_2)).force(couleur,couleur_carte))
					{
						echanger(indice_carte,indice_carte_2);
					}
				}
			}
		}
	}
	/**Appelee apres un contrat couleur ou autre couleur*/
	public void trier(String couleurs, String sens,Carte cb)
	{
		ordre=Ordre.Couleur;
		trier(couleurs, sens);
		byte couleur_atout=cb.couleur();
		byte indiceAtout=0;
		if(!estVide())
		{
			byte couleur=carte(0).couleur();
			for(;couleur!=couleur_atout;)
			{
				indiceAtout+=tailleCouleur(couleur);
				if(indiceAtout>=total())
				{
					break;
				}
				couleur=carte(indiceAtout).couleur();
			}
			if(sens.contains(Monotonie.Croissant.toString()))
			{
				indiceAtout+=tailleCouleur(couleur_atout)-1;
			}
			if(contient(new CarteBelote((byte)9,couleur_atout)))
			{
				ajouter(jouer(position(new CarteBelote((byte)9,couleur_atout))), indiceAtout);
			}
			if(contient(new CarteBelote((byte)11,couleur_atout)))
			{
				ajouter(jouer(position(new CarteBelote((byte)11,couleur_atout))), indiceAtout);
			}
		}
		//On trie les cartes d'atout
	}
	public byte nombreCartesPoints(Carte carteAppelee, Contrat contrat)
	{
		byte nombre_cartes_points=0;
		for(Carte carte:this)
		{
			if(((CarteBelote)carte).points(contrat,carteAppelee)>0)
			{
				nombre_cartes_points++;
			}
		}
		return nombre_cartes_points;
	}
}