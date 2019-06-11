/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "article", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a"),
    @NamedQuery(name = "Article.findByIdarticle", query = "SELECT a FROM Article a WHERE a.idarticle = :idarticle"),
    @NamedQuery(name = "Article.findByDateEntree", query = "SELECT a FROM Article a WHERE a.dateEntree = :dateEntree"),
    @NamedQuery(name = "Article.findByDateSortie", query = "SELECT a FROM Article a WHERE a.dateSortie = :dateSortie"),
    @NamedQuery(name = "Article.findByEtat", query = "SELECT a FROM Article a WHERE a.etat = :etat"),
    @NamedQuery(name = "Article.findByPriorite", query = "SELECT a FROM Article a WHERE a.priorite = :priorite")})
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "idarticle", nullable = false)
    private Integer idarticle;
    @Column(name = "date_entree")
    @Temporal(TemporalType.DATE)
    private Date dateEntree;
    @Column(name = "date_sortie")
    @Temporal(TemporalType.DATE)
    private Date dateSortie;
    @Column(name = "etat", length = 45)
    private String etat;
    @Lob
    @Column(name = "commentaire", length = 65535)
    private String commentaire;
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @Column(name = "priorite")
    private Integer priorite;
    @JoinColumn(name = "idcategorie", referencedColumnName = "idcategorie", nullable = false)
    @ManyToOne(optional = false)
    private CategorieArticle categorieArticle;
    @JoinColumn(name = "idmesure", referencedColumnName = "idmesure", nullable = false)
    @ManyToOne(optional = false)
    private Mesure mesure;
    @JoinColumn(name = "idtype_article", referencedColumnName = "idtype_article", nullable = false)
    @ManyToOne(optional = false)
    private TypeArticle typeArticle;
    @JoinColumn(name = "idmodele", referencedColumnName = "idmodele", nullable = false)
    @ManyToOne(optional = false)
    private Modele modele;
    @JoinColumn(name = "idclient", referencedColumnName = "idclient", nullable = false)
    @ManyToOne(optional = false)
    private Client client;
    @JoinColumn(name = "idfacture", referencedColumnName = "idfacture")
    @ManyToOne
    private Facture facture;

    public Article() {
    }

    public Article(Integer idarticle) {
        this.idarticle = idarticle;
    }

    public Integer getIdarticle() {
        return idarticle;
    }

    public void setIdarticle(Integer idarticle) {
        this.idarticle = idarticle;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Integer getPriorite() {
        return priorite;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public CategorieArticle getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(CategorieArticle categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public Mesure getMesure() {
        return mesure;
    }

    public void setMesure(Mesure mesure) {
        this.mesure = mesure;
    }

    public TypeArticle getTypeArticle() {
        return typeArticle;
    }

    public void setTypeArticle(TypeArticle typeArticle) {
        this.typeArticle = typeArticle;
    }

    public Modele getModele() {
        return modele;
    }

    public void setModele(Modele modele) {
        this.modele = modele;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarticle != null ? idarticle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.idarticle == null && other.idarticle != null) || (this.idarticle != null && !this.idarticle.equals(other.idarticle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.Article[idarticle=" + idarticle + "]";
    }

}
