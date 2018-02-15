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
import modelo.Areas;

/**
 *
 * @author usuario
 */
public class AreasJpaController implements Serializable {

    public AreasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Areas areas) {
        if (areas.getDocumentoList() == null) {
            areas.setDocumentoList(new ArrayList<Documento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documento> attachedDocumentoList = new ArrayList<Documento>();
            for (Documento documentoListDocumentoToAttach : areas.getDocumentoList()) {
                documentoListDocumentoToAttach = em.getReference(documentoListDocumentoToAttach.getClass(), documentoListDocumentoToAttach.getIdDocumento());
                attachedDocumentoList.add(documentoListDocumentoToAttach);
            }
            areas.setDocumentoList(attachedDocumentoList);
            em.persist(areas);
            for (Documento documentoListDocumento : areas.getDocumentoList()) {
                Areas oldAreaOfDocumentoListDocumento = documentoListDocumento.getArea();
                documentoListDocumento.setArea(areas);
                documentoListDocumento = em.merge(documentoListDocumento);
                if (oldAreaOfDocumentoListDocumento != null) {
                    oldAreaOfDocumentoListDocumento.getDocumentoList().remove(documentoListDocumento);
                    oldAreaOfDocumentoListDocumento = em.merge(oldAreaOfDocumentoListDocumento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Areas areas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Areas persistentAreas = em.find(Areas.class, areas.getIdArea());
            List<Documento> documentoListOld = persistentAreas.getDocumentoList();
            List<Documento> documentoListNew = areas.getDocumentoList();
            List<Documento> attachedDocumentoListNew = new ArrayList<Documento>();
            documentoListNew = attachedDocumentoListNew;
            areas.setDocumentoList(documentoListNew);
            areas = em.merge(areas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = areas.getIdArea();
                if (findAreas(id) == null) {
                    throw new NonexistentEntityException("The areas with id " + id + " no longer exists.");
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
            Areas areas;
            try {
                areas = em.getReference(Areas.class, id);
                areas.getIdArea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The areas with id " + id + " no longer exists.", enfe);
            }
            List<Documento> documentoList = areas.getDocumentoList();
            for (Documento documentoListDocumento : documentoList) {
                documentoListDocumento.setArea(null);
                documentoListDocumento = em.merge(documentoListDocumento);
            }
            em.remove(areas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Areas> findAreasEntities() {
        return findAreasEntities(true, -1, -1);
    }

    public List<Areas> findAreasEntities(int maxResults, int firstResult) {
        return findAreasEntities(false, maxResults, firstResult);
    }

    private List<Areas> findAreasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Areas.class));
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

    public Areas findAreas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Areas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAreasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Areas> rt = cq.from(Areas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
