/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author Ibrahima
 */
public class Commander {

    private int id_commande;
    private String nom_article;
    private String categorie;
    private int quantite;
    //Accesseurs

    public int getId_Commande() {
        return this.id_commande;
    }

    public String getNom_Article() {
        return this.nom_article;
    }

    public String get_Categorie() {
        return this.categorie;
    }

    public int getQuantite() {
        return this.quantite;
    }
    //Modificateurs

    public void setID_Commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public void setNom_Article(String nom_article) {
        this.nom_article = nom_article;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
