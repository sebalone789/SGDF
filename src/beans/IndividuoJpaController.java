/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Documento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Individuo;

/**
 *
 * @author usuario
 */
public class IndividuoJpaController implements Serializable {

    public IndividuoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Individuo individuo) {
        if (individuo.getDocumentoList() == null) {
            individuo.setDocumentoList(new ArrayList<Documento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documento> attachedDocumentoList = new ArrayList<Documento>();
            for (Documento documentoListDocumentoToAttach : individuo.getDocumentoList()) {
                documentoListDocumentoToAttach = em.getReference(documentoListDocumentoToAttach.getClass(), documentoListDocumentoToAttach.getIdDocumento());
                attachedDocumentoList.add(documentoListDocumentoToAttach);
            }
            individuo.setDocumentoList(attachedDocumentoList);
            em.persist(individuo);
            for (Documento documentoListDocumento : individuo.getDocumentoList()) {
                Individuo oldIndividuoOfDocumentoListDocumento = documentoListDocumento.getIndividuo();
                documentoListDocumento.setIndividuo(individuo);
                documentoListDocumento = em.merge(documentoListDocumento);
                if (oldIndividuoOfDocumentoListDocumento != null) {
                    oldIndividuoOfDocumentoListDocumento.getDocumentoList().remove(documentoListDocumento);
                    oldIndividuoOfDocumentoListDocumento = em.merge(oldIndividuoOfDocumentoListDocumento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Individuo individuo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Individuo persistentIndividuo = em.find(Individuo.class, individuo.getIdIndividuo());
            List<Documento> documentoListOld = persistentIndividuo.getDocumentoList();
            List<Documento> documentoListNew = individuo.getDocumentoList();
            List<Documento> attachedDocumentoListNew = new ArrayList<Documento>();

            documentoListNew = attachedDocumentoListNew;
            individuo.setDocumentoList(documentoListNew);
            individuo = em.merge(individuo);
           
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = individuo.getIdIndividuo();
                if (findIndividuo(id) == null) {
                    throw new NonexistentEntityException("The individuo with id " + id + " no longer exists.");
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
            Individuo individuo;
            try {
                individuo = em.getReference(Individuo.class, id);
                individuo.getIdIndividuo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The individuo with id " + id + " no longer exists.", enfe);
            }
            List<Documento> documentoList = individuo.getDocumentoList();
            for (Documento documentoListDocumento : documentoList) {
                documentoListDocumento.setIndividuo(null);
                documentoListDocumento = em.merge(documentoListDocumento);
            }
            em.remove(individuo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Individuo> findIndividuoEntities() {
        return findIndividuoEntities(true, -1, -1);
    }

    public List<Individuo> findIndividuoEntities(int maxResults, int firstResult) {
        return findIndividuoEntities(false, maxResults, firstResult);
    }

    private List<Individuo> findIndividuoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Individuo.class));
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

    public Individuo findIndividuo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Individuo.class, id);
        } finally {
            em.close();
        }
    }

    public int getIndividuoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Individuo> rt = cq.from(Individuo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
