/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.Article;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.bash.jcouture.entities.model.CategorieArticle;
import com.bash.jcouture.entities.model.Mesure;
import com.bash.jcouture.entities.model.TypeArticle;
import com.bash.jcouture.entities.model.Client;

/**
 *
 * @author bashizip
 */
public class ArticleJpaController {

    public ArticleJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Article article) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategorieArticle categorieArticle = article.getCategorieArticle();
            if (categorieArticle != null) {
                categorieArticle = em.getReference(categorieArticle.getClass(), categorieArticle.getIdcategorie());
                article.setCategorieArticle(categorieArticle);
            }
            Mesure mesure = article.getMesure();
            if (mesure != null) {
                mesure = em.getReference(mesure.getClass(), mesure.getIdmesure());
                article.setMesure(mesure);
            }
            TypeArticle typeArticle = article.getTypeArticle();
            if (typeArticle != null) {
                typeArticle = em.getReference(typeArticle.getClass(), typeArticle.getIdtypeArticle());
                article.setTypeArticle(typeArticle);
            }
            Client client = article.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getIdclient());
                article.setClient(client);
            }
            em.persist(article);
            if (categorieArticle != null) {
                categorieArticle.getArticleList().add(article);
                categorieArticle = em.merge(categorieArticle);
            }
            if (mesure != null) {
                mesure.getArticleList().add(article);
                mesure = em.merge(mesure);
            }
            if (typeArticle != null) {
                typeArticle.getArticleList().add(article);
                typeArticle = em.merge(typeArticle);
            }
            if (client != null) {
                client.getArticleList().add(article);
                client = em.merge(client);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Article article) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article persistentArticle = em.find(Article.class, article.getIdarticle());
            CategorieArticle categorieArticleOld = persistentArticle.getCategorieArticle();
            CategorieArticle categorieArticleNew = article.getCategorieArticle();
            Mesure mesureOld = persistentArticle.getMesure();
            Mesure mesureNew = article.getMesure();
            TypeArticle typeArticleOld = persistentArticle.getTypeArticle();
            TypeArticle typeArticleNew = article.getTypeArticle();
            Client clientOld = persistentArticle.getClient();
            Client clientNew = article.getClient();
            if (categorieArticleNew != null) {
                categorieArticleNew = em.getReference(categorieArticleNew.getClass(), categorieArticleNew.getIdcategorie());
                article.setCategorieArticle(categorieArticleNew);
            }
            if (mesureNew != null) {
                mesureNew = em.getReference(mesureNew.getClass(), mesureNew.getIdmesure());
                article.setMesure(mesureNew);
            }
            if (typeArticleNew != null) {
                typeArticleNew = em.getReference(typeArticleNew.getClass(), typeArticleNew.getIdtypeArticle());
                article.setTypeArticle(typeArticleNew);
            }
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getIdclient());
                article.setClient(clientNew);
            }
            article = em.merge(article);
            if (categorieArticleOld != null && !categorieArticleOld.equals(categorieArticleNew)) {
                categorieArticleOld.getArticleList().remove(article);
                categorieArticleOld = em.merge(categorieArticleOld);
            }
            if (categorieArticleNew != null && !categorieArticleNew.equals(categorieArticleOld)) {
                categorieArticleNew.getArticleList().add(article);
                categorieArticleNew = em.merge(categorieArticleNew);
            }
            if (mesureOld != null && !mesureOld.equals(mesureNew)) {
                mesureOld.getArticleList().remove(article);
                mesureOld = em.merge(mesureOld);
            }
            if (mesureNew != null && !mesureNew.equals(mesureOld)) {
                mesureNew.getArticleList().add(article);
                mesureNew = em.merge(mesureNew);
            }
            if (typeArticleOld != null && !typeArticleOld.equals(typeArticleNew)) {
                typeArticleOld.getArticleList().remove(article);
                typeArticleOld = em.merge(typeArticleOld);
            }
            if (typeArticleNew != null && !typeArticleNew.equals(typeArticleOld)) {
                typeArticleNew.getArticleList().add(article);
                typeArticleNew = em.merge(typeArticleNew);
            }
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getArticleList().remove(article);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getArticleList().add(article);
                clientNew = em.merge(clientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = article.getIdarticle();
                if (findArticle(id) == null) {
                    throw new NonexistentEntityException("The article with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article article;
            try {
                article = em.getReference(Article.class, id);
                article.getIdarticle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The article with id " + id + " no longer exists.", enfe);
            }
            CategorieArticle categorieArticle = article.getCategorieArticle();
            if (categorieArticle != null) {
                categorieArticle.getArticleList().remove(article);
                categorieArticle = em.merge(categorieArticle);
            }
            Mesure mesure = article.getMesure();
            if (mesure != null) {
                mesure.getArticleList().remove(article);
                mesure = em.merge(mesure);
            }
            TypeArticle typeArticle = article.getTypeArticle();
            if (typeArticle != null) {
                typeArticle.getArticleList().remove(article);
                typeArticle = em.merge(typeArticle);
            }
            Client client = article.getClient();
            if (client != null) {
                client.getArticleList().remove(article);
                client = em.merge(client);
            }
            em.remove(article);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Article> findArticleEntities() {
        return findArticleEntities(true, -1, -1);
    }

    public List<Article> findArticleEntities(int maxResults, int firstResult) {
        return findArticleEntities(false, maxResults, firstResult);
    }

    private List<Article> findArticleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Article as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Article findArticle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Article.class, id);
        } finally {
            em.close();
        }
    }

      public List<Article> findUnFinishedArticleEntities() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Article as o where o.etat =:etat order by o.dateEntree");
            q.setParameter("etat", "En Conception");
            // Query q = em.createNativeQuery("select * from Article where Etat ='En Conception'");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    public int getArticleCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Article as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
