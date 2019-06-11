package jeux.mains;
import java.io.*;
import java.util.*;
import jeux.*;
import jeux.cartes.*;
/**
 *
 */
public abstract class Main implements Serializable,Iterable<Carte>
{
	private static final long serialVersionUID = 1L;
	private Vector<Carte> main=new Vector<Carte>();
	/**Constructeur premettant de simplifier le code*/
	Main(){}
	/**Utilise pour l'editeur*/
	Main(Carte[] cartes)
	{
		for(Carte carte:cartes)
			main.addElement(carte);
	}
	/**On prend la premiere carte de la main puis on la place a la fin
	 * dans le but de deplacer la moitie d'une main apres l'autre*/
	public void couper()
	{
		int taille=total()/2;
		for (int i=0;i<taille;i++)
			ajouter(jouer(0));
	}
	/**<ol>
	 * <li>Choisit un nombre aleatoire entre 0 et le nombre total de carte dans la main moins une carte
	 * d'ou total()*Math.random() puis Math.floor(total()*Math.random())</li>
	 * <li>puis retire la carte choisie de la main courante et la retourne</li>
	 * </ol>
	 * @return la carte aleatoire choisie*/
	public Carte tirerUneCarteAleatoire()
	{
		return jouer((int)Math.floor(total()*Math.random()));
		//0<=total()*Math.random()<total()
		//Donc 0<=Math.floor(total()*Math.random())<Math.floor(total())=total()
	}
	/**Ajoute une carte a la fin de la main
	 * et precise le jeu
	 * @param t la carte a ajouter*/
	public void ajouter(Carte t)
	{
		main.addElement(t);
	}
	/**Ajoute une carte a la position donnee
	 * @param t la carte a ajouter
	 * @param a la position ou placer la carte*/
	public void ajouter(Carte t,int a)
	{
		main.add(a,t);
	}
	/**Utilise pour l'editeur*/
	public void ajouterCartes(Carte[] cartes)
	{
		for(Carte t:cartes)
			main.addElement(t);
	}
	/**Ajoute les cartes d'une main dans la main courante
	 * a la fin de celle-ci les cartes ajoutees conservent le
	 * meme ordre qu'a l'etat initial
	 * @param cartes la main a ajouter*/
	public void ajouterCartes(Main cartes)
	{
		for(Carte carte:cartes)
		{
			main.addElement(carte);
		}
	}
	/**Joue une carte a la position a
	 * @return la carte a jouer*/
	public Carte jouer(int a)
	{
		return main.remove(a);
	}
	public void jouer(Carte c)
	{
		main.removeElementAt(main.indexOf(c));
	}
	void supprimer(int carte)
	{
		main.removeElementAt(carte);
	}
	/**Supprime les cartes d'une main tout en conservant les eventuels doublons*/
	public void supprimerCartes(Main m)
	{
		int indice;
		for(Carte c:m)
		{
			indice=position(c);
			if(indice>-1)
			{
				supprimer(indice);
			}
		}
	}
	/**Renvoie le nombre total de cartes dans la main*/
	public int total()
	{
		return main.size();
	}
	/**Renvoie la carte a la position i*/
	public Carte carte(int i)
	{
		return main.elementAt(i);
	}
	public Carte derniereCarte()
	{
		return main.lastElement();
	}
	/**Tester l'existence d'une carte dans une main*/
	public boolean contient(Carte c)
	{
		return main.contains(c);
	}
	public void supprimerCartes()
	{
		main.removeAllElements();
	}
	public int position(Carte c)
	{
		return main.indexOf(c);
	}
	void changer(Carte c,int i)
	{
		main.setElementAt(c, i);
	}
	/**On echange deux cartes dans la main*/
	public void echanger(int a,int b)
	{
		Carte c=carte(a);
		changer(carte(b),a);
		changer(c,b);
	}
	public int tailleCouleur(byte couleur)
	{
		int taille=0;
		for(Carte c:this)
		{
			if(c.couleur()==couleur)
			{
				taille++;
			}
		}
		return taille;
	}
	public int tailleValeur(byte valeur)
	{
		int taille=0;
		for(Carte c:this)
		{
			if(c.valeur()==valeur&&c.couleur()>1)
			{
				taille++;
			}
		}
		return taille;
	}
	public boolean estVide()
	{
		return main.isEmpty();
	}
	/**Deux mains sont egales si et seulement elles ont les memes cartes*/
	public boolean equals(Object o)
	{
		if(o==null||!(o instanceof Main)||!(o instanceof Carte))
			return false;
		if(o instanceof Carte)
		{
			return total()==1&&((Carte)o).equals(carte(0));
		}
		if(((Main) o).total()!=total())
			return false;
		boolean id=true;
		for (int i = 0; i < total(); i++) {
			id&=((Main)o).carte(i).equals(carte(i));
		}
		return id;
	}
	public String toString()
	{
		String retString=Lettre.ch_v;
		for (int i = 0; i < total(); i++) {
			if(i<total()-1)
				retString+=carte(i)+Parametres.separateur_tiret;
			else
				retString+=carte(i);
		}
		return retString;
	}
	public void sauvegarder(String fichier)
	{
		try{
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fichier))));
			oos.writeObject(this);
			oos.close();
		}catch (Exception e) {}
	}
	/**Permet d'utiliser une Main iterable par un vecteur iterable*/
	public Iterator<Carte> iterator() {
		return main.iterator();
	}
}