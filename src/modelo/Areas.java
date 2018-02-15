/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "areas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Areas.findAll", query = "SELECT a FROM Areas a")
    , @NamedQuery(name = "Areas.findByIdArea", query = "SELECT a FROM Areas a WHERE a.idArea = :idArea")
    , @NamedQuery(name = "Areas.findByDescripcion", query = "SELECT a FROM Areas a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "Areas.findByNombreResponsable", query = "SELECT a FROM Areas a WHERE a.nombreResponsable = :nombreResponsable")
    , @NamedQuery(name = "Areas.findByApPaternoResponsable", query = "SELECT a FROM Areas a WHERE a.apPaternoResponsable = :apPaternoResponsable")
    , @NamedQuery(name = "Areas.findByApMaternoResponsable", query = "SELECT a FROM Areas a WHERE a.apMaternoResponsable = :apMaternoResponsable")
    , @NamedQuery(name = "Areas.findExiste", query = "SELECT a FROM Areas a WHERE a.descripcion = :descripcion AND "
            + "a.nombreResponsable = :nombreResponsable AND "
            + "a.apPaternoResponsable = :apPaternoResponsable AND "
            + "a.apMaternoResponsable = :apMaternoResponsable")})
public class Areas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idArea")
    private Integer idArea;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nombreResponsable")
    private String nombreResponsable;
    @Column(name = "apPaternoResponsable")
    private String apPaternoResponsable;
    @Column(name = "apMaternoResponsable")
    private String apMaternoResponsable;
    @OneToMany(mappedBy = "area")
    private List<Documento> documentoList;

    public Areas() {
    }

    public Areas(Integer idArea) {
        this.idArea = idArea;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getApPaternoResponsable() {
        return apPaternoResponsable;
    }

    public void setApPaternoResponsable(String apPaternoResponsable) {
        this.apPaternoResponsable = apPaternoResponsable;
    }

    public String getApMaternoResponsable() {
        return apMaternoResponsable;
    }

    public void setApMaternoResponsable(String apMaternoResponsable) {
        this.apMaternoResponsable = apMaternoResponsable;
    }

    @XmlTransient
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArea != null ? idArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Areas)) {
            return false;
        }
        Areas other = (Areas) object;
        if ((this.idArea == null && other.idArea != null) || (this.idArea != null && !this.idArea.equals(other.idArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Areas[ idArea=" + idArea + " ]";
    }
    
}
