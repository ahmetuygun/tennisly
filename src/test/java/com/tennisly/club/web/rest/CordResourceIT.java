package com.tennisly.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tennisly.club.IntegrationTest;
import com.tennisly.club.domain.Challenge;
import com.tennisly.club.domain.Cord;
import com.tennisly.club.domain.enumeration.GeneralStatus;
import com.tennisly.club.repository.CordRepository;
import com.tennisly.club.service.criteria.CordCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CordResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final GeneralStatus DEFAULT_STATUS = GeneralStatus.ACTIVE;
    private static final GeneralStatus UPDATED_STATUS = GeneralStatus.PASSIVE;

    private static final String ENTITY_API_URL = "/api/cords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CordRepository cordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCordMockMvc;

    private Cord cord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cord createEntity(EntityManager em) {
        Cord cord = new Cord()
            .name(DEFAULT_NAME)
            .adress(DEFAULT_ADRESS)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return cord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cord createUpdatedEntity(EntityManager em) {
        Cord cord = new Cord()
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return cord;
    }

    @BeforeEach
    public void initTest() {
        cord = createEntity(em);
    }

    @Test
    @Transactional
    void createCord() throws Exception {
        int databaseSizeBeforeCreate = cordRepository.findAll().size();
        // Create the Cord
        restCordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cord)))
            .andExpect(status().isCreated());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeCreate + 1);
        Cord testCord = cordList.get(cordList.size() - 1);
        assertThat(testCord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCord.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testCord.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCord.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCord.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCordWithExistingId() throws Exception {
        // Create the Cord with an existing ID
        cord.setId(1L);

        int databaseSizeBeforeCreate = cordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cord)))
            .andExpect(status().isBadRequest());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCords() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList
        restCordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getCord() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get the cord
        restCordMockMvc
            .perform(get(ENTITY_API_URL_ID, cord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cord.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getCordsByIdFiltering() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        Long id = cord.getId();

        defaultCordShouldBeFound("id.equals=" + id);
        defaultCordShouldNotBeFound("id.notEquals=" + id);

        defaultCordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCordShouldNotBeFound("id.greaterThan=" + id);

        defaultCordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCordsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name equals to DEFAULT_NAME
        defaultCordShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cordList where name equals to UPDATED_NAME
        defaultCordShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCordsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name not equals to DEFAULT_NAME
        defaultCordShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the cordList where name not equals to UPDATED_NAME
        defaultCordShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCordsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCordShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cordList where name equals to UPDATED_NAME
        defaultCordShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCordsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name is not null
        defaultCordShouldBeFound("name.specified=true");

        // Get all the cordList where name is null
        defaultCordShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCordsByNameContainsSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name contains DEFAULT_NAME
        defaultCordShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cordList where name contains UPDATED_NAME
        defaultCordShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCordsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where name does not contain DEFAULT_NAME
        defaultCordShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cordList where name does not contain UPDATED_NAME
        defaultCordShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCordsByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress equals to DEFAULT_ADRESS
        defaultCordShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the cordList where adress equals to UPDATED_ADRESS
        defaultCordShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCordsByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress not equals to DEFAULT_ADRESS
        defaultCordShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the cordList where adress not equals to UPDATED_ADRESS
        defaultCordShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCordsByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultCordShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the cordList where adress equals to UPDATED_ADRESS
        defaultCordShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCordsByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress is not null
        defaultCordShouldBeFound("adress.specified=true");

        // Get all the cordList where adress is null
        defaultCordShouldNotBeFound("adress.specified=false");
    }

    @Test
    @Transactional
    void getAllCordsByAdressContainsSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress contains DEFAULT_ADRESS
        defaultCordShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the cordList where adress contains UPDATED_ADRESS
        defaultCordShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCordsByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where adress does not contain DEFAULT_ADRESS
        defaultCordShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the cordList where adress does not contain UPDATED_ADRESS
        defaultCordShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCordsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where status equals to DEFAULT_STATUS
        defaultCordShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the cordList where status equals to UPDATED_STATUS
        defaultCordShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCordsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where status not equals to DEFAULT_STATUS
        defaultCordShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the cordList where status not equals to UPDATED_STATUS
        defaultCordShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCordsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCordShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the cordList where status equals to UPDATED_STATUS
        defaultCordShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCordsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        // Get all the cordList where status is not null
        defaultCordShouldBeFound("status.specified=true");

        // Get all the cordList where status is null
        defaultCordShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCordShouldBeFound(String filter) throws Exception {
        restCordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restCordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCordShouldNotBeFound(String filter) throws Exception {
        restCordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCord() throws Exception {
        // Get the cord
        restCordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCord() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        int databaseSizeBeforeUpdate = cordRepository.findAll().size();

        // Update the cord
        Cord updatedCord = cordRepository.findById(cord.getId()).get();
        // Disconnect from session so that the updates on updatedCord are not directly saved in db
        em.detach(updatedCord);
        updatedCord
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restCordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCord))
            )
            .andExpect(status().isOk());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
        Cord testCord = cordList.get(cordList.size() - 1);
        assertThat(testCord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCord.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCord.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCord.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCord.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCordWithPatch() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        int databaseSizeBeforeUpdate = cordRepository.findAll().size();

        // Update the cord using partial update
        Cord partialUpdatedCord = new Cord();
        partialUpdatedCord.setId(cord.getId());

        partialUpdatedCord.adress(UPDATED_ADRESS);

        restCordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCord))
            )
            .andExpect(status().isOk());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
        Cord testCord = cordList.get(cordList.size() - 1);
        assertThat(testCord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCord.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCord.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCord.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCord.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCordWithPatch() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        int databaseSizeBeforeUpdate = cordRepository.findAll().size();

        // Update the cord using partial update
        Cord partialUpdatedCord = new Cord();
        partialUpdatedCord.setId(cord.getId());

        partialUpdatedCord
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restCordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCord))
            )
            .andExpect(status().isOk());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
        Cord testCord = cordList.get(cordList.size() - 1);
        assertThat(testCord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCord.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testCord.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCord.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCord.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCord() throws Exception {
        int databaseSizeBeforeUpdate = cordRepository.findAll().size();
        cord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cord in the database
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCord() throws Exception {
        // Initialize the database
        cordRepository.saveAndFlush(cord);

        int databaseSizeBeforeDelete = cordRepository.findAll().size();

        // Delete the cord
        restCordMockMvc
            .perform(delete(ENTITY_API_URL_ID, cord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cord> cordList = cordRepository.findAll();
        assertThat(cordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
