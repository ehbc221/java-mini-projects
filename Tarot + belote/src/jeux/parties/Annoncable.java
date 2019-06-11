package jeux.parties;
import java.util.*;
import jeux.encheres.*;
public interface Annoncable extends Levable {
	boolean autorise_annonce(Annonce a,byte numero);
	public Vector<Annonce> getAnnoncesPossibles(byte numero);
}