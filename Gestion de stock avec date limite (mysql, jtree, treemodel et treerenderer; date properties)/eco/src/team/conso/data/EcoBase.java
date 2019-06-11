
package team.conso.data;

import java.util.Hashtable;
import java.util.Vector;

/**
 * team.conso.data
 * 
 */
public interface EcoBase
{
    public Hashtable getCategories();
    public Hashtable getProduits();
    public Vector getProduits( String categorie );
    
}
