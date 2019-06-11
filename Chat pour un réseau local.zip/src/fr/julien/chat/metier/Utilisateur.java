package fr.julien.chat.metier;


public class Utilisateur {
	
	private String pseudo;
	private Couleur couleur;

	/**
	 * @param pseudo
	 */
	public Utilisateur(String pseudo, Couleur couleur) {
		super();
		this.pseudo = pseudo;
		this.couleur = couleur;
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @return the couleur
	 */
	public Couleur getCouleur() {
		return couleur;
	}

}
