package jeux.enumerations;
import jeux.*;
/** Choix du tarot pour l'entrainement*/
public enum ChoixTarot
{
Petit_a_sauver,Petit_a_chasser,Petit_a_emmener_au_bout;
	public String toString() {
		return name().replace(new String(new char[]{Lettre._,Lettre.a,Lettre._}),new String(new char[]{Lettre.es,Lettre.ag,Lettre.es})).replace(Lettre._, Lettre.es);
	}
}