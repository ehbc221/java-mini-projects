/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.controler.exceptions.PreexistingEntityException;
import com.bash.jcouture.entities.model.MouvementCompte;
import com.bash.jcouture.entities.model.MouvementComptePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.bash.jcouture.entities.model.Facture;
import com.bash.jcouture.entities.model.Compte;

/**
 *
 * @author bashizip
 */
public class MouvementCompteJpaController {

    public MouvementCompteJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MouvementCompte mouvementCompte) throws PreexistingEntityException, Exception {
        if (mouvementCompte.getMouvementComptePK() == null) {
            mouvementCompte.setMouvementComptePK(new MouvementComptePK());
        }
        mouvementCompte.getMouvementComptePK().setIdfacture(mouvementCompte.getFacture().getIdfacture());
        mouvementCompte.getMouvementComptePK().setIdcompte(mouvementCompte.getCompte().getIdcompte());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facture facture = mouvementCompte.getFacture();
            if (facture != null) {
                facture = em.getReference(facture.getClass(), facture.getIdfacture());
                mouvementCompte.setFacture(facture);
            }
            Compte compte = mouvementCompte.getCompte();
            if (compte != null) {
                compte = em.getReference(compte.getClass(), compte.getIdcompte());
                mouvementCompte.setCompte(compte);
            }
            em.persist(mouvementCompte);
            if (facture != null) {
                facture.getMouvementCompteList().add(mouvementCompte);
                facture = em.merge(facture);
            }
            if (compte != null) {
                compte.getMouvementCompteList().add(mouvementCompte);
                compte = em.merge(compte);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMouvementCompte(mouvementCompte.getMouvementComptePK()) != null) {
                throw new PreexistingEntityException("MouvementCompte " + mouvementCompte + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MouvementCompte mouvementCompte) throws NonexistentEntityException, Exception {
        mouvementCompte.getMouvementComptePK().setIdfacture(mouvementCompte.getFacture().getIdfacture());
        mouvementCompte.getMouvementComptePK().setIdcompte(mouvementCompte.getCompte().getIdcompte());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MouvementCompte persistentMouvementCompte = em.find(MouvementCompte.class, mouvementCompte.getMouvementComptePK());
            Facture factureOld = persistentMouvementCompte.getFacture();
            Facture factureNew = mouvementCompte.getFacture();
            Compte compteOld = persistentMouvementCompte.getCompte();
            Compte compteNew = mouvementCompte.getCompte();
            if (factureNew != null) {
                factureNew = em.getReference(factureNew.getClass(), factureNew.getIdfacture());
                mouvementCompte.setFacture(factureNew);
            }
            if (compteNew != null) {
                compteNew = em.getReference(compteNew.getClass(), compteNew.getIdcompte());
                mouvementCompte.setCompte(compteNew);
            }
            mouvementCompte = em.merge(mouvementCompte);
            if (factureOld != null && !factureOld.equals(factureNew)) {
                factureOld.getMouvementCompteList().remove(mouvementCompte);
                factureOld = em.merge(factureOld);
            }
            if (factureNew != null && !factureNew.equals(factureOld)) {
                factureNew.getMouvementCompteList().add(mouvementCompte);
                factureNew = em.merge(factureNew);
            }
            if (compteOld != null && !compteOld.equals(compteNew)) {
                compteOld.getMouvementCompteList().remove(mouvementCompte);
                compteOld = em.merge(compteOld);
            }
            if (compteNew != null && !compteNew.equals(compteOld)) {
                compteNew.getMouvementCompteList().add(mouvementCompte);
                compteNew = em.merge(compteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MouvementComptePK id = mouvementCompte.getMouvementComptePK();
                if (findMouvementCompte(id) == null) {
                    throw new NonexistentEntityException("The mouvementCompte with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MouvementComptePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MouvementCompte mouvementCompte;
            try {
                mouvementCompte = em.getReference(MouvementCompte.class, id);
                mouvementCompte.getMouvementComptePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mouvementCompte with id " + id + " no longer exists.", enfe);
            }
            Facture facture = mouvementCompte.getFacture();
            if (facture != null) {
                facture.getMouvementCompteList().remove(mouvementCompte);
                facture = em.merge(facture);
            }
            Compte compte = mouvementCompte.getCompte();
            if (compte != null) {
                compte.getMouvementCompteList().remove(mouvementCompte);
                compte = em.merge(compte);
            }
            em.remove(mouvementCompte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MouvementCompte> findMouvementCompteEntities() {
        return findMouvementCompteEntities(true, -1, -1);
    }

    public List<MouvementCompte> findMouvementCompteEntities(int maxResults, int firstResult) {
        return findMouvementCompteEntities(false, maxResults, firstResult);
    }

    private List<MouvementCompte> findMouvementCompteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from MouvementCompte as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MouvementCompte findMouvementCompte(MouvementComptePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MouvementCompte.class, id);
        } finally {
            em.close();
        }
    }

    public int getMouvementCompteCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from MouvementCompte as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
