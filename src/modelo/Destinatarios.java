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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "destinatarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Destinatarios.findAll", query = "SELECT d FROM Destinatarios d")
    , @NamedQuery(name = "Destinatarios.findByIdDestinario", query = "SELECT d FROM Destinatarios d WHERE d.idDestinario = :idDestinario")
    , @NamedQuery(name = "Destinatarios.findByNombre", query = "SELECT d FROM Destinatarios d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Destinatarios.findByApPaterno", query = "SELECT d FROM Destinatarios d WHERE d.apPaterno = :apPaterno")
    , @NamedQuery(name = "Destinatarios.findByApMaterno", query = "SELECT d FROM Destinatarios d WHERE d.apMaterno = :apMaterno")
    , @NamedQuery(name = "Destinatarios.findByEmail", query = "SELECT d FROM Destinatarios d WHERE d.email = :email")
    , @NamedQuery(name = "Destinatarios.findByContrasena", query = "SELECT d FROM Destinatarios d WHERE d.contrasena = :contrasena")
    , @NamedQuery(name = "Destinatarios.findForLogin", query = "SELECT d FROM Destinatarios d WHERE d.contrasena = :contrasena AND "
            + "d.email = :email")})
public class Destinatarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDestinario")
    private Integer idDestinario;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apPaterno")
    private String apPaterno;
    @Column(name = "apMaterno")
    private String apMaterno;
    @Column(name = "email")
    private String email;
    @Column(name = "contrasena")
    private String contrasena;

    public Destinatarios() {
    }

    public Destinatarios(Integer idDestinario) {
        this.idDestinario = idDestinario;
    }

    public Integer getIdDestinario() {
        return idDestinario;
    }

    public void setIdDestinario(Integer idDestinario) {
        this.idDestinario = idDestinario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDestinario != null ? idDestinario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Destinatarios)) {
            return false;
        }
        Destinatarios other = (Destinatarios) object;
        if ((this.idDestinario == null && other.idDestinario != null) || (this.idDestinario != null && !this.idDestinario.equals(other.idDestinario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Destinatarios[ idDestinario=" + idDestinario + " ]";
    }

}
