package team.conso.data.bdd;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;


/**
 * filtre de donne pour les requete a la bdd
 */
public class DataFilter implements Serializable
{
    // par defaut ausun filtrage dc 
    Vector cate = null; // new Vector();
    Vector prod = null; //new Vector();
    boolean valide = false;
    boolean alarme = false;
    boolean perime = false;
    Calendar dateDeb = null; // date de limite de consomation
    Calendar dateFin = null;
    
    /**
     * 
     */
    public DataFilter()
    {
        super();
    }
    
    public DataFilter( Vector cate, Vector prod, boolean vali, boolean alar, boolean peri )
    {
        this( cate, prod, vali, alar, peri, null, null );
    }
    public DataFilter( Vector cate, Vector prod, boolean vali, boolean alar, boolean peri, Calendar db, Calendar df )
    {
        this();
        setCate( cate );
        setProd( prod );
        setValide( vali );
        setAlarme( alar );
        setPerime( peri );
        setDateDeb( db );
        setDateFin( df );
    }

    public boolean isAlarme()
    {
        return alarme;
    }
    public void setAlarme(boolean alarme)
    {
        this.alarme = alarme;
    }
    public Vector getCate()
    {
        return cate;
    }
    public void setCate(Vector cate)
    {
        this.cate = cate;
    }
    public Calendar getDateDeb()
    {
        return dateDeb;
    }
    public void setDateDeb(Calendar dateDeb)
    {
        this.dateDeb = dateDeb;
    }
    public Calendar getDateFin()
    {
        return dateFin;
    }
    public void setDateFin(Calendar dateFin)
    {
        this.dateFin = dateFin;
    }
    public boolean isPerime()
    {
        return perime;
    }
    public void setPerime(boolean perime)
    {
        this.perime = perime;
    }
    public Vector getProd()
    {
        return prod;
    }
    public void setProd(Vector prod)
    {
        this.prod = prod;
    }
    public boolean isValide()
    {
        return valide;
    }
    public void setValide(boolean valide)
    {
        this.valide = valide;
    }
    
    /**
     * renvoi les cate separer par une virgule et entre cote
     * @return
     */
    public String getCateValues()
    {
        return getString( cate );
    }
    
    /**
     * renvoi les prod separer par une virgule et entre cote
     * @return
     */
    public String getProdValues()
    {
        return getString( prod );
    }
    
    private String getString( Vector v )
    {
        if ( v == null ) return null;
        String result = "";
        for (Iterator iter = v.iterator(); iter.hasNext();)
        {
            String cat = (String) iter.next();
            result += ",'" + cat + "'";
        }
        return result.substring( 1 );
    }
    
}
