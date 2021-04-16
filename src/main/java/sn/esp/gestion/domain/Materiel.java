package sn.esp.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Materiel.
 */
@Entity
@Table(name = "materiel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Materiel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "quantity_use")
    private Integer quantityUse;

    @Column(name = "quantity_stock")
    private Integer quantityStock;

    @ManyToOne
    @JsonIgnoreProperties(value = { "materiels", "agent" }, allowSetters = true)
    private Tache tache;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Materiel id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Materiel libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getQuantityUse() {
        return this.quantityUse;
    }

    public Materiel quantityUse(Integer quantityUse) {
        this.quantityUse = quantityUse;
        return this;
    }

    public void setQuantityUse(Integer quantityUse) {
        this.quantityUse = quantityUse;
    }

    public Integer getQuantityStock() {
        return this.quantityStock;
    }

    public Materiel quantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
        return this;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }

    public Tache getTache() {
        return this.tache;
    }

    public Materiel tache(Tache tache) {
        this.setTache(tache);
        return this;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materiel)) {
            return false;
        }
        return id != null && id.equals(((Materiel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materiel{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", quantityUse=" + getQuantityUse() +
            ", quantityStock=" + getQuantityStock() +
            "}";
    }
}
