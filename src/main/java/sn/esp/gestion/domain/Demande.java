package sn.esp.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.esp.gestion.domain.enumeration.EnumCause;
import sn.esp.gestion.domain.enumeration.EnumDepartement;
import sn.esp.gestion.domain.enumeration.EnumPriorite;
import sn.esp.gestion.domain.enumeration.EnumService;
import sn.esp.gestion.domain.enumeration.StatusDemande;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Demande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatusDemande statut;

    @Column(name = "date_demande")
    private Instant dateDemande;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "duree")
    private Integer duree;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite")
    private EnumPriorite priorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "cause_defaillance")
    private EnumCause causeDefaillance;

    @Column(name = "autres_causes")
    private String autresCauses;

    @Enumerated(EnumType.STRING)
    @Column(name = "departement")
    private EnumDepartement departement;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_defaillance")
    private EnumService typeDefaillance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "demandes" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Demande id(Long id) {
        this.id = id;
        return this;
    }

    public StatusDemande getStatut() {
        return this.statut;
    }

    public Demande statut(StatusDemande statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatusDemande statut) {
        this.statut = statut;
    }

    public Instant getDateDemande() {
        return this.dateDemande;
    }

    public Demande dateDemande(Instant dateDemande) {
        this.dateDemande = dateDemande;
        return this;
    }

    public void setDateDemande(Instant dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Demande lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Integer getDuree() {
        return this.duree;
    }

    public Demande duree(Integer duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public EnumPriorite getPriorite() {
        return this.priorite;
    }

    public Demande priorite(EnumPriorite priorite) {
        this.priorite = priorite;
        return this;
    }

    public void setPriorite(EnumPriorite priorite) {
        this.priorite = priorite;
    }

    public EnumCause getCauseDefaillance() {
        return this.causeDefaillance;
    }

    public Demande causeDefaillance(EnumCause causeDefaillance) {
        this.causeDefaillance = causeDefaillance;
        return this;
    }

    public void setCauseDefaillance(EnumCause causeDefaillance) {
        this.causeDefaillance = causeDefaillance;
    }

    public String getAutresCauses() {
        return this.autresCauses;
    }

    public Demande autresCauses(String autresCauses) {
        this.autresCauses = autresCauses;
        return this;
    }

    public void setAutresCauses(String autresCauses) {
        this.autresCauses = autresCauses;
    }

    public EnumDepartement getDepartement() {
        return this.departement;
    }

    public Demande departement(EnumDepartement departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(EnumDepartement departement) {
        this.departement = departement;
    }

    public EnumService getTypeDefaillance() {
        return this.typeDefaillance;
    }

    public Demande typeDefaillance(EnumService typeDefaillance) {
        this.typeDefaillance = typeDefaillance;
        return this;
    }

    public void setTypeDefaillance(EnumService typeDefaillance) {
        this.typeDefaillance = typeDefaillance;
    }

    public Client getClient() {
        return this.client;
    }

    public Demande client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return id != null && id.equals(((Demande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", dateDemande='" + getDateDemande() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", duree=" + getDuree() +
            ", priorite='" + getPriorite() + "'" +
            ", causeDefaillance='" + getCauseDefaillance() + "'" +
            ", autresCauses='" + getAutresCauses() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", typeDefaillance='" + getTypeDefaillance() + "'" +
            "}";
    }
}
