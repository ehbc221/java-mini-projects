package jeux.ihm.etiquettes.selection;
import java.awt.*;
import javax.swing.*;
//import jeux.*;
import jeux.enumerations.*;
/**
 *
 */
public class CouleurCellRenderer extends CellRenderer {
	private static final long serialVersionUID = 1L;
	private String couleur;
	private boolean selectionne;
	/**Donne la facon de presenter une couleur dans une liste avec un symbole et un nom*/
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		couleur=(String)value;
		selectionne=isSelected;
		setPreferredSize(new Dimension(100,10));
		return this;
	}
	protected void paintComponent(Graphics g)
	{
		if(!selectionne)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLUE);
		g.fillRect(0,0,100,10);
		if(couleur.equals(Couleur.Atout.toString()))
		{
			g.setColor(Color.BLUE);
			g.drawLine(0,0,5,10);
			g.drawLine(2,5,8,5);
			g.drawLine(10,0,5,10);
		}
		else if(couleur.equals(Couleur.Coeur.toString()))
		{
			g.setColor(Color.RED);
			g.fillOval(0,0,5,5);
			g.fillOval(5,0,5,5);
			g.fillRect(3,3,5,5);
			if(!selectionne)
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.BLUE);
			g.fillOval(0,5,5,5);
			g.fillOval(5,5,5,5);
		}
		else if(couleur.equals(Couleur.Pique.toString()))
		{
			g.setColor(Color.BLACK);
			g.fillOval(0,2,5,5);
			g.fillOval(5,2,5,5);
			g.fillPolygon(new int[]{5,8,5,2},new int[]{5,12,8,12},4);
			g.fillRect(3,-2,5,7);
			if(!selectionne)
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.BLUE);
			g.fillOval(0,-3,5,5);
			g.fillOval(5,-3,5,5);
		}
		else if(couleur.equals(Couleur.Carreau.toString()))
		{
			g.setColor(Color.RED);
			g.fillPolygon(new int[]{0,5,10,5},new int[]{5,0,5,10},4);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.fillOval(0,3,4,4);
			g.fillOval(6,3,4,4);
			g.fillOval(3,0,4,4);
			g.fillPolygon(new int[]{3,5,3},new int[]{4,5,6},3);
			g.fillPolygon(new int[]{6,5,6},new int[]{4,5,6},3);
			g.fillPolygon(new int[]{4,5,6},new int[]{3,5,3},3);
			g.fillPolygon(new int[]{3,5,6,5},new int[]{10,5,10,8},4);
		}
		if(!selectionne)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		g.drawString(couleur,10,10);
	}
}