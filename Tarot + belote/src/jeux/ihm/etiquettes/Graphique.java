package jeux.ihm.etiquettes;
import java.awt.*;
import java.util.*;
import javax.swing.*;
/**
 *
 */
public class Graphique extends JLabel {
	private static final long serialVersionUID = 1L;
	private Vector<Vector<Long>> scores;
	private Vector<Long> sommes;
	private Vector<Double> sigmas;
	private Vector<Color> couleurs;
	public Graphique(Vector<Vector<Long>> pscores,Vector<Long> psommes,Vector<Double> psigmas,Vector<Color> pcouleurs)
	{
		scores=pscores;
		sommes=psommes;
		sigmas=psigmas;
		couleurs=pcouleurs;
	}
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2=(Graphics2D)g;
		int rapport=getWidth()/scores.size();
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,getWidth(),getHeight());
		g2.translate(0,getHeight()/2);
		dessiner_pointilles(g2,true);
		dessiner_pointilles(g2,false);
		g2.setColor(Color.BLACK);
		g2.drawLine(0,0,getWidth(),0);
		int nombre_joueurs=scores.lastElement().size();
		double esperance;
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			g2.setColor(couleurs.get(joueur));
			esperance=sommes.get(0)/(double)nombre_joueurs;
			g2.drawLine(0,0,rapport,(int)(esperance-scores.get(0).get(joueur)));
			for(int partie=0;partie<scores.size()-1;partie++)
			{
				esperance=sommes.get(partie)/(double)nombre_joueurs;
				double esperance2=sommes.get(partie+1)/(double)nombre_joueurs;
				g2.drawLine(rapport*(partie+1),(int)(esperance-scores.get(partie).get(joueur)),rapport*(partie+2),(int)(esperance2-scores.get(partie+1).get(joueur)));
			}
		}
		g2.setColor(Color.GRAY);
		double esp_plus_3_sigmas=sigmas.get(0);
		double esp_moins_3_sigmas=-sigmas.get(0);
		g2.drawLine(0,0,rapport,(int)-esp_plus_3_sigmas);
		g2.drawLine(0,0,rapport,(int)-esp_moins_3_sigmas);
		for(int partie=0;partie<scores.size()-1;partie++)
		{
			esp_plus_3_sigmas=sigmas.get(partie);
			double esp_plus_3_sigmas2=sigmas.get(partie+1);
			esp_moins_3_sigmas=-sigmas.get(partie);
			double esp_moins_3_sigmas2=-sigmas.get(partie+1);
			g2.drawLine(rapport*(partie+1),(int)-esp_plus_3_sigmas,rapport*(partie+2),(int)-esp_plus_3_sigmas2);
			g2.drawLine(rapport*(partie+1),(int)-esp_moins_3_sigmas,rapport*(partie+2),(int)-esp_moins_3_sigmas2);
		}
	}
	private void dessiner_pointilles(Graphics2D g2,boolean horizontal)
	{
		g2.setColor(Color.BLACK);
		int rapport2=10;
		if(horizontal)
		{
			int nombre=getHeight()/2;
			int nombre2=getWidth()/2;
			g2.drawString(new Integer(0).toString(),0,0);
			for(int ordonnee=1;ordonnee<nombre;ordonnee++)
			{
				g2.drawString(new Integer(ordonnee*rapport2).toString(),0,-ordonnee*rapport2);
				g2.drawString(new Integer(-ordonnee*rapport2).toString(),0,ordonnee*rapport2);
				for(int abscisse=0;abscisse<nombre2;abscisse++)
				{
					g2.drawLine(2*abscisse*rapport2,ordonnee*rapport2,(2*abscisse+1)*rapport2,ordonnee*rapport2);
				}
				for(int abscisse=0;abscisse<nombre2;abscisse++)
				{
					g2.drawLine(2*abscisse*rapport2,-ordonnee*rapport2,(2*abscisse+1)*rapport2,-ordonnee*rapport2);
				}
			}
		}
		else
		{
			int rapport=getWidth()/scores.size();
			int nombre=getHeight()/2;
			int nombre2=scores.size();
			for(int abscisse=0;abscisse<nombre2;abscisse++)
			{
				g2.drawString(new Integer(abscisse+1).toString(),(abscisse+1)*rapport,0);
				for(int ordonnee=1;ordonnee<nombre;ordonnee++)
				{
					g2.drawLine((abscisse+1)*rapport,2*ordonnee*rapport2,(abscisse+1)*rapport,(2*ordonnee+1)*rapport2);
				}
				for(int ordonnee=1;ordonnee<nombre;ordonnee++)
				{
					g2.drawLine((abscisse+1)*rapport,-2*ordonnee*rapport2,(abscisse+1)*rapport,-(2*ordonnee+1)*rapport2);
				}
			}
		}
	}
}