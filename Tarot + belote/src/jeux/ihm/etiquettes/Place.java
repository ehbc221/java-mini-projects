package jeux.ihm.etiquettes;
import java.awt.*;
import jeux.cartes.*;
import jeux.enumerations.*;
/**Classe utilisee pour une etiquette a afficher sur le tapis pour des jeux non solitaires
 *
 */
public class Place extends CarteGraphique {
	private static final long serialVersionUID = 1L;
	/**Jeu auquel on joue*/
	private Jeu jeu;
	/**Construit une etiquette sur le tapis (carte jouee) qui peut etre montre par un joueur qui vient de la jouer*/
	public Place(Carte c, int i, boolean pcoupe)
	{
		super(c, i, pcoupe);
	}
	/**Construit une etiquette sur le tapis (dos d'une carte) entre chaque pli*/
	public Place(Jeu pj, int i, boolean pcoupe)
	{
		super(i,pcoupe);
		jeu=pj;
	}
	public void setCarte(Carte c)
	{
		super.setCarte(c);
	}
	public void setJeu(Jeu pj)
	{
		jeu=pj;
	}
	/**Methode importante dessinant les dos des cartes, cette methode evite d enlever les elements graphiques et d en rajouter*/
	protected void paintComponent(Graphics g2)
	{
		if(jeu==null)
		{
			super.paintComponent(g2);
		}
		else
		{
			g2.setColor(Color.RED);
			if(coupe_horizontal())
				g2.translate(-75,0);
			g2.fillRect(0,0,75+getWidth(),getHeight());
			g2.setColor(Color.BLACK);
			if(coupe_horizontal())
				g2.drawRect(0,0,75+getWidth()-1,getHeight()-1);
			else
				g2.drawRect(0,0,getWidth()-1,getHeight()-1);
			g2.setColor(Color.BLUE);
			g2.setFont(new Font(Default,Font.BOLD,20));
			g2.drawString(jeu.toString(),20,80);
		}
	}
}