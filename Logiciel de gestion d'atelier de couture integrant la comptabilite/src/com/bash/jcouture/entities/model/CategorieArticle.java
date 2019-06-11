/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.entities.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author bashizip
 */
@Entity
@Table(name = "categorie_article", catalog = "couturedb", schema = "")
@NamedQueries({
    @NamedQuery(name = "CategorieArticle.findAll", query = "SELECT c FROM CategorieArticle c"),
    @NamedQuery(name = "CategorieArticle.findByIdcategorie", query = "SELECT c FROM CategorieArticle c WHERE c.idcategorie = :idcategorie"),
    @NamedQuery(name = "CategorieArticle.findByLabel", query = "SELECT c FROM CategorieArticle c WHERE c.label = :label")})
public class CategorieArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcategorie", nullable = false)
    private Integer idcategorie;
    @Basic(optional = false)
    @Column(name = "label", nullable = false, length = 45)
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categorieArticle")
    private List<Article> articleList;

    public CategorieArticle() {
    }

    public CategorieArticle(Integer idcategorie) {
        this.idcategorie = idcategorie;
    }

    public CategorieArticle(Integer idcategorie, String label) {
        this.idcategorie = idcategorie;
        this.label = label;
    }

    public Integer getIdcategorie() {
        return idcategorie;
    }

    public void setIdcategorie(Integer idcategorie) {
        this.idcategorie = idcategorie;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcategorie != null ? idcategorie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategorieArticle)) {
            return false;
        }
        CategorieArticle other = (CategorieArticle) object;
        if ((this.idcategorie == null && other.idcategorie != null) || (this.idcategorie != null && !this.idcategorie.equals(other.idcategorie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bash.jcouture.entities.model.CategorieArticle[idcategorie=" + idcategorie + "]";
    }

}
