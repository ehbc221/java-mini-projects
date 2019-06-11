package jeux.cartes;
public interface Triable
{
	byte forceValeurDansUnTri(String sens,String ordre);
	byte forceValeurDansUnTri(String ordre);
	byte forceValeurDansUnTri();
	public boolean vientAvant(Triable c,String sens,String ordre,String couleurs);
}