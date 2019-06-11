/*
 *  constitution du jeu de carte
 */


import java.util.ArrayList;

public class Paquet {

	ArrayList<Carte> unPaquet = new ArrayList<Carte>();

	ArrayList<Carte> lePaquet = new ArrayList<Carte>(32);

	private static final Couleur Coeur = new Couleur("Coeur");

	private static final Couleur Pique = new Couleur("Pique");

	private static final Couleur Carreau = new Couleur("Carreau");

	private static final Couleur Trefle = new Couleur("Trefle");

	private static final Couleur Blanc = new Couleur("Blanc");

	private static final Figure Sept = new Figure("Sept", 0, 0, 1, 9);

	private static final Figure Huit = new Figure("Huit", 0, 0, 2, 10);

	private static final Figure Neuf = new Figure("Neuf", 0, 14, 3, 15);

	private static final Figure Dix = new Figure("Dix", 10, 10, 7, 13);

	private static final Figure Valet = new Figure("Valet", 2, 20, 4, 16);

	private static final Figure Dame = new Figure("Dame", 3, 3, 5, 11);

	private static final Figure Roi = new Figure("Roi", 4, 4, 6, 12);

	private static final Figure As = new Figure("As", 11, 11, 8, 14);

	private static final Figure BlancC = new Figure("", 0, 0, 0, 0);

	Paquet() {
		unPaquet.add(new Carte(Pique, As));
		unPaquet.add(new Carte(Pique, Roi));
		unPaquet.add(new Carte(Pique, Dame));
		unPaquet.add(new Carte(Pique, Valet));
		unPaquet.add(new Carte(Pique, Dix));
		unPaquet.add(new Carte(Pique, Neuf));
		unPaquet.add(new Carte(Pique, Huit));
		unPaquet.add(new Carte(Pique, Sept));
		unPaquet.add(new Carte(Carreau, As));
		unPaquet.add(new Carte(Carreau, Roi));
		unPaquet.add(new Carte(Carreau, Dame));
		unPaquet.add(new Carte(Carreau, Valet));
		unPaquet.add(new Carte(Carreau, Dix));
		unPaquet.add(new Carte(Carreau, Neuf));
		unPaquet.add(new Carte(Carreau, Huit));
		unPaquet.add(new Carte(Carreau, Sept));
		unPaquet.add(new Carte(Trefle, As));
		unPaquet.add(new Carte(Trefle, Roi));
		unPaquet.add(new Carte(Trefle, Dame));
		unPaquet.add(new Carte(Trefle, Valet));
		unPaquet.add(new Carte(Trefle, Dix));
		unPaquet.add(new Carte(Trefle, Neuf));
		unPaquet.add(new Carte(Trefle, Huit));
		unPaquet.add(new Carte(Trefle, Sept));
		unPaquet.add(new Carte(Coeur, As));
		unPaquet.add(new Carte(Coeur, Roi));
		unPaquet.add(new Carte(Coeur, Dame));
		unPaquet.add(new Carte(Coeur, Valet));
		unPaquet.add(new Carte(Coeur, Dix));
		unPaquet.add(new Carte(Coeur, Neuf));
		unPaquet.add(new Carte(Coeur, Huit));
		unPaquet.add(new Carte(Coeur, Sept));
	}

	void bat() {
		int numero = 0;
		for (int i = 32; i > 0; i--) {
			numero = (int) (Math.random() * i);
			lePaquet.add((unPaquet.get(numero)).clone());
			unPaquet.remove(numero);

		}
	}

	void coupe(int n) {
		lePaquet.addAll(lePaquet.subList(n, 31));
		lePaquet.addAll(lePaquet.subList(0, n));
		for (int i = 0; i < 31; i++)
			lePaquet.remove(0);
	}

	void distribue(Joueur joueur, int nb) {
		for (int i = 0; i < nb; i++) {
			joueur.main.add(lePaquet.get(0).clone());
			lePaquet.remove(0);
		}
	}

	public Carte retourne() {
		Carte uneCarte = lePaquet.get(0).clone();
		lePaquet.remove(0);
		return uneCarte;
	}

}