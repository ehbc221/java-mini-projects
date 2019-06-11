/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.controler;

import com.bash.jcouture.controler.exceptions.IllegalOrphanException;
import com.bash.jcouture.controler.exceptions.NonexistentEntityException;
import com.bash.jcouture.entities.model.Facture;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.bash.jcouture.entities.model.PayementMode;
import com.bash.jcouture.entities.model.MouvementCompte;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bashizip
 */
public class FactureJpaController {

    public FactureJpaController() {
        emf = Persistence.createEntityManagerFactory("JCouturePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facture facture) {
        if (facture.getMouvementCompteList() == null) {
            facture.setMouvementCompteList(new ArrayList<MouvementCompte>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PayementMode payementMode = facture.getPayementMode();
            if (payementMode != null) {
                payementMode = em.getReference(payementMode.getClass(), payementMode.getIdpayementMode());
                facture.setPayementMode(payementMode);
            }
            List<MouvementCompte> attachedMouvementCompteList = new ArrayList<MouvementCompte>();
            for (MouvementCompte mouvementCompteListMouvementCompteToAttach : facture.getMouvementCompteList()) {
                mouvementCompteListMouvementCompteToAttach = em.getReference(mouvementCompteListMouvementCompteToAttach.getClass(), mouvementCompteListMouvementCompteToAttach.getMouvementComptePK());
                attachedMouvementCompteList.add(mouvementCompteListMouvementCompteToAttach);
            }
            facture.setMouvementCompteList(attachedMouvementCompteList);
            em.persist(facture);
            if (payementMode != null) {
                payementMode.getFactureList().add(facture);
                payementMode = em.merge(payementMode);
            }
            for (MouvementCompte mouvementCompteListMouvementCompte : facture.getMouvementCompteList()) {
                Facture oldFactureOfMouvementCompteListMouvementCompte = mouvementCompteListMouvementCompte.getFacture();
                mouvementCompteListMouvementCompte.setFacture(facture);
                mouvementCompteListMouvementCompte = em.merge(mouvementCompteListMouvementCompte);
                if (oldFactureOfMouvementCompteListMouvementCompte != null) {
                    oldFactureOfMouvementCompteListMouvementCompte.getMouvementCompteList().remove(mouvementCompteListMouvementCompte);
                    oldFactureOfMouvementCompteListMouvementCompte = em.merge(oldFactureOfMouvementCompteListMouvementCompte);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facture facture) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facture persistentFacture = em.find(Facture.class, facture.getIdfacture());
            PayementMode payementModeOld = persistentFacture.getPayementMode();
            PayementMode payementModeNew = facture.getPayementMode();
            List<MouvementCompte> mouvementCompteListOld = persistentFacture.getMouvementCompteList();
            List<MouvementCompte> mouvementCompteListNew = facture.getMouvementCompteList();
            List<String> illegalOrphanMessages = null;
            for (MouvementCompte mouvementCompteListOldMouvementCompte : mouvementCompteListOld) {
                if (!mouvementCompteListNew.contains(mouvementCompteListOldMouvementCompte)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MouvementCompte " + mouvementCompteListOldMouvementCompte + " since its facture field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (payementModeNew != null) {
                payementModeNew = em.getReference(payementModeNew.getClass(), payementModeNew.getIdpayementMode());
                facture.setPayementMode(payementModeNew);
            }
            List<MouvementCompte> attachedMouvementCompteListNew = new ArrayList<MouvementCompte>();
            for (MouvementCompte mouvementCompteListNewMouvementCompteToAttach : mouvementCompteListNew) {
                mouvementCompteListNewMouvementCompteToAttach = em.getReference(mouvementCompteListNewMouvementCompteToAttach.getClass(), mouvementCompteListNewMouvementCompteToAttach.getMouvementComptePK());
                attachedMouvementCompteListNew.add(mouvementCompteListNewMouvementCompteToAttach);
            }
            mouvementCompteListNew = attachedMouvementCompteListNew;
            facture.setMouvementCompteList(mouvementCompteListNew);
            facture = em.merge(facture);
            if (payementModeOld != null && !payementModeOld.equals(payementModeNew)) {
                payementModeOld.getFactureList().remove(facture);
                payementModeOld = em.merge(payementModeOld);
            }
            if (payementModeNew != null && !payementModeNew.equals(payementModeOld)) {
                payementModeNew.getFactureList().add(facture);
                payementModeNew = em.merge(payementModeNew);
            }
            for (MouvementCompte mouvementCompteListNewMouvementCompte : mouvementCompteListNew) {
                if (!mouvementCompteListOld.contains(mouvementCompteListNewMouvementCompte)) {
                    Facture oldFactureOfMouvementCompteListNewMouvementCompte = mouvementCompteListNewMouvementCompte.getFacture();
                    mouvementCompteListNewMouvementCompte.setFacture(facture);
                    mouvementCompteListNewMouvementCompte = em.merge(mouvementCompteListNewMouvementCompte);
                    if (oldFactureOfMouvementCompteListNewMouvementCompte != null && !oldFactureOfMouvementCompteListNewMouvementCompte.equals(facture)) {
                        oldFactureOfMouvementCompteListNewMouvementCompte.getMouvementCompteList().remove(mouvementCompteListNewMouvementCompte);
                        oldFactureOfMouvementCompteListNewMouvementCompte = em.merge(oldFactureOfMouvementCompteListNewMouvementCompte);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facture.getIdfacture();
                if (findFacture(id) == null) {
                    throw new NonexistentEntityException("The facture with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facture facture;
            try {
                facture = em.getReference(Facture.class, id);
                facture.getIdfacture();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facture with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MouvementCompte> mouvementCompteListOrphanCheck = facture.getMouvementCompteList();
            for (MouvementCompte mouvementCompteListOrphanCheckMouvementCompte : mouvementCompteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facture (" + facture + ") cannot be destroyed since the MouvementCompte " + mouvementCompteListOrphanCheckMouvementCompte + " in its mouvementCompteList field has a non-nullable facture field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PayementMode payementMode = facture.getPayementMode();
            if (payementMode != null) {
                payementMode.getFactureList().remove(facture);
                payementMode = em.merge(payementMode);
            }
            em.remove(facture);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facture> findFactureEntities() {
        return findFactureEntities(true, -1, -1);
    }

    public List<Facture> findFactureEntities(int maxResults, int firstResult) {
        return findFactureEntities(false, maxResults, firstResult);
    }

    private List<Facture> findFactureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Facture as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Facture findFacture(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facture.class, id);
        } finally {
            em.close();
        }
    }

    public int getFactureCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Facture as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
