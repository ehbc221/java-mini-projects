package jeux.enumerations;
import jeux.*;
/**Miseres utilisees au tarot*/
public enum Miseres
{Atout,Tete,Figure,Couleur,Cartes_basses;
	public String toString() {
		return name().replace(Lettre._,Lettre.es);
	}
}