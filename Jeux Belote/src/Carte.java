import java.awt.Image;

import javax.swing.ImageIcon;

//=====================================================
// definition d'une carte
//=====================================================
public class Carte {
	Couleur couleur ;
	
	Figure figure;	
	
	ImageIcon image;
	
	public Carte ( Couleur c , Figure f ){
		couleur= c ;
		figure= f ;
		image=new ImageIcon ("image/"+figure.nom+couleur.nom+".gif");
	}
	
	public Carte clone(){
		return new Carte(couleur,figure);
	}
	
	public Image getImage(){
		return image.getImage();
	}

	public String getCouleur(){
		return couleur.nom;
	}

	public String getFigure(){
		return figure.nom;
	}

	public int getPoint(){
		return figure.point;
	}

	public int getPointAtout(){
		return figure.pointAtout;
	}

	public int getValeurAtout(){
		return figure.valeurAtout;
	}

	public int getValeur(){
		return figure.valeur;
	}
}