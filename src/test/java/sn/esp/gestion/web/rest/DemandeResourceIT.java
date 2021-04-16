package sn.esp.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.esp.gestion.IntegrationTest;
import sn.esp.gestion.domain.Demande;
import sn.esp.gestion.domain.enumeration.EnumCause;
import sn.esp.gestion.domain.enumeration.EnumDepartement;
import sn.esp.gestion.domain.enumeration.EnumPriorite;
import sn.esp.gestion.domain.enumeration.EnumService;
import sn.esp.gestion.domain.enumeration.StatusDemande;
import sn.esp.gestion.repository.DemandeRepository;

/**
 * Integration tests for the {@link DemandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeResourceIT {

    private static final StatusDemande DEFAULT_STATUT = StatusDemande.Ok;
    private static final StatusDemande UPDATED_STATUT = StatusDemande.En_cours;

    private static final Instant DEFAULT_DATE_DEMANDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEMANDE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final Integer DEFAULT_DUREE = 1;
    private static final Integer UPDATED_DUREE = 2;

    private static final EnumPriorite DEFAULT_PRIORITE = EnumPriorite.Pas_Urgent;
    private static final EnumPriorite UPDATED_PRIORITE = EnumPriorite.Urgent;

    private static final EnumCause DEFAULT_CAUSE_DEFAILLANCE = EnumCause.Usure_Normale;
    private static final EnumCause UPDATED_CAUSE_DEFAILLANCE = EnumCause.Defaut_D_Utilisateur;

    private static final String DEFAULT_AUTRES_CAUSES = "AAAAAAAAAA";
    private static final String UPDATED_AUTRES_CAUSES = "BBBBBBBBBB";

    private static final EnumDepartement DEFAULT_DEPARTEMENT = EnumDepartement.Genie_Civile;
    private static final EnumDepartement UPDATED_DEPARTEMENT = EnumDepartement.Genie_Informatique;

    private static final EnumService DEFAULT_TYPE_DEFAILLANCE = EnumService.Maconnerie;
    private static final EnumService UPDATED_TYPE_DEFAILLANCE = EnumService.Electricite;

    private static final String ENTITY_API_URL = "/api/demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMockMvc;

    private Demande demande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createEntity(EntityManager em) {
        Demande demande = new Demande()
            .statut(DEFAULT_STATUT)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .lieu(DEFAULT_LIEU)
            .duree(DEFAULT_DUREE)
            .priorite(DEFAULT_PRIORITE)
            .causeDefaillance(DEFAULT_CAUSE_DEFAILLANCE)
            .autresCauses(DEFAULT_AUTRES_CAUSES)
            .departement(DEFAULT_DEPARTEMENT)
            .typeDefaillance(DEFAULT_TYPE_DEFAILLANCE);
        return demande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createUpdatedEntity(EntityManager em) {
        Demande demande = new Demande()
            .statut(UPDATED_STATUT)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .lieu(UPDATED_LIEU)
            .duree(UPDATED_DUREE)
            .priorite(UPDATED_PRIORITE)
            .causeDefaillance(UPDATED_CAUSE_DEFAILLANCE)
            .autresCauses(UPDATED_AUTRES_CAUSES)
            .departement(UPDATED_DEPARTEMENT)
            .typeDefaillance(UPDATED_TYPE_DEFAILLANCE);
        return demande;
    }

    @BeforeEach
    public void initTest() {
        demande = createEntity(em);
    }

    @Test
    @Transactional
    void createDemande() throws Exception {
        int databaseSizeBeforeCreate = demandeRepository.findAll().size();
        // Create the Demande
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isCreated());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeCreate + 1);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testDemande.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testDemande.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testDemande.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testDemande.getPriorite()).isEqualTo(DEFAULT_PRIORITE);
        assertThat(testDemande.getCauseDefaillance()).isEqualTo(DEFAULT_CAUSE_DEFAILLANCE);
        assertThat(testDemande.getAutresCauses()).isEqualTo(DEFAULT_AUTRES_CAUSES);
        assertThat(testDemande.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testDemande.getTypeDefaillance()).isEqualTo(DEFAULT_TYPE_DEFAILLANCE);
    }

    @Test
    @Transactional
    void createDemandeWithExistingId() throws Exception {
        // Create the Demande with an existing ID
        demande.setId(1L);

        int databaseSizeBeforeCreate = demandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandes() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get all the demandeList
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].priorite").value(hasItem(DEFAULT_PRIORITE.toString())))
            .andExpect(jsonPath("$.[*].causeDefaillance").value(hasItem(DEFAULT_CAUSE_DEFAILLANCE.toString())))
            .andExpect(jsonPath("$.[*].autresCauses").value(hasItem(DEFAULT_AUTRES_CAUSES)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].typeDefaillance").value(hasItem(DEFAULT_TYPE_DEFAILLANCE.toString())));
    }

    @Test
    @Transactional
    void getDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demande.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.priorite").value(DEFAULT_PRIORITE.toString()))
            .andExpect(jsonPath("$.causeDefaillance").value(DEFAULT_CAUSE_DEFAILLANCE.toString()))
            .andExpect(jsonPath("$.autresCauses").value(DEFAULT_AUTRES_CAUSES))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.typeDefaillance").value(DEFAULT_TYPE_DEFAILLANCE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande
        Demande updatedDemande = demandeRepository.findById(demande.getId()).get();
        // Disconnect from session so that the updates on updatedDemande are not directly saved in db
        em.detach(updatedDemande);
        updatedDemande
            .statut(UPDATED_STATUT)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .lieu(UPDATED_LIEU)
            .duree(UPDATED_DUREE)
            .priorite(UPDATED_PRIORITE)
            .causeDefaillance(UPDATED_CAUSE_DEFAILLANCE)
            .autresCauses(UPDATED_AUTRES_CAUSES)
            .departement(UPDATED_DEPARTEMENT)
            .typeDefaillance(UPDATED_TYPE_DEFAILLANCE);

        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testDemande.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemande.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testDemande.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDemande.getPriorite()).isEqualTo(UPDATED_PRIORITE);
        assertThat(testDemande.getCauseDefaillance()).isEqualTo(UPDATED_CAUSE_DEFAILLANCE);
        assertThat(testDemande.getAutresCauses()).isEqualTo(UPDATED_AUTRES_CAUSES);
        assertThat(testDemande.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testDemande.getTypeDefaillance()).isEqualTo(UPDATED_TYPE_DEFAILLANCE);
    }

    @Test
    @Transactional
    void putNonExistingDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande
            .dateDemande(UPDATED_DATE_DEMANDE)
            .lieu(UPDATED_LIEU)
            .departement(UPDATED_DEPARTEMENT)
            .typeDefaillance(UPDATED_TYPE_DEFAILLANCE);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testDemande.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemande.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testDemande.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testDemande.getPriorite()).isEqualTo(DEFAULT_PRIORITE);
        assertThat(testDemande.getCauseDefaillance()).isEqualTo(DEFAULT_CAUSE_DEFAILLANCE);
        assertThat(testDemande.getAutresCauses()).isEqualTo(DEFAULT_AUTRES_CAUSES);
        assertThat(testDemande.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testDemande.getTypeDefaillance()).isEqualTo(UPDATED_TYPE_DEFAILLANCE);
    }

    @Test
    @Transactional
    void fullUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande
            .statut(UPDATED_STATUT)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .lieu(UPDATED_LIEU)
            .duree(UPDATED_DUREE)
            .priorite(UPDATED_PRIORITE)
            .causeDefaillance(UPDATED_CAUSE_DEFAILLANCE)
            .autresCauses(UPDATED_AUTRES_CAUSES)
            .departement(UPDATED_DEPARTEMENT)
            .typeDefaillance(UPDATED_TYPE_DEFAILLANCE);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testDemande.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemande.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testDemande.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDemande.getPriorite()).isEqualTo(UPDATED_PRIORITE);
        assertThat(testDemande.getCauseDefaillance()).isEqualTo(UPDATED_CAUSE_DEFAILLANCE);
        assertThat(testDemande.getAutresCauses()).isEqualTo(UPDATED_AUTRES_CAUSES);
        assertThat(testDemande.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testDemande.getTypeDefaillance()).isEqualTo(UPDATED_TYPE_DEFAILLANCE);
    }

    @Test
    @Transactional
    void patchNonExistingDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeDelete = demandeRepository.findAll().size();

        // Delete the demande
        restDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, demande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
