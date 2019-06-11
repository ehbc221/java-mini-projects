import java.io.*;
/*
 * Facture_Export.java
 *
 * Created on 17 novembre 2005, 08:34
 */

/**
 *
 * @author  Sébastien
 */
public class Facture_Export extends Facture implements Serializable {
    private double remise;
    private double total_TTC;
    private double total_TTC_Apres;
    private double valeur_TVA;
    private double total_HT;
    /** Creates a new instance of Facture_Export */
    public Facture_Export(String nom, String prenom, 
    String adresse, String codePostal, String ville, String pays, String telephone, String fax, String numeroFacture,
    String[] numero, String[] designation, double[] qte, double[] prixUnitaire, double[] prixTTC,
    double remise, double total_TTC, double total_TTC_Apres, double valeur_TVA, double total_HT) {
        super(nom, prenom, 
    adresse, codePostal, ville, pays, telephone, fax, numeroFacture, 
    numero, designation, qte, prixUnitaire, prixTTC);
        
        this.remise = remise;
        this.total_TTC = total_TTC;
        this.total_TTC_Apres = total_TTC_Apres;
        this.valeur_TVA = valeur_TVA;
        this.total_HT = total_HT;
    }
    
    public double[] Calculer() {
        double[] calcul = new double[24];
        int increment = 0;
        for(increment = 0; increment < 20; increment++){
            calcul[increment] = qte[increment] * prixUnitaire[increment];
        }
        // Calcul prix TTC avant remise
        calcul[20] = calcul[0] + calcul[1] + calcul[2] + calcul[3] + calcul[4] + 
                    calcul[5] + calcul[6] + calcul[7] + calcul[8] + calcul[9] + 
                    calcul[10] + calcul[11] + calcul[12] + calcul[13] + calcul[14] + 
                    calcul[15] + calcul[16] + calcul[17] + calcul[18] + calcul[19];
        
        // Calcul prix TTC aprés remise
        calcul[21] = calcul[20] - (calcul[20] * (remise / 100.0));
        
        // Calcul TVA
        calcul[22] = calcul[21] * 0.1639;
        
        // Calcul prix HT
        calcul[23] = calcul[21] - calcul[22];
        
         
        return(calcul);
    }
    
    /**
     * Getter for property remise.
     * @return Value of property remise.
     */
    public double getRemise() {
        return remise;
    }
    
    /**
     * Setter for property remise.
     * @param remise New value of property remise.
     */
    public void setRemise(double remise) {
        this.remise = remise;
    }
    
    /**
     * Getter for property total_TTC.
     * @return Value of property total_TTC.
     */
    public double getTotal_TTC() {
        return total_TTC;
    }
    
    /**
     * Setter for property total_TTC.
     * @param total_TTC New value of property total_TTC.
     */
    public void setTotal_TTC(double total_TTC) {
        this.total_TTC = total_TTC;
    }
    
    /**
     * Getter for property total_TTC_Apres.
     * @return Value of property total_TTC_Apres.
     */
    public double getTotal_TTC_Apres() {
        return total_TTC_Apres;
    }
    
    /**
     * Setter for property total_TTC_Apres.
     * @param total_TTC_Apres New value of property total_TTC_Apres.
     */
    public void setTotal_TTC_Apres(double total_TTC_Apres) {
        this.total_TTC_Apres = total_TTC_Apres;
    }
    
    /**
     * Getter for property valeur_TVA.
     * @return Value of property valeur_TVA.
     */
    public double getValeur_TVA() {
        return valeur_TVA;
    }
    
    /**
     * Setter for property valeur_TVA.
     * @param valeur_TVA New value of property valeur_TVA.
     */
    public void setValeur_TVA(double valeur_TVA) {
        this.valeur_TVA = valeur_TVA;
    }
    
    /**
     * Getter for property total_HT.
     * @return Value of property total_HT.
     */
    public double getTotal_HT() {
        return total_HT;
    }
    
    /**
     * Setter for property total_HT.
     * @param total_HT New value of property total_HT.
     */
    public void setTotal_HT(double total_HT) {
        this.total_HT = total_HT;
    }
    
}
