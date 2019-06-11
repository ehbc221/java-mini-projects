/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.Mesure;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author bashizip
 */
public class MesureJpaController {

    public MesureJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mesure mesure) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(mesure);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mesure mesure) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mesure = em.merge(mesure);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mesure.getIdmesure();
                if (findMesure(id) == null) {
                    throw new NonexistentEntityException("The mesure with id " + id + " no longer exists.");
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
            Mesure mesure;
            try {
                mesure = em.getReference(Mesure.class, id);
                mesure.getIdmesure();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mesure with id " + id + " no longer exists.", enfe);
            }
            em.remove(mesure);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mesure> findMesureEntities() {
        return findMesureEntities(true, -1, -1);
    }

    public List<Mesure> findMesureEntities(int maxResults, int firstResult) {
        return findMesureEntities(false, maxResults, firstResult);
    }

    private List<Mesure> findMesureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Mesure as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Mesure findMesure(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mesure.class, id);
        } finally {
            em.close();
        }
    }

    public int getMesureCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Mesure as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
