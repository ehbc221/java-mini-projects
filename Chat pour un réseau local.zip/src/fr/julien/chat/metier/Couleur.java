package fr.julien.chat.metier;


public enum Couleur {
	NOIR,
	BLEU,
	ROUGE,
	VERT
	;
	
	public String getValeur(){
		String res;
		switch (this) {
		case NOIR:
			res = "black";
			break;
		case BLEU:
			res = "blue";
			break;
		case ROUGE:
			res = "red";
			break;
		case VERT:
			res = "green";
			break;
		default:
			res = "black";
		}
		return res;
	}

}
