//=====================================================
// definition des figures des cartes (7,8,. . .,As)
//=====================================================
class Figure {
	String nom;
	
	int pointAtout=0;
	
	int point=0;
	
	int valeurAtout=0;
	
	int valeur=0;
	

	Figure ( String nomFigure,int point ,int pointAtout,int valeur,int valeurAtout){
		nom = nomFigure ;
		this.pointAtout=pointAtout;
		this.point=point;
		this.valeurAtout=valeurAtout;
		this.valeur=valeur;
	}
}