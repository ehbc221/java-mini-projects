package jeux.enumerations;
public enum Lancements
{Bienvenue_dans_les_jeux_de_cartes;
	public String toString()
	{
		return name().replace(jeux.Lettre._,jeux.Lettre.es);
	}
}