package sn.esp.gestion.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.esp.gestion.domain.ChefService;
import sn.esp.gestion.repository.ChefServiceRepository;
import sn.esp.gestion.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.esp.gestion.domain.ChefService}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChefServiceResource {

    private final Logger log = LoggerFactory.getLogger(ChefServiceResource.class);

    private static final String ENTITY_NAME = "chefService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChefServiceRepository chefServiceRepository;

    public ChefServiceResource(ChefServiceRepository chefServiceRepository) {
        this.chefServiceRepository = chefServiceRepository;
    }

    /**
     * {@code POST  /chef-services} : Create a new chefService.
     *
     * @param chefService the chefService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chefService, or with status {@code 400 (Bad Request)} if the chefService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chef-services")
    public ResponseEntity<ChefService> createChefService(@Valid @RequestBody ChefService chefService) throws URISyntaxException {
        log.debug("REST request to save ChefService : {}", chefService);
        if (chefService.getId() != null) {
            throw new BadRequestAlertException("A new chefService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChefService result = chefServiceRepository.save(chefService);
        return ResponseEntity
            .created(new URI("/api/chef-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chef-services/:id} : Updates an existing chefService.
     *
     * @param id the id of the chefService to save.
     * @param chefService the chefService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefService,
     * or with status {@code 400 (Bad Request)} if the chefService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chefService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chef-services/{id}")
    public ResponseEntity<ChefService> updateChefService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChefService chefService
    ) throws URISyntaxException {
        log.debug("REST request to update ChefService : {}, {}", id, chefService);
        if (chefService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChefService result = chefServiceRepository.save(chefService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chefService.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chef-services/:id} : Partial updates given fields of an existing chefService, field will ignore if it is null
     *
     * @param id the id of the chefService to save.
     * @param chefService the chefService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chefService,
     * or with status {@code 400 (Bad Request)} if the chefService is not valid,
     * or with status {@code 404 (Not Found)} if the chefService is not found,
     * or with status {@code 500 (Internal Server Error)} if the chefService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chef-services/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChefService> partialUpdateChefService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChefService chefService
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChefService partially : {}, {}", id, chefService);
        if (chefService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chefService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChefService> result = chefServiceRepository
            .findById(chefService.getId())
            .map(
                existingChefService -> {
                    if (chefService.getFirstName() != null) {
                        existingChefService.setFirstName(chefService.getFirstName());
                    }
                    if (chefService.getLastName() != null) {
                        existingChefService.setLastName(chefService.getLastName());
                    }
                    if (chefService.getSexe() != null) {
                        existingChefService.setSexe(chefService.getSexe());
                    }
                    if (chefService.getEmail() != null) {
                        existingChefService.setEmail(chefService.getEmail());
                    }
                    if (chefService.getTel() != null) {
                        existingChefService.setTel(chefService.getTel());
                    }

                    return existingChefService;
                }
            )
            .map(chefServiceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chefService.getId().toString())
        );
    }

    /**
     * {@code GET  /chef-services} : get all the chefServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chefServices in body.
     */
    @GetMapping("/chef-services")
    public ResponseEntity<List<ChefService>> getAllChefServices(Pageable pageable) {
        log.debug("REST request to get a page of ChefServices");
        Page<ChefService> page = chefServiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chef-services/:id} : get the "id" chefService.
     *
     * @param id the id of the chefService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chefService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chef-services/{id}")
    public ResponseEntity<ChefService> getChefService(@PathVariable Long id) {
        log.debug("REST request to get ChefService : {}", id);
        Optional<ChefService> chefService = chefServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chefService);
    }

    /**
     * {@code DELETE  /chef-services/:id} : delete the "id" chefService.
     *
     * @param id the id of the chefService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chef-services/{id}")
    public ResponseEntity<Void> deleteChefService(@PathVariable Long id) {
        log.debug("REST request to delete ChefService : {}", id);
        chefServiceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
