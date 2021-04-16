package sn.esp.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.esp.gestion.domain.enumeration.EtatTache;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatTache etat;

    @OneToMany(mappedBy = "tache")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tache" }, allowSetters = true)
    private Set<Materiel> materiels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "taches" }, allowSetters = true)
    private Agent agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tache id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Tache nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public EtatTache getEtat() {
        return this.etat;
    }

    public Tache etat(EtatTache etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(EtatTache etat) {
        this.etat = etat;
    }

    public Set<Materiel> getMateriels() {
        return this.materiels;
    }

    public Tache materiels(Set<Materiel> materiels) {
        this.setMateriels(materiels);
        return this;
    }

    public Tache addMateriel(Materiel materiel) {
        this.materiels.add(materiel);
        materiel.setTache(this);
        return this;
    }

    public Tache removeMateriel(Materiel materiel) {
        this.materiels.remove(materiel);
        materiel.setTache(null);
        return this;
    }

    public void setMateriels(Set<Materiel> materiels) {
        if (this.materiels != null) {
            this.materiels.forEach(i -> i.setTache(null));
        }
        if (materiels != null) {
            materiels.forEach(i -> i.setTache(this));
        }
        this.materiels = materiels;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public Tache agent(Agent agent) {
        this.setAgent(agent);
        return this;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tache)) {
            return false;
        }
        return id != null && id.equals(((Tache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tache{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
