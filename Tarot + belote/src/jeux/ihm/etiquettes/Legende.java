package jeux.ihm.etiquettes;
import java.awt.*;
import java.util.*;
import javax.swing.*;
public class Legende extends JLabel {
	private static final long serialVersionUID = 1L;
	private static final String Default="Default";
	private Vector<Color> couleurs;
	private Vector<String> pseudos;
	public Legende(Vector<String> ppseudos,Vector<Color> pcouleurs)
	{
		pseudos=ppseudos;
		couleurs=pcouleurs;
	}
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,getWidth(),getHeight());
		g2.setColor(Color.BLACK);
		g2.translate(getWidth()/2,0);
		g2.setFont(new Font(Default,Font.BOLD,10));
		for(String pseudo:pseudos)
		{
			g2.translate(0,15);
			g2.drawString(pseudo,0,0);
		}
		g2.translate(0,15);
		g2.drawString("+3*sigma et -3*sigma (Ecarts maximaux de la moyenne)",0,0);
		g2.translate(-getWidth()/2,-15*(pseudos.size()+1));
		for(Color couleur:couleurs)
		{
			g2.translate(0,15);
			g2.setColor(couleur);
			g2.drawLine(getWidth()/8,0,3*getWidth()/8,0);
		}
		g2.translate(0,15);
		g2.setColor(Color.GRAY);
		g2.drawLine(getWidth()/8,0,3*getWidth()/8,0);
	}
}