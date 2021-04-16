package sn.esp.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.esp.gestion.domain.ChefService;
import sn.esp.gestion.domain.enumeration.EnumSexe;
import sn.esp.gestion.repository.ChefServiceRepository;

/**
 * Integration tests for the {@link ChefServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChefServiceResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final EnumSexe DEFAULT_SEXE = EnumSexe.M;
    private static final EnumSexe UPDATED_SEXE = EnumSexe.F;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chef-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefServiceRepository chefServiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChefServiceMockMvc;

    private ChefService chefService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefService createEntity(EntityManager em) {
        ChefService chefService = new ChefService()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .sexe(DEFAULT_SEXE)
            .email(DEFAULT_EMAIL)
            .tel(DEFAULT_TEL);
        return chefService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChefService createUpdatedEntity(EntityManager em) {
        ChefService chefService = new ChefService()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL);
        return chefService;
    }

    @BeforeEach
    public void initTest() {
        chefService = createEntity(em);
    }

    @Test
    @Transactional
    void createChefService() throws Exception {
        int databaseSizeBeforeCreate = chefServiceRepository.findAll().size();
        // Create the ChefService
        restChefServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isCreated());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ChefService testChefService = chefServiceList.get(chefServiceList.size() - 1);
        assertThat(testChefService.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testChefService.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testChefService.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testChefService.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testChefService.getTel()).isEqualTo(DEFAULT_TEL);
    }

    @Test
    @Transactional
    void createChefServiceWithExistingId() throws Exception {
        // Create the ChefService with an existing ID
        chefService.setId(1L);

        int databaseSizeBeforeCreate = chefServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isBadRequest());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefServiceRepository.findAll().size();
        // set the field null
        chefService.setFirstName(null);

        // Create the ChefService, which fails.

        restChefServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isBadRequest());

        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefServiceRepository.findAll().size();
        // set the field null
        chefService.setLastName(null);

        // Create the ChefService, which fails.

        restChefServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isBadRequest());

        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefServiceRepository.findAll().size();
        // set the field null
        chefService.setEmail(null);

        // Create the ChefService, which fails.

        restChefServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isBadRequest());

        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChefServices() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        // Get all the chefServiceList
        restChefServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefService.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }

    @Test
    @Transactional
    void getChefService() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        // Get the chefService
        restChefServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, chefService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chefService.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL));
    }

    @Test
    @Transactional
    void getNonExistingChefService() throws Exception {
        // Get the chefService
        restChefServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChefService() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();

        // Update the chefService
        ChefService updatedChefService = chefServiceRepository.findById(chefService.getId()).get();
        // Disconnect from session so that the updates on updatedChefService are not directly saved in db
        em.detach(updatedChefService);
        updatedChefService
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL);

        restChefServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChefService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChefService))
            )
            .andExpect(status().isOk());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
        ChefService testChefService = chefServiceList.get(chefServiceList.size() - 1);
        assertThat(testChefService.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testChefService.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testChefService.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefService.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testChefService.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void putNonExistingChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chefService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chefService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chefService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChefServiceWithPatch() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();

        // Update the chefService using partial update
        ChefService partialUpdatedChefService = new ChefService();
        partialUpdatedChefService.setId(chefService.getId());

        partialUpdatedChefService.firstName(UPDATED_FIRST_NAME).sexe(UPDATED_SEXE).tel(UPDATED_TEL);

        restChefServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefService))
            )
            .andExpect(status().isOk());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
        ChefService testChefService = chefServiceList.get(chefServiceList.size() - 1);
        assertThat(testChefService.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testChefService.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testChefService.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefService.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testChefService.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void fullUpdateChefServiceWithPatch() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();

        // Update the chefService using partial update
        ChefService partialUpdatedChefService = new ChefService();
        partialUpdatedChefService.setId(chefService.getId());

        partialUpdatedChefService
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL);

        restChefServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChefService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChefService))
            )
            .andExpect(status().isOk());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
        ChefService testChefService = chefServiceList.get(chefServiceList.size() - 1);
        assertThat(testChefService.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testChefService.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testChefService.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testChefService.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testChefService.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void patchNonExistingChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chefService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chefService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChefService() throws Exception {
        int databaseSizeBeforeUpdate = chefServiceRepository.findAll().size();
        chefService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chefService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChefService in the database
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChefService() throws Exception {
        // Initialize the database
        chefServiceRepository.saveAndFlush(chefService);

        int databaseSizeBeforeDelete = chefServiceRepository.findAll().size();

        // Delete the chefService
        restChefServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, chefService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChefService> chefServiceList = chefServiceRepository.findAll();
        assertThat(chefServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
