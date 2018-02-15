/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "observacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacion.findAll", query = "SELECT o FROM Observacion o")
    , @NamedQuery(name = "Observacion.findByIdObservacion", query = "SELECT o FROM Observacion o WHERE o.idObservacion = :idObservacion")
    , @NamedQuery(name = "Observacion.findByInformacion", query = "SELECT o FROM Observacion o WHERE o.informacion = :informacion")
    , @NamedQuery(name = "Observacion.findBySalida", query = "SELECT o FROM Observacion o WHERE o.salida = :salida")
    , @NamedQuery(name = "Observacion.findByDocumento", query = "SELECT o FROM Observacion o WHERE o.documento = :documento")})
public class Observacion implements Serializable {

    @JoinColumn(name = "documento", referencedColumnName = "idDocumento")
    @ManyToOne
    private Documento documento;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idObservacion")
    private Integer idObservacion;
    @Column(name = "informacion")
    private String informacion;
    @Column(name = "salida")
    private String salida;

    public Observacion() {
    }

    public Observacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObservacion != null ? idObservacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observacion)) {
            return false;
        }
        Observacion other = (Observacion) object;
        if ((this.idObservacion == null && other.idObservacion != null) || (this.idObservacion != null && !this.idObservacion.equals(other.idObservacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Observacion[ idObservacion=" + idObservacion + " ]";
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

}
