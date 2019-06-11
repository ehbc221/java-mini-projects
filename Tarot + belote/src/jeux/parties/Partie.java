package jeux.parties;
import java.io.*;
import java.util.*;
import jeux.*;
import jeux.enumerations.*;
/**Classe gerant une partie de cartes qui est sauvable
 * elle permet de gerer les annonces, le contrat, le jeu...*/
public abstract class Partie implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nombre de joueurs dans une partie de cartes*/
	private byte nombreDeJoueurs;
	/**Le type d'une partie est aleatoire ou encore edite ou encore un entrainement*/
	private Type type;
	/**Contient toutes les cartes au debut de chaque partie*/
	private Donne distribution;
	/**Scores cumules au cours des parties
	 * Chaque nombre (Short) represente un score pour le joueur*/
	private Vector<Short> scores=new Vector<Short>();
	/**Etat de la partie (Contrat,Appel,Ecart,Jeu)*/
	private Etat etat;
	/**Nombre de fois qu'a ete joue la partie(partie fini)*/
	private long nombre;
	public static volatile int chargement_simulation;
	Partie(Type ptype,Donne donne)
	{
		type=ptype;
		distribution=donne;
		if(this instanceof PartieBelote)
			nombreDeJoueurs=4;
		else
			nombreDeJoueurs=(byte)(distribution.nombreDeMains()-1);
		if(this instanceof PartieBelote||this instanceof PartieTarot&&((PartieTarot)this).avec_contrat())
			etat=Etat.Contrat;
		else
			etat=Etat.Jeu;
		for(int i=0;i<nombreDeJoueurs;i++)
			scores.addElement((short) 0);
	}
	public long getNombre()
	{
		return nombre;
	}
	public void setNombre()
	{
		nombre++;
	}
	public Etat getEtat()
	{
		return etat;
	}
	public void setEtat(Etat petat)
	{
		etat=petat;
	}
	public abstract Jeu jeu();
	public Vector<String> getInfos()
	{
		return distribution.getInfos();
	}
	/**Appelee pour la belote et le solitaire et pour jouer une partie editee*/
	public String getMode() {
		return distribution.getMode();
	}
	public Vector<Short> getScores()
	{
		return scores;
	}
	/**Renvoie le nombre de joueurs jouant a la partie*/
	public byte getNombreDeJoueurs()
	{
		return nombreDeJoueurs;
	}
	public Donne getDistribution()
	{
		return distribution;
	}
	public Type getType()
	{
		return type;
	}
	public void sauvegarder(String fichier)
	{
		File f=new File(fichier);
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			oos.writeObject(this);
			oos.close();
		} catch (Exception e) {}
	}
}