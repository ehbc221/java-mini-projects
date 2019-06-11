package team.conso.data.bdd;

import javax.swing.ImageIcon;

/**
 * team.conso.data.bdd
 * 
 */
public class Produit extends DataElement
{
    public Produit( String lib, String im )
    {
        imagePath = im;
        libelle = lib;
        if ( im != null )
            icon = new ImageIcon( IMAGE_ROOT + imagePath );
    }
    
}
