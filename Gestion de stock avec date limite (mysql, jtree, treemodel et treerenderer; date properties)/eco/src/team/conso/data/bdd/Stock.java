package team.conso.data.bdd;

/**
 * team.conso.data.bdd
 * 
 */
public class Stock extends DataElement
{
    String codeRef = "";
    String date = "";
    String dateLimit = "";
    String nombre = "";
    String prod = "";
    String libProd = "";
    String prix = "";
    String nbDep = "";
    public Stock( String date, String dateL, String nombre, String ref, String prix )
    {
        imagePath = null;
        codeRef = ref;
        setNombre( nombre );
        setDate( date );
        setDateLimit( dateL );
        setPrix( prix );
    }
    public String getDate()
    {
        return date;
    }
    public void setDate( String date )
    {
        this.date = date;
        
    }
    public String getDateLimit()
    {
        return dateLimit;
    }
    
    public void setDateLimit( String dateLimit )
    {
        this.dateLimit = dateLimit;
        modifyLibelle();
    }
    
    private void modifyLibelle()
    {
        libelle = " " + nombre + " - " + dateLimit;
    }
    
    public String getNombre()
    {
        return nombre;
    }
    public void setNombre( String nombre )
    {
        this.nombre = nombre;
        modifyLibelle();
    }
    public String getCodeRef()
    {
        return codeRef;
    }
    public void setCodeRef( String codeRef )
    {
        this.codeRef = codeRef;
    }
    public String getLibProd()
    {
        return libProd;
    }
    public void setLibProd( String libProd )
    {
        this.libProd = libProd;
    }
    /**
     * @return
     */
    public String getPrix()
    {
        return prix;
    }
    public void setPrix(String prix)
    {
        this.prix = prix;
    }
    /**
     * @param string
     */
    public void setNBDep( String s )
    {
        nbDep = s;
    }
    
    public String getNbDep()
    {
        return nbDep;
    }
    public void setNbDep(String nbDep)
    {
        this.nbDep = nbDep;
    }
}
