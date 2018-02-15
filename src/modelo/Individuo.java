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
@Table(name = "individuo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Individuo.findAll", query = "SELECT i FROM Individuo i")
    , @NamedQuery(name = "Individuo.findByIdIndividuo", query = "SELECT i FROM Individuo i WHERE i.idIndividuo = :idIndividuo")
    , @NamedQuery(name = "Individuo.findByNombreIndividuo", query = "SELECT i FROM Individuo i WHERE i.nombreIndividuo = :nombreIndividuo")
    , @NamedQuery(name = "Individuo.findByApPaterno", query = "SELECT i FROM Individuo i WHERE i.apPaterno = :apPaterno")
    , @NamedQuery(name = "Individuo.findByApMaterno", query = "SELECT i FROM Individuo i WHERE i.apMaterno = :apMaterno")
    , @NamedQuery(name = "Individuo.findByCargo", query = "SELECT i FROM Individuo i WHERE i.cargo = :cargo")
    , @NamedQuery(name = "Individuo.findExiste", query = "SELECT i FROM Individuo i WHERE i.nombreIndividuo = :nombreIndividuo AND i.apPaterno = :apPaterno AND "
            + "i.apMaterno = :apMaterno")})
public class Individuo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idIndividuo")
    private Integer idIndividuo;
    @Column(name = "nombreIndividuo")
    private String nombreIndividuo;
    @Column(name = "apPaterno")
    private String apPaterno;
    @Column(name = "apMaterno")
    private String apMaterno;
    @Column(name = "cargo")
    private String cargo;
    @OneToMany(mappedBy = "individuo")
    private List<Documento> documentoList;

    public Individuo() {
    }

    public Individuo(Integer idIndividuo) {
        this.idIndividuo = idIndividuo;
    }

    public Integer getIdIndividuo() {
        return idIndividuo;
    }

    public void setIdIndividuo(Integer idIndividuo) {
        this.idIndividuo = idIndividuo;
    }

    public String getNombreIndividuo() {
        return nombreIndividuo;
    }

    public void setNombreIndividuo(String nombreIndividuo) {
        this.nombreIndividuo = nombreIndividuo;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
        hash += (idIndividuo != null ? idIndividuo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Individuo)) {
            return false;
        }
        Individuo other = (Individuo) object;
        if ((this.idIndividuo == null && other.idIndividuo != null) || (this.idIndividuo != null && !this.idIndividuo.equals(other.idIndividuo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Individuo[ idIndividuo=" + idIndividuo + " ]";
    }

}
