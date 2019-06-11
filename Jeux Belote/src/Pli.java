import java.util.ArrayList;

public class Pli {
	Carte cartePli[]=new Carte [4];
	int pointEstOuest=0;
	int pointSudNord=0;
	boolean pause=true;
	private static final int sud=0;
	private static final int est=1;
	private static final int nord=2;
	private static final int ouest=3;
	
	ArrayList<Carte> tas = new ArrayList<Carte>();
	
	Pli(){
		cartePli[0]=null;
		cartePli[1]=null;
		cartePli[2]=null;
		cartePli[3]=null;
	}
	
	void setPli(int joueur,Carte laCarte){
		cartePli[joueur]=laCarte;
	}
	
	public int remportePli(String couleurJouee,String atout,int tour) {
		int nbPoint=0;
		int pos = 0;
		int val;
		int val2 = 0;
		int point=0;
		System.out.println("atout= "+atout);
		for (int x = 0; x < 4; x++) {
			val = Arbitre.rangPli(couleurJouee,cartePli[x],atout);
			if (val > val2) {
				val2 = val;
				pos = x;
			}
			if(cartePli[x].getCouleur().equals(atout))point=cartePli[x].getPointAtout();
			else point=cartePli[x].getPoint();
			System.out.println(""+cartePli[x].getFigure()+" de "+cartePli[x].getCouleur()+" point = "+point);
			nbPoint+=point;
			point=0;
		}
		System.out.println("somme des points = "+nbPoint);
		if(tour==7)nbPoint+=10;
		if(pos==sud||pos==nord) pointSudNord+=nbPoint;
		if(pos==est||pos==ouest) pointEstOuest+=nbPoint;
		nbPoint=0;
		tas.add(cartePli[sud].clone());
		tas.add(cartePli[est].clone());
		tas.add(cartePli[nord].clone());
		tas.add(cartePli[ouest].clone());
		return pos;
	}
	
	public void gagnePartie() {
		System.out.println("L'equipe sud nord a "+pointSudNord+" points");
		System.out.println("L'equipe est ouest a "+pointEstOuest+" points");
	}
	
}
