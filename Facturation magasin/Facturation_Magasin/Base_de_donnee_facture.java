import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.text.*;    
import java.util.*;
/*
 * Base_de_donnee_facture.java
 *
 * Created on 17 novembre 2005, 08:50
 */

/**
 *
 * @author  Sébastien
 */
public class Base_de_donnee_facture implements Serializable {
    private ArrayList baseDeDonneeFacture;
    
    /** Creates a new instance of Base_de_donnee_facture */
    public Base_de_donnee_facture() {
        baseDeDonneeFacture = new ArrayList();
    }
    
    public boolean Enregistrer(Facture nouveau, String recherche) {
        Facture enregistrer;
        ListIterator index = baseDeDonneeFacture.listIterator();
        
        while(index.hasNext()){
            enregistrer = (Facture)index.next();
            if(enregistrer.numeroFacture.equals(recherche)){
                return(false);
            }
        }
        
        baseDeDonneeFacture.add(nouveau);
        return(true);
    }
    
    public boolean Supprimer(String recherche) {
        Facture supprimer;
        ListIterator index = baseDeDonneeFacture.listIterator();
        
        while(index.hasNext()){
            supprimer = (Facture)index.next();
            if(supprimer.numeroFacture.equals(recherche)){
                index.remove();
                return(true);
            }
        }
        return(false);
    }
    
    public Facture Parcourir() {
        Facture nouveau;
        ListIterator index = baseDeDonneeFacture.listIterator();
        //Iterator index = baseDeDonneeFacture.iterator();  
        while(index.hasNext()){
            nouveau = (Facture)index.next();
            return(nouveau);
            //nouveau.Calculer();
        }
        return(null);
    }
    
    public Facture Parcourir_pas_a_pas(int i){
        Facture parcourir;
        ListIterator index = baseDeDonneeFacture.listIterator(i);
        if(index.hasNext()){
            parcourir = (Facture)index.next();
            return(parcourir);
        }
        return(null);
        /**else
        {
            return(null);
        }*/
    }
    
    public ArrayList rechercher(String recherche, String typeRecherche, String type){
        ArrayList tableRechercheFacture = new ArrayList();
        Facture rechercheFacture;
        ListIterator index = baseDeDonneeFacture.listIterator();
        
        while(index.hasNext()){
            rechercheFacture = (Facture)index.next();
            if(rechercheFacture.getClass().getName().equals(type)){
                if(typeRecherche == "Nom"){
                    if(rechercheFacture.nom.equals(recherche)){
                        tableRechercheFacture.add(rechercheFacture);
                    }
                }
                else if(typeRecherche == "Prénom"){
                    if(rechercheFacture.prenom.equals(recherche)){
                        tableRechercheFacture.add(rechercheFacture);
                    }
                }
                else if(typeRecherche == "Numéro facture"){
                    if(rechercheFacture.numeroFacture.equals(recherche)){
                        tableRechercheFacture.add(rechercheFacture);
                    }
                }
            }
        }
        return(tableRechercheFacture);
    }
    
    /**
     * Getter for property baseDeDonneeFacture.
     * @return Value of property baseDeDonneeFacture.
     */
    public java.util.ArrayList getBaseDeDonneeFacture() {
        return baseDeDonneeFacture;
    }
    
    /**
     * Setter for property baseDeDonneeFacture.
     * @param baseDeDonneeFacture New value of property baseDeDonneeFacture.
     */
    public void setBaseDeDonneeFacture(java.util.ArrayList baseDeDonneeFacture) {
        this.baseDeDonneeFacture = baseDeDonneeFacture;
    }
    
    public Exception openBase(String nomFichier){
        try{
            // Definition du fichier de lecture et du flux
            FileInputStream fileInput = new FileInputStream(nomFichier);
            ObjectInputStream input = new ObjectInputStream(fileInput);
            
            // recuperation de la valeur
            ArrayList liste = (ArrayList) input.readObject();
            baseDeDonneeFacture = liste;
            input.close();
        }
        catch(ClassNotFoundException e1){
            return(e1);
        }
        catch(IOException e2){
            return(e2);
        }
        return(null);
        
    }
    
    public Exception saveBase(String nomFichier) {
        try
        {
            /* Ouverture et ecriture du fichier de sauvegarde */
            FileOutputStream fileOutput = new FileOutputStream(nomFichier);
            // creation du flux
            ObjectOutputStream output = new ObjectOutputStream(fileOutput);
            // ecriture
            output.writeObject(baseDeDonneeFacture);//laBase.getBase().get(0));
            output.flush();
            output.close();
        }
        catch(IOException e)
        {
            return(e);
        }
        return(null);
    }
    
}
