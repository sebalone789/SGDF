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
import modelo.Individuo;
import modelo.Areas;
import modelo.Categoria;
import modelo.Documento;

/**
 *
 * @author usuario
 */
public class DocumentoJpaController implements Serializable {

    public DocumentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documento documento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Individuo individuo = documento.getIndividuo();
            if (individuo != null) {
                individuo = em.getReference(individuo.getClass(), individuo.getIdIndividuo());
                documento.setIndividuo(individuo);
            }
            Areas area = documento.getArea();
            if (area != null) {
                area = em.getReference(area.getClass(), area.getIdArea());
                documento.setArea(area);
            }
            Categoria categoria = documento.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getIdCategoria());
                documento.setCategoria(categoria);
            }
            em.persist(documento);
            if (individuo != null) {
                individuo.getDocumentoList().add(documento);
                individuo = em.merge(individuo);
            }
            if (area != null) {
                area.getDocumentoList().add(documento);
                area = em.merge(area);
            }
            if (categoria != null) {
                categoria.getDocumentoList().add(documento);
                categoria = em.merge(categoria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documento documento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento persistentDocumento = em.find(Documento.class, documento.getIdDocumento());
            Individuo individuoOld = persistentDocumento.getIndividuo();
            Individuo individuoNew = documento.getIndividuo();
            Areas areaOld = persistentDocumento.getArea();
            Areas areaNew = documento.getArea();
            Categoria categoriaOld = persistentDocumento.getCategoria();
            Categoria categoriaNew = documento.getCategoria();
            if (individuoNew != null) {
                individuoNew = em.getReference(individuoNew.getClass(), individuoNew.getIdIndividuo());
                documento.setIndividuo(individuoNew);
            }
            if (areaNew != null) {
                areaNew = em.getReference(areaNew.getClass(), areaNew.getIdArea());
                documento.setArea(areaNew);
            }
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getIdCategoria());
                documento.setCategoria(categoriaNew);
            }
            documento = em.merge(documento);
            if (individuoOld != null && !individuoOld.equals(individuoNew)) {
                individuoOld.getDocumentoList().remove(documento);
                individuoOld = em.merge(individuoOld);
            }
            if (individuoNew != null && !individuoNew.equals(individuoOld)) {
                individuoNew.getDocumentoList().add(documento);
                individuoNew = em.merge(individuoNew);
            }
            if (areaOld != null && !areaOld.equals(areaNew)) {
                areaOld.getDocumentoList().remove(documento);
                areaOld = em.merge(areaOld);
            }
            if (areaNew != null && !areaNew.equals(areaOld)) {
                areaNew.getDocumentoList().add(documento);
                areaNew = em.merge(areaNew);
            }
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getDocumentoList().remove(documento);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getDocumentoList().add(documento);
                categoriaNew = em.merge(categoriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documento.getIdDocumento();
                if (findDocumento(id) == null) {
                    throw new NonexistentEntityException("The documento with id " + id + " no longer exists.");
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
            Documento documento;
            try {
                documento = em.getReference(Documento.class, id);
                documento.getIdDocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documento with id " + id + " no longer exists.", enfe);
            }
            Individuo individuo = documento.getIndividuo();
            if (individuo != null) {
                individuo.getDocumentoList().remove(documento);
                individuo = em.merge(individuo);
            }
            Areas area = documento.getArea();
            if (area != null) {
                area.getDocumentoList().remove(documento);
                area = em.merge(area);
            }
            Categoria categoria = documento.getCategoria();
            if (categoria != null) {
                categoria.getDocumentoList().remove(documento);
                categoria = em.merge(categoria);
            }
            em.remove(documento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documento> findDocumentoEntities() {
        return findDocumentoEntities(true, -1, -1);
    }

    public List<Documento> findDocumentoEntities(int maxResults, int firstResult) {
        return findDocumentoEntities(false, maxResults, firstResult);
    }

    private List<Documento> findDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documento.class));
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

    public Documento findDocumento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from(Documento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
