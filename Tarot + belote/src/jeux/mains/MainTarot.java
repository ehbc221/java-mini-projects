package jeux.mains;
import java.util.*;
import jeux.cartes.*;
/**
 *
 */
public class MainTarot extends MainTriable{
	private static final long serialVersionUID = 1L;
	public MainTarot() {}
	public MainTarot(Carte[] cartes) {
		super(cartes);
	}
	/**Permet de trier les cartes selon les parametres
	 * @param couleurs une chaine de couleurs espacee par des tirets
	 * @param sens croissant ou decroissant*/
	public void trier(String couleurs, String sens) {
		trier(couleurs, sens, null);
	}
	private void trier(String couleurs, String sens, String ordre) {
		if(couleurs.contains(":"))
		couleurs=couleurs.split(":")[1].split(";")[0];
		if(sens.contains(";"))
		sens=sens.split(";")[1];
		for(int i=0;i<total();i++)
			for(int j=i+1;j<total();j++)
				if(((Triable)carte(j)).vientAvant((Triable)carte(i), sens, ordre, couleurs))
					echanger(i,j);
	}
	/**Permet de trier une main de tarot lorsque les cartes de cette main sont toutes de la meme couleur par ordre decroissant des valeurs*/
	public void trier()
	{
		for(int i=0;i<total();i++)
			for(int j=i+1;j<total();j++)
				if(carte(j).valeur()>carte(i).valeur())
					echanger(i,j);
	}
	public int nombreDeFigures()
	{
		int nb=0;
		for(byte couleur=2;couleur<6;couleur++)
			nb+=nombreDeFigures(couleur);
		return nb;
	}
	public int nombreDeBouts()
	{
		return bouts().total();
	}
	public MainTarot bouts()
	{
		MainTarot mt=new MainTarot();
		for(Carte carte:this)
			if(((CarteTarot)carte).estUnBout())
				mt.ajouter(carte);
		return mt;
	}
	/**Retourne vrai si et seulement si la main a pour seul atout le Petit sans L'Excuse*/
	public boolean petitSec()
	{
		boolean presence_petit=false;
		byte nombre_atouts=0;
		for(Carte carte:this)
		{
			if(carte.couleur()<2)
			{
				nombre_atouts++;
				if(carte.valeur()==1)
				{
					presence_petit=true;
				}
			}
		}
		return presence_petit&&nombre_atouts==1;
	}
	public int tailleRois()
	{
		return tailleValeur((byte)14);
	}
	public int tailleDames()
	{
		return tailleValeur((byte)13);
	}
	public int tailleCavaliers()
	{
		return tailleValeur((byte)12);
	}
	public int tailleValets()
	{
		return tailleValeur((byte)11);
	}
	/**Ne leve pas d exception*/
	public int nombreDeFigures(byte couleur)
	{
		int nb=0;
		for(int i=0;i<total();i++)
			if(carte(i).valeur()>10&&carte(i).couleur()==couleur)
				nb++;
		return nb;
	}
	/**Eclate une couleur en suite en tenant compte des cartes jouees*/
	public Vector<MainTarot> eclater(Vector<MainTarot> cartesJouees)
	{
		if(estVide())
		{
			return new Vector<MainTarot>();
		}
		Vector<MainTarot> suites=new Vector<MainTarot>();
		byte couleur=carte(0).couleur();
		MainTarot union=new MainTarot();
		union.ajouterCartes(cartesJouees.get(couleur));//C'est la reunion des cartes jouees dans le jeu et de celles du joueur
		for(int i=0;i<total();i++)
		{
			Carte c=carte(i);
			if(union.estVide()||union.derniereCarte().valeur()>c.valeur())
			{
				union.ajouter(c);
			}
			else
			{
				int j=0;
				for(;union.carte(j).valeur()>c.valeur();j++);
				union.ajouter(c,j);
			}
		}
		int cte=union.carte(0).valeur();
		for(int i=0;i<union.total();i++)
		{
			if(i+union.carte(i).valeur()==cte)
			{
				if(contient(union.carte(i)))
				{
					if(suites.isEmpty())
						suites.addElement(new MainTarot());
					suites.lastElement().ajouter(union.carte(i));
				}
			}
			else if(contient(union.carte(i)))
			{
				suites.addElement(new MainTarot());
				suites.lastElement().ajouter(union.carte(i));
				cte=i+union.carte(i).valeur();
			}
		}
		return suites;
	}
	public MainTarot atoutsMaitres(Vector<MainTarot> cartesJouees)
	{
		if(estVide())
		{
			return new MainTarot();
		}
		MainTarot suite=new MainTarot();
		byte couleur=carte(0).couleur();
		MainTarot union=new MainTarot();
		union.ajouterCartes(cartesJouees.get(couleur));//C'est la reunion des cartes jouees dans le jeu et de celles du joueur
		for(Carte carte:this)
		{
			if(union.estVide()||union.derniereCarte().valeur()>carte.valeur())
			{
				union.ajouter(carte);
			}
			else
			{
				int j=0;
				for(;union.carte(j).valeur()>carte.valeur();j++);
				union.ajouter(carte,j);
			}
		}
		for(int i=0;i<union.total();i++)
		{
			if(i+union.carte(i).valeur()==21)
			{
				if(contient(union.carte(i)))
				{
					suite.ajouter(union.carte(i));
				}
			}
			else
			{
				break;
			}
		}
		return suite;
	}
	public MainTarot figures(byte couleur)
	{
		MainTarot m=new MainTarot();
		for(Carte carte:this)
			if(carte.couleur()==couleur&&carte.valeur()>10)
				m.ajouter(carte);
		return m;
	}
	public MainTarot cartesBasses(byte couleur)
	{
		MainTarot m=new MainTarot();
		for(Carte carte:this)
			if(carte.couleur()==couleur&&carte.valeur()<11)
				m.ajouter(carte);
		return m;
	}
	/**Renvoie vrai si et seulement si le nombre de figures passe en parametre est egal au nombre de figures
	 * ceci n'est efficace que pour des mains triees dans l'ordre decroissant des valeurs d'une meme couleur*/
	public boolean nombreFiguresEgal(int n)
	{
		return (total()==n&&carte(n-1).valeur()>10)||(total()>n&&carte(n-1).valeur()>10&&carte(n).valeur()<11);
	}
	/**Renvoie vrai si et seulement si le nombre de figures passe en parametre est superieur ou egal au nombre de figures
	 * ceci n'est efficace que pour des mains triees dans l'ordre decroissant des valeurs d'une meme couleur*/
	public boolean nombreFiguresSuperieurOuEgal(int n)
	{
		return total()>=n&&carte(n-1).valeur()>10;
	}
	/**Construit une nouvelle main telle que les cartes aient toutes leurs couleurs valant
	 * la couleur passee en parametres et trie la main dans l'ordre decroissant des valeurs
	 * @param couleur le numero de couleur a passer en parametres (0:Excuse 1:Atout(Excuse exclue) 2:Coeur 3:Pique 4:Carreau 5:Tr&egrave;fle)
	 * @return une main triee avec les cartes de meme couleur que celle passee en parametres*/
	public MainTarot couleur(byte couleur)
	{
		MainTarot m=new MainTarot();
		for(Carte c:this)
			if(c.couleur()==couleur)
				m.ajouter(c);
		for (int i=0;i<m.total();i++)
			for (int j=i+1;j<m.total();j++)
				if(m.carte(j).valeur()>m.carte(i).valeur())
					m.echanger(i,j);
		return m;
	}
	/**Donne la repartition des cartes de la main dans les differentes couleurs
	 * pour une main de tarot les cartes d'une meme couleur sont classees par ordre decroissant de la valeur
	 * @return un vecteur de mains ordonnee par numero de couleur croissant*/
	public Vector<MainTarot> couleurs()
	{
		Vector<MainTarot> main=new Vector<MainTarot>();
		for(byte b=0;b<6;b++)
			main.add(couleur(b));
		return main;
	}
}