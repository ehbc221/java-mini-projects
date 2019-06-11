package fr.julien.chat.metier;

public class Message implements ISerialisableHTML {
	
	private Utilisateur utilisateur;
	private String contenu;
	
	
	/**
	 * @param utilisateur
	 * @param contenu
	 */
	public Message(Utilisateur utilisateur, String contenu) {
		super();
		this.utilisateur = utilisateur;
		this.contenu = contenu;
	}
	
	public String toHTML(){
		return "<font color=\""+utilisateur.getCouleur().getValeur()+"\"><b>"+utilisateur.getPseudo()+" dit : </b>"+contenu.replaceAll("\n", "")+"</font><br />\n";
	}

}
