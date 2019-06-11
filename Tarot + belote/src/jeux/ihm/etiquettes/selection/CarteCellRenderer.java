package jeux.ihm.etiquettes.selection;
import java.awt.*;
import javax.swing.*;
import jeux.cartes.*;
import jeux.*;
/**
 *
 */
public class CarteCellRenderer extends CellRenderer{
	private static final long serialVersionUID = 1L;
	private static final char c0=Lettre.c0;
	private static final char c9=Lettre.c9;
	private static final String es=Lettre.ch_v+Lettre.es;
	private Carte c;
	private boolean selectionne;
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		c=(Carte)value;
		selectionne=isSelected;
		setPreferredSize(new Dimension(50,10));
		return this;
	}
	protected void paintComponent(Graphics g)
	{
		if(!selectionne)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLUE);
		g.fillRect(0,0,50,10);
		byte couleur=c.couleur();
		if(couleur<2)
		{
			g.setColor(Color.BLUE);
			g.drawLine(0,0,5,10);
			g.drawLine(2,5,8,5);
			g.drawLine(10,0,5,10);
		}
		else if(couleur<3)
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
		else if(couleur<4)
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
		else if(couleur<5)
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
		String chaineCarte=c.toString();
		if(!selectionne)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		if(chaineCarte.charAt(0)>c0&&chaineCarte.charAt(0)<=c9)
		{
			g.drawString(chaineCarte.split(es)[0],10,10);
		}
		else
		{
			g.drawString(chaineCarte.substring(0,1),10,10);
		}
	}
}