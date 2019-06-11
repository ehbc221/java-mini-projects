package team.conso.data.bdd;

import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * team.conso.data.bdd
 * 
 */
public abstract class DataElement implements Serializable
{
    public static String IMAGE_ROOT = "images//food//";
    protected String imagePath = "";
    protected String libelle = "";
    protected ImageIcon icon = null;
    protected int status = 0;
    public boolean equal( DataElement elt )
    {
        if ( elt == null ) return false;
        if ( libelle.equals( elt.libelle ) )
            return true;
        return false;
    }
    public String getLibelle()
    {
        return libelle;
    }
    public void setLibelle( String libelle )
    {
        this.libelle = libelle;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus( int status )
    {
        this.status = status;
    }
    public ImageIcon getIcon()
    {
        return icon;
    }
    public void setIcon( ImageIcon icon )
    {
        this.icon = icon;
    }
}
