package fr.julien.chat.lancement;

import fr.julien.chat.ihm.CreationUtilisateur;
import fr.julien.chat.util.FichierConversation;

public class Lanceur {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FichierConversation();
		new CreationUtilisateur();
	}

}
