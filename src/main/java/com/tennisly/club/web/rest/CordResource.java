package com.tennisly.club.web.rest;

import com.tennisly.club.domain.Cord;
import com.tennisly.club.repository.CordRepository;
import com.tennisly.club.service.CordQueryService;
import com.tennisly.club.service.CordService;
import com.tennisly.club.service.criteria.CordCriteria;
import com.tennisly.club.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tennisly.club.domain.Cord}.
 */
@RestController
@RequestMapping("/api")
public class CordResource {

    private final Logger log = LoggerFactory.getLogger(CordResource.class);

    private static final String ENTITY_NAME = "cord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CordService cordService;

    private final CordRepository cordRepository;

    private final CordQueryService cordQueryService;

    public CordResource(CordService cordService, CordRepository cordRepository, CordQueryService cordQueryService) {
        this.cordService = cordService;
        this.cordRepository = cordRepository;
        this.cordQueryService = cordQueryService;
    }

    /**
     * {@code POST  /cords} : Create a new cord.
     *
     * @param cord the cord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cord, or with status {@code 400 (Bad Request)} if the cord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cords")
    public ResponseEntity<Cord> createCord(@RequestBody Cord cord) throws URISyntaxException {
        log.debug("REST request to save Cord : {}", cord);
        if (cord.getId() != null) {
            throw new BadRequestAlertException("A new cord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cord result = cordService.save(cord);
        return ResponseEntity
            .created(new URI("/api/cords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cords/:id} : Updates an existing cord.
     *
     * @param id the id of the cord to save.
     * @param cord the cord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cord,
     * or with status {@code 400 (Bad Request)} if the cord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cords/{id}")
    public ResponseEntity<Cord> updateCord(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cord cord)
        throws URISyntaxException {
        log.debug("REST request to update Cord : {}, {}", id, cord);
        if (cord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cord result = cordService.save(cord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cord.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cords/:id} : Partial updates given fields of an existing cord, field will ignore if it is null
     *
     * @param id the id of the cord to save.
     * @param cord the cord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cord,
     * or with status {@code 400 (Bad Request)} if the cord is not valid,
     * or with status {@code 404 (Not Found)} if the cord is not found,
     * or with status {@code 500 (Internal Server Error)} if the cord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cords/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cord> partialUpdateCord(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cord cord)
        throws URISyntaxException {
        log.debug("REST request to partial update Cord partially : {}, {}", id, cord);
        if (cord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cord> result = cordService.partialUpdate(cord);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cord.getId().toString())
        );
    }

    /**
     * {@code GET  /cords} : get all the cords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cords in body.
     */
    @GetMapping("/cords")
    public ResponseEntity<List<Cord>> getAllCords(CordCriteria criteria) {
        log.debug("REST request to get Cords by criteria: {}", criteria);
        List<Cord> entityList = cordQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cords/count} : count all the cords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cords/count")
    public ResponseEntity<Long> countCords(CordCriteria criteria) {
        log.debug("REST request to count Cords by criteria: {}", criteria);
        return ResponseEntity.ok().body(cordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cords/:id} : get the "id" cord.
     *
     * @param id the id of the cord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cords/{id}")
    public ResponseEntity<Cord> getCord(@PathVariable Long id) {
        log.debug("REST request to get Cord : {}", id);
        Optional<Cord> cord = cordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cord);
    }

    /**
     * {@code DELETE  /cords/:id} : delete the "id" cord.
     *
     * @param id the id of the cord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cords/{id}")
    public ResponseEntity<Void> deleteCord(@PathVariable Long id) {
        log.debug("REST request to delete Cord : {}", id);
        cordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
