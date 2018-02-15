/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Destinatarios;

/**
 *
 * @author usuario
 */
public class DestinatariosJpaController implements Serializable {

    public DestinatariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Destinatarios destinatarios) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(destinatarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Destinatarios destinatarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            destinatarios = em.merge(destinatarios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = destinatarios.getIdDestinario();
                if (findDestinatarios(id) == null) {
                    throw new NonexistentEntityException("The destinatarios with id " + id + " no longer exists.");
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
            Destinatarios destinatarios;
            try {
                destinatarios = em.getReference(Destinatarios.class, id);
                destinatarios.getIdDestinario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The destinatarios with id " + id + " no longer exists.", enfe);
            }
            em.remove(destinatarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Destinatarios> findDestinatariosEntities() {
        return findDestinatariosEntities(true, -1, -1);
    }

    public List<Destinatarios> findDestinatariosEntities(int maxResults, int firstResult) {
        return findDestinatariosEntities(false, maxResults, firstResult);
    }

    private List<Destinatarios> findDestinatariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Destinatarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Destinatarios findDestinatarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Destinatarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getDestinatariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Destinatarios> rt = cq.from(Destinatarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
