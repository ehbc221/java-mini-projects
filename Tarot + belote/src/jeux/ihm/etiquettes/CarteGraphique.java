package jeux.ihm.etiquettes;
import java.awt.*;
import javax.swing.*;
import jeux.*;
import jeux.cartes.*;
/**
 *
 */
public class CarteGraphique extends JLabel
{private static final long serialVersionUID = 1L;
static final String Default="Default";
private static final String ch_v=Lettre.ch_v;
private static final String et=Lettre.et+ch_v;
	private Carte c;
	private boolean coupeH;
	public CarteGraphique(Carte pc,int i,boolean pcoupe)
	{
		this(i,pcoupe);
		c=pc;
	}
	CarteGraphique(int i,boolean pcoupe)
	{
		setHorizontalAlignment(i);
		setVerticalAlignment(JLabel.TOP);
		coupeH=pcoupe;
	}
	public Carte getCarte()
	{
		return c;
	}
	void setCarte(Carte pc)
	{
		c=pc;
	}
	boolean coupe_horizontal()
	{
		return coupeH;
	}
	/**Methode importante dessinant les cartes des jeux de cartes face non cachee sauf pour certaines cartes du solitaire*/
	protected void paintComponent(Graphics g2)
	{
		Graphics2D g=(Graphics2D)g2;
		if(coupeH)
		{
			g.translate(-75,0);
		}
		g.setColor(Color.WHITE);
		g.fillRect(0,0,75+getWidth(),getHeight());
		g.setColor(Color.BLACK);
		if(coupeH)
			g.drawRect(0,0,75+getWidth()-1,getHeight()-1);
		else
			g.drawRect(0,0,getWidth()-1,getHeight()-1);
		if(c.couleur()==0)
		{
			for(byte b=0;b<2;b++)
			{
				g.setColor(Color.BLACK);
				g.setFont(new Font(Default,Font.BOLD,20));
				g.drawString(et,5,20);
				g.drawString(et,87,20);
				g.setColor(Color.GREEN);
				g.fillPolygon(new int[]{35,40,40,33,13,11,30,35,47,47,53,53,65,80,65,70},new int[]{75,60,40,55,61,56,50,35,35,30,30,35,35,55,65,75},16);
				g.setColor(Color.WHITE);
				g.fillPolygon(new int[]{60,60,70,62},new int[]{60,40,55,63},4);
				g.setColor(new Color(128,64,0));
				g.fillOval(35,65,35,10);
				g.setColor(Color.GREEN);
				g.fillPolygon(new int[]{70,56,60,80},new int[]{55,65,70,55},4);
				g.setColor(new Color(128,64,0));
				g.fillPolygon(new int[]{0,3,5,40,38,4},new int[]{60,50,55,67,72,58},6);
				g.setColor(Color.BLACK);
				g.fillOval(47,67,6,6);
				g.setColor(new Color(254,180,160));
				g.fillOval(53,67,6,6);
				g.fillOval(5,55,6,6);
				g.fillOval(40,10,20,20);
				g.setColor(Color.BLACK);
				g.drawLine(44,16,48,16);
				g.drawLine(56,16,52,16);
				g.drawPolygon(new int[]{47,50,53},new int[]{23,18,23},3);
				g.setColor(Color.RED);
				g.drawLine(46,26,54,26);
				g.fillPolygon(new int[]{50,60,62,38,40},new int[]{8,10,15,15,10,8},5);
				g.setColor(Color.YELLOW);
				g.fillRect(38,15,5,15);
				g.fillRect(57,15,5,15);
				g.setColor(Color.BLUE);
				g.drawLine(60,10,60,2);
				g.setColor(Color.BLACK);
				g.drawLine(0,75,100,75);
				g.rotate(Math.PI,50,75);
			}
		}
		else if(c.couleur()==1||c.valeur()<11)
		{//Cartes chiffrees
			if(c.valeur()>1)
			{
				int[][] coordonnees=coordonnees();
				for(byte b=0;b<2;b++)
				{
					if(c.couleur()>1)
					{
						if(c.couleur()%2==0)
							g.setColor(Color.RED);
						else
							g.setColor(Color.BLACK);
						g.setFont(new Font(Default,Font.BOLD,12));
						g.drawString(c.valeur()+ch_v,5,12);
						g.drawString(c.valeur()+ch_v,83,12);
					}
					else
					{
						g.setColor(Color.BLUE);
						g.setFont(new Font(Default,Font.BOLD,16));
						g.drawString(c.valeur()+ch_v,5,18);
						g.drawString(c.valeur()+ch_v,80,18);
						g.drawLine(45,10,55,10);
						g.drawLine(40,3,50,18);
						g.drawLine(50,18,60,3);
						g.setColor(Color.BLACK);
						g.drawLine(0,20,100,20);
						g.drawArc(0,-5,30,30,-45,90);
						g.drawArc(70,-5,30,30,135,90);
					}
					for(int i=0;i<coordonnees[0].length;i++)
						dessinerGrandSymbole(g,coordonnees[0][i],coordonnees[1][i]);
					if(c.couleur()>1)
					{
						dessinerPetitSymbole(g,5,17);
						dessinerPetitSymbole(g,85,17);
					}
					g.rotate(Math.PI,50,75);
				}
			}
			else
			{
				for(byte b=0;b<2;b++)
				{
					if(c.couleur()>1)
					{
						if(c.couleur()%2==0)
							g.setColor(Color.RED);
						else
							g.setColor(Color.BLACK);
						g.setFont(new Font(Default,Font.BOLD,12));
						g.drawString(c.valeur()+ch_v,5,12);
						g.drawString(c.valeur()+ch_v,83,12);
						dessinerPetitSymbole(g,5,17);
						dessinerPetitSymbole(g,85,17);
					}
					else
					{
						g.setColor(Color.BLUE);
						g.setFont(new Font(Default,Font.BOLD,16));
						g.drawString(c.valeur()+ch_v,7,18);
						g.drawString(c.valeur()+ch_v,83,18);
						g.drawLine(45,10,55,10);
						g.drawLine(40,3,50,18);
						g.drawLine(50,18,60,3);
						g.setColor(Color.BLACK);
						g.drawLine(0,20,100,20);
						g.drawArc(0,-5,30,30,-45,90);
						g.drawArc(70,-5,30,30,135,90);
					}
					g.rotate(Math.PI,50,75);
				}
			}
			if(c.valeur()%2==1)
			{
				int[][] pivot=pivot();
				dessinerGrandSymbole(g,pivot[0][(c.valeur()-1)/2],pivot[1][(c.valeur()-1)/2]);
			}
		}
		else
		{
			for(byte b=0;b<2;b++)
			{
				if(c.couleur()%2==0)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				g.setFont(new Font(Default,Font.BOLD,12));
				g.drawString(c.toString().charAt(0)+ch_v,5,12);
				g.drawString(c.toString().charAt(0)+ch_v,83,12);
				dessinerPetitSymbole(g,5,17);
				dessinerPetitSymbole(g,85,17);
				if(c.valeur()==13)
				{
					g.setColor(new Color(255,0,128));
				}
				else if(c.valeur()==14)
				{
					g.setColor(new Color(0,128,128));
				}
				else
				{
					g.setColor(new Color(128,128,0));
				}
				g.fillPolygon(new int[]{25,35,47,47,53,53,65,75},new int[]{75,45,45,40,40,45,45,75},8);
				g.setColor(Color.WHITE);
				g.fillPolygon(new int[]{30,40,40},new int[]{75,55,75},3);
				g.fillPolygon(new int[]{60,60,70},new int[]{75,55,75},3);
				g.setColor(new Color(254,180,160));
				g.fillOval(40,20,20,20);
				g.setColor(Color.BLACK);
				g.drawLine(44,26,48,26);
				g.drawLine(56,26,52,26);
				g.drawPolygon(new int[]{47,50,53},new int[]{33,28,33},3);
				g.setColor(Color.RED);
				g.drawLine(46,36,54,36);
				if(c.valeur()<13)
				{
					g.setColor(Color.GREEN);
					g.fillPolygon(new int[]{40,40,50,60,60,50},new int[]{25,20,15,20,25,25},6);
				}
				else
				{
					g.setColor(Color.YELLOW);
					g.fillPolygon(new int[]{55,60,55,58,54,50,46,42,45,40,45},new int[]{23,18,21,13,17,9,17,13,21,18,23},11);
				}
				g.setColor(new Color(128,64,0));
				if(c.valeur()==13)
				{
					g.fillPolygon(new int[]{43,43,47,47,35},new int[]{22,37,40,45,45},5);
					g.fillPolygon(new int[]{57,57,53,53,65},new int[]{22,37,40,45,45},5);
				}
				else
				{
					g.fillPolygon(new int[]{43,43,45,55,57,57,60,55,45,40},new int[]{22,32,35,35,32,22,30,40,40,30},10);
					g.setColor(Color.RED);
					g.drawLine(46,36,54,36);
					if(c.valeur()==12)
					{
						g.setColor(new Color(128,64,0));
						g.fillPolygon(new int[]{2,2,10,12,15,30,15,10,5},new int[]{60,55,47,40,50,75,75,55,60},9);
					}
				}
				dessinerGrandSymbole(g,15,7);
				dessinerGrandSymbole(g,60,7);
				g.rotate(Math.PI,50,75);
			}
			g.setColor(Color.BLACK);
			g.drawLine(0,75,100,75);
		}
	}
	private int[][] coordonnees()
	{
		byte valeur=c.valeur();
		int[][] tab=new int[2][valeur/2];
		if(valeur<4)
		{
			tab[0][0]=40;
			tab[1][0]=25;
		}
		else if(valeur<9)
		{
			tab[0][0]=10;
			tab[1][0]=25;
			tab[0][1]=70;
			tab[1][1]=25;
			if(valeur>5)
			{
				tab[0][2]=70;
				tab[1][2]=65;
				if(valeur==8)
				{
					tab[0][3]=40;
					tab[1][3]=40;
				}
			}
		}
		else
		{
			tab[0][0]=10;
			tab[1][0]=25;
			tab[0][1]=70;
			tab[1][1]=25;
			if(valeur<12)
			{
				tab[0][2]=10;
				tab[1][2]=50;
				tab[0][3]=70;
				tab[1][3]=50;
				if(valeur>9)
				{
					tab[0][4]=40;
					tab[1][4]=35;
				}
			}
			else
			{
				tab[0][2]=10;
				tab[0][3]=70;
				tab[0][4]=10;
				if(valeur<13)
				{
					tab[1][2]=45;
					tab[1][3]=45;
					tab[1][4]=65;
					tab[1][5]=45;
					tab[0][5]=40;
				}
				else if(valeur<15)
				{
					tab[1][2]=45;
					tab[1][3]=45;
					tab[1][4]=65;
					tab[1][5]=35;
					tab[0][5]=40;
					if(valeur==14)
					{
						tab[0][6]=40;
						tab[1][6]=55;
					}
				}
				else if(valeur<18)
				{
					tab[1][2]=40;
					tab[1][3]=40;
					tab[1][4]=55;
					tab[1][5]=55;
					tab[0][5]=70;
					tab[0][6]=40;
					tab[1][6]=30;
					if(valeur>15)
					{
						tab[0][7]=40;
						tab[1][7]=45;
					}
				}
				else
				{
					tab[1][2]=35;
					tab[1][3]=35;
					tab[1][4]=45;
					tab[0][5]=70;
					tab[1][5]=45;
					tab[0][6]=10;
					tab[1][6]=55;
					if(valeur<21)
					{
						tab[0][7]=40;
						tab[1][7]=30;
						if(valeur==18)
						{
							tab[0][8]=40;
							tab[1][8]=50;
						}
						else
						{
							tab[0][8]=40;
							tab[1][8]=40;
							if(valeur==20)
							{
								tab[0][9]=40;
								tab[1][9]=50;
							}
						}
					}
					else
					{
						tab[0][7]=70;
						tab[1][7]=55;
						tab[0][8]=40;
						tab[1][8]=30;
						tab[0][9]=40;
						tab[1][9]=40;
					}
				}
			}
		}
		return tab;
	}
	private int[][] pivot()
	{
		int[][] tab=new int[2][11];
		for(byte b=0;b<11;b++)
		{
			tab[0][b]=40;
			if(b!=3)
			{
				tab[1][b]=65;
			}
			else
			{
				tab[1][3]=40;
			}
		}
		return tab;
	}
	private void dessinerPetitSymbole(Graphics2D g,int x,int y)
	{
		byte couleur=c.couleur();
		if(couleur<3)
		{
			g.setColor(Color.RED);
			g.fillOval(x,y,5,5);
			g.fillOval(x+5,y,5,5);
			g.fillRect(x+3,y+3,5,5);
			g.setColor(Color.WHITE);
			g.fillOval(x,y+5,5,5);
			g.fillOval(x+5,y+5,5,5);
		}
		else if(couleur<4)
		{
			g.setColor(Color.BLACK);
			g.fillOval(x,y+2,5,5);
			g.fillOval(x+5,y+2,5,5);
			g.fillPolygon(new int[]{5+x,8+x,5+x,2+x},new int[]{5+y,12+y,8+y,12+y},4);
			g.fillRect(x+3,y-2,5,7);
			g.setColor(Color.WHITE);
			g.fillOval(x,y-3,5,5);
			g.fillOval(x+5,y-3,5,5);
		}
		else if(couleur<5)
		{
			g.setColor(Color.RED);
			g.fillPolygon(new int[]{x,5+x,10+x,5+x},new int[]{5+y,y,5+y,10+y},4);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.fillOval(x,y+3,4,4);
			g.fillOval(x+6,y+3,4,4);
			g.fillOval(x+3,y,4,4);
			g.fillPolygon(new int[]{3+x,5+x,3+x},new int[]{4+y,5+y,6+y},3);
			g.fillPolygon(new int[]{6+x,5+x,6+x},new int[]{4+y,5+y,6+y},3);
			g.fillPolygon(new int[]{4+x,5+x,6+x},new int[]{3+y,5+y,3+y},3);
			g.fillPolygon(new int[]{3+x,5+x,6+x,5+x},new int[]{10+y,5+y,10+y,8+y},4);
		}
	}
	private void dessinerGrandSymbole(Graphics2D g,int x,int y)
	{
		byte couleur=c.couleur();
		if(couleur<2)
		{
			g.setColor(Color.BLUE);
			g.drawLine(x,y,x+10,y+10);
			g.setColor(Color.BLACK);
			g.fillPolygon(new int[]{10+x,12+x,15+x,15+x,12+x,10+x},new int[]{10+y,10+y,12+y,15+y,15+y,12+y},6);
		}
		else if(couleur<3)
		{
			g.setColor(Color.RED);
			g.fillOval(x,y,10,10);
			g.fillOval(x+10,y,10,10);
			g.fillRect(x+5,y+5,10,10);
			g.setColor(Color.WHITE);
			g.fillOval(x,y+10,10,10);
			g.fillOval(x+10,y+10,10,10);
		}
		else if(couleur<4)
		{
			g.setColor(Color.BLACK);
			g.fillOval(x,y+5,10,10);
			g.fillOval(x+10,y+5,10,10);
			g.fillPolygon(new int[]{10+x,13+x,10+x,7+x},new int[]{10+y,20+y,17+y,20+y},4);
			g.fillRect(x+5,y,10,10);
			g.setColor(Color.WHITE);
			g.fillOval(x,y-5,10,10);
			g.fillOval(x+10,y-5,10,10);
		}
		else if(couleur<5)
		{
			g.setColor(Color.RED);
			g.fillPolygon(new int[]{x,10+x,20+x,10+x},new int[]{10+y,y,10+y,20+y},4);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.fillOval(x,y+6,8,8);
			g.fillOval(x+12,y+6,8,8);
			g.fillOval(x+6,y,8,8);
			g.fillPolygon(new int[]{7+x,10+x,7+x},new int[]{8+y,10+y,12+y},3);
			g.fillPolygon(new int[]{13+x,10+x,13+x},new int[]{8+y,10+y,12+y},3);
			g.fillPolygon(new int[]{8+x,10+x,12+x},new int[]{7+y,10+y,7+y},3);
			g.fillPolygon(new int[]{7+x,10+x,13+x,10+x},new int[]{20+y,10+y,20+y,17+y},4);
		}
	}
}