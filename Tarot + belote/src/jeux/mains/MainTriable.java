package jeux.mains;
import jeux.cartes.*;
/**Classe permettant de trier les cartes de l'utilisateur sur l'ihm mais on ne trie par contre pas les mains des autres joueurs
 *
 */
public abstract class MainTriable extends Main {
	private static final long serialVersionUID = 1L;
	/**Construit une main vide triable*/
	MainTriable() {}
	/**Constructeur permettant a partir d'un tableau de cartes static de contruire une main triable*/
	MainTriable(Carte[] tc)
	{
		super(tc);
	}
	/**Permet de trier les cartes selon les parametres
	 * @param couleurs une chaine de couleurs espace par des tirets
	 * @param sens croissant ou decroissant*/
	public abstract void trier(String couleurs,String sens);
}