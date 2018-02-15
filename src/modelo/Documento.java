/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d")
    , @NamedQuery(name = "Documento.findByIdDocumento", query = "SELECT d FROM Documento d WHERE d.idDocumento = :idDocumento")
    , @NamedQuery(name = "Documento.findByTituloDocumento", query = "SELECT d FROM Documento d WHERE d.tituloDocumento = :tituloDocumento")
    , @NamedQuery(name = "Documento.findByUrlDocumento", query = "SELECT d FROM Documento d WHERE d.urlDocumento = :urlDocumento")
    , @NamedQuery(name = "Documento.findByFechaEmision", query = "SELECT d FROM Documento d WHERE d.fechaEmision = :fechaEmision")
    , @NamedQuery(name = "Documento.findByFechaRecepcion", query = "SELECT d FROM Documento d WHERE d.fechaRecepcion = :fechaRecepcion")
    , @NamedQuery(name = "Documento.findByNroDocumento", query = "SELECT d FROM Documento d WHERE d.nroDocumento = :nroDocumento")
    , @NamedQuery(name = "Documento.findByCategoria", query = "SELECT d FROM Documento d WHERE d.categoria = :categoria ORDER BY d.idDocumento DESC")
    , @NamedQuery(name = "Documento.findAllDesc", query = "SELECT d FROM Documento d ORDER BY d.idDocumento DESC")})
public class Documento implements Serializable {

    @OneToMany(mappedBy = "documento")
    private List<Observacion> observacionList;

    @Column(name = "nroDocumento")
    private String nroDocumento;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDocumento")
    private Integer idDocumento;
    @Column(name = "tituloDocumento")
    private String tituloDocumento;
    @Column(name = "urlDocumento")
    private String urlDocumento;
    @Column(name = "fechaEmision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Column(name = "fechaRecepcion")
    @Temporal(TemporalType.DATE)
    private Date fechaRecepcion;
    @JoinColumn(name = "individuo", referencedColumnName = "idIndividuo")
    @ManyToOne
    private Individuo individuo;
    @JoinColumn(name = "area", referencedColumnName = "idArea")
    @ManyToOne
    private Areas area;
    @JoinColumn(name = "categoria", referencedColumnName = "idCategoria")
    @ManyToOne
    private Categoria categoria;

    public Documento() {
    }

    public Documento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getTituloDocumento() {
        return tituloDocumento;
    }

    public void setTituloDocumento(String tituloDocumento) {
        this.tituloDocumento = tituloDocumento;
    }

    public String getUrlDocumento() {
        return urlDocumento;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Individuo getIndividuo() {
        return individuo;
    }

    public void setIndividuo(Individuo individuo) {
        this.individuo = individuo;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumento != null ? idDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.idDocumento == null && other.idDocumento != null) || (this.idDocumento != null && !this.idDocumento.equals(other.idDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Documento[ idDocumento=" + idDocumento + " ]";
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    @XmlTransient
    public List<Observacion> getObservacionList() {
        return observacionList;
    }

    public void setObservacionList(List<Observacion> observacionList) {
        this.observacionList = observacionList;
    }

}
