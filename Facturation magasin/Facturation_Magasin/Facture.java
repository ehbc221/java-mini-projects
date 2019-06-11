import java.io.*;
/*
 * Facture.java
 *
 * Created on 16 novembre 2005, 18:26
 */

/**
 *
 * @author  Sébastien
 */
public abstract class Facture implements Serializable {
    protected String nom; protected String prenom;
    protected String adresse; protected String codePostal; protected String ville; protected String pays;
    protected String telephone; protected String fax; protected String numeroFacture;
    protected String[] numero = new String[20];
    protected String[] designation = new String[20];
    protected double[] qte = new double[20];
    protected double[] prixUnitaire = new double[20];
    protected double[] prixTTC = new double[20];
    
        
    /** Creates a new instance of Facture */
    public Facture(String nom, String prenom, 
    String adresse, String codePostal, String ville, String pays, String telephone, String fax, String numeroFacture,
    String[] numero, String[] designation, double[] qte, double[] prixUnitaire, double[] prixTTC) {
       
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
        this.telephone = telephone;
        this.fax = fax;
        this.numeroFacture = numeroFacture;
        
        //numero = new String[20];
        this.numero = numero;
        
        //designation = new String[20];
        this.designation = designation;
        
        //qte = new double[20];
        this.qte = qte;
        
        //prixUnitaire = new double[20];
        this.prixUnitaire = prixUnitaire;
        
        //prixTTC = new double[20];
        this.prixTTC = prixTTC;
        }
    public abstract double[] Calculer();
    
}
