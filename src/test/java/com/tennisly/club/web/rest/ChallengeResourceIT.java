package com.tennisly.club.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tennisly.club.IntegrationTest;
import com.tennisly.club.domain.Challenge;
import com.tennisly.club.domain.Cord;
import com.tennisly.club.domain.Player;
import com.tennisly.club.domain.enumeration.ChallengeStatus;
import com.tennisly.club.domain.enumeration.GeneralStatus;
import com.tennisly.club.repository.ChallengeRepository;
import com.tennisly.club.service.criteria.ChallengeCriteria;
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

/**
 * Integration tests for the {@link ChallengeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChallengeResourceIT {

    private static final Instant DEFAULT_MATCH_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MATCH_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ChallengeStatus DEFAULT_CHALLENGE_STATUS = ChallengeStatus.REQUESTED;
    private static final ChallengeStatus UPDATED_CHALLENGE_STATUS = ChallengeStatus.ACCEPTED;

    private static final GeneralStatus DEFAULT_STATUS = GeneralStatus.ACTIVE;
    private static final GeneralStatus UPDATED_STATUS = GeneralStatus.PASSIVE;

    private static final String ENTITY_API_URL = "/api/challenges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChallengeMockMvc;

    private Challenge challenge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Challenge createEntity(EntityManager em) {
        Challenge challenge = new Challenge()
            .matchTime(DEFAULT_MATCH_TIME)
            .challengeStatus(DEFAULT_CHALLENGE_STATUS)
            .status(DEFAULT_STATUS);
        return challenge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Challenge createUpdatedEntity(EntityManager em) {
        Challenge challenge = new Challenge()
            .matchTime(UPDATED_MATCH_TIME)
            .challengeStatus(UPDATED_CHALLENGE_STATUS)
            .status(UPDATED_STATUS);
        return challenge;
    }

    @BeforeEach
    public void initTest() {
        challenge = createEntity(em);
    }

    @Test
    @Transactional
    void createChallenge() throws Exception {
        int databaseSizeBeforeCreate = challengeRepository.findAll().size();
        // Create the Challenge
        restChallengeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challenge)))
            .andExpect(status().isCreated());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeCreate + 1);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getMatchTime()).isEqualTo(DEFAULT_MATCH_TIME);
        assertThat(testChallenge.getChallengeStatus()).isEqualTo(DEFAULT_CHALLENGE_STATUS);
        assertThat(testChallenge.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createChallengeWithExistingId() throws Exception {
        // Create the Challenge with an existing ID
        challenge.setId(1L);

        int databaseSizeBeforeCreate = challengeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChallengeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challenge)))
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChallenges() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(challenge.getId().intValue())))
            .andExpect(jsonPath("$.[*].matchTime").value(hasItem(DEFAULT_MATCH_TIME.toString())))
            .andExpect(jsonPath("$.[*].challengeStatus").value(hasItem(DEFAULT_CHALLENGE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get the challenge
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL_ID, challenge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(challenge.getId().intValue()))
            .andExpect(jsonPath("$.matchTime").value(DEFAULT_MATCH_TIME.toString()))
            .andExpect(jsonPath("$.challengeStatus").value(DEFAULT_CHALLENGE_STATUS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getChallengesByIdFiltering() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        Long id = challenge.getId();

        defaultChallengeShouldBeFound("id.equals=" + id);
        defaultChallengeShouldNotBeFound("id.notEquals=" + id);

        defaultChallengeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChallengeShouldNotBeFound("id.greaterThan=" + id);

        defaultChallengeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChallengeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChallengesByMatchTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where matchTime equals to DEFAULT_MATCH_TIME
        defaultChallengeShouldBeFound("matchTime.equals=" + DEFAULT_MATCH_TIME);

        // Get all the challengeList where matchTime equals to UPDATED_MATCH_TIME
        defaultChallengeShouldNotBeFound("matchTime.equals=" + UPDATED_MATCH_TIME);
    }

    @Test
    @Transactional
    void getAllChallengesByMatchTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where matchTime not equals to DEFAULT_MATCH_TIME
        defaultChallengeShouldNotBeFound("matchTime.notEquals=" + DEFAULT_MATCH_TIME);

        // Get all the challengeList where matchTime not equals to UPDATED_MATCH_TIME
        defaultChallengeShouldBeFound("matchTime.notEquals=" + UPDATED_MATCH_TIME);
    }

    @Test
    @Transactional
    void getAllChallengesByMatchTimeIsInShouldWork() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where matchTime in DEFAULT_MATCH_TIME or UPDATED_MATCH_TIME
        defaultChallengeShouldBeFound("matchTime.in=" + DEFAULT_MATCH_TIME + "," + UPDATED_MATCH_TIME);

        // Get all the challengeList where matchTime equals to UPDATED_MATCH_TIME
        defaultChallengeShouldNotBeFound("matchTime.in=" + UPDATED_MATCH_TIME);
    }

    @Test
    @Transactional
    void getAllChallengesByMatchTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where matchTime is not null
        defaultChallengeShouldBeFound("matchTime.specified=true");

        // Get all the challengeList where matchTime is null
        defaultChallengeShouldNotBeFound("matchTime.specified=false");
    }

    @Test
    @Transactional
    void getAllChallengesByChallengeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where challengeStatus equals to DEFAULT_CHALLENGE_STATUS
        defaultChallengeShouldBeFound("challengeStatus.equals=" + DEFAULT_CHALLENGE_STATUS);

        // Get all the challengeList where challengeStatus equals to UPDATED_CHALLENGE_STATUS
        defaultChallengeShouldNotBeFound("challengeStatus.equals=" + UPDATED_CHALLENGE_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByChallengeStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where challengeStatus not equals to DEFAULT_CHALLENGE_STATUS
        defaultChallengeShouldNotBeFound("challengeStatus.notEquals=" + DEFAULT_CHALLENGE_STATUS);

        // Get all the challengeList where challengeStatus not equals to UPDATED_CHALLENGE_STATUS
        defaultChallengeShouldBeFound("challengeStatus.notEquals=" + UPDATED_CHALLENGE_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByChallengeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where challengeStatus in DEFAULT_CHALLENGE_STATUS or UPDATED_CHALLENGE_STATUS
        defaultChallengeShouldBeFound("challengeStatus.in=" + DEFAULT_CHALLENGE_STATUS + "," + UPDATED_CHALLENGE_STATUS);

        // Get all the challengeList where challengeStatus equals to UPDATED_CHALLENGE_STATUS
        defaultChallengeShouldNotBeFound("challengeStatus.in=" + UPDATED_CHALLENGE_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByChallengeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where challengeStatus is not null
        defaultChallengeShouldBeFound("challengeStatus.specified=true");

        // Get all the challengeList where challengeStatus is null
        defaultChallengeShouldNotBeFound("challengeStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllChallengesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where status equals to DEFAULT_STATUS
        defaultChallengeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the challengeList where status equals to UPDATED_STATUS
        defaultChallengeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where status not equals to DEFAULT_STATUS
        defaultChallengeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the challengeList where status not equals to UPDATED_STATUS
        defaultChallengeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultChallengeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the challengeList where status equals to UPDATED_STATUS
        defaultChallengeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllChallengesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where status is not null
        defaultChallengeShouldBeFound("status.specified=true");

        // Get all the challengeList where status is null
        defaultChallengeShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllChallengesByCordIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);
        Cord cord;
        if (TestUtil.findAll(em, Cord.class).isEmpty()) {
            cord = CordResourceIT.createEntity(em);
            em.persist(cord);
            em.flush();
        } else {
            cord = TestUtil.findAll(em, Cord.class).get(0);
        }
        em.persist(cord);
        em.flush();
        challenge.setCord(cord);
        challengeRepository.saveAndFlush(challenge);
        Long cordId = cord.getId();

        // Get all the challengeList where cord equals to cordId
        defaultChallengeShouldBeFound("cordId.equals=" + cordId);

        // Get all the challengeList where cord equals to (cordId + 1)
        defaultChallengeShouldNotBeFound("cordId.equals=" + (cordId + 1));
    }

    @Test
    @Transactional
    void getAllChallengesByProposerIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);
        Player proposer;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            proposer = PlayerResourceIT.createEntity(em);
            em.persist(proposer);
            em.flush();
        } else {
            proposer = TestUtil.findAll(em, Player.class).get(0);
        }
        em.persist(proposer);
        em.flush();
        challenge.setProposer(proposer);
        challengeRepository.saveAndFlush(challenge);
        Long proposerId = proposer.getId();

        // Get all the challengeList where proposer equals to proposerId
        defaultChallengeShouldBeFound("proposerId.equals=" + proposerId);

        // Get all the challengeList where proposer equals to (proposerId + 1)
        defaultChallengeShouldNotBeFound("proposerId.equals=" + (proposerId + 1));
    }

    @Test
    @Transactional
    void getAllChallengesByAcceptorIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);
        Player acceptor;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            acceptor = PlayerResourceIT.createEntity(em);
            em.persist(acceptor);
            em.flush();
        } else {
            acceptor = TestUtil.findAll(em, Player.class).get(0);
        }
        em.persist(acceptor);
        em.flush();
        challenge.setAcceptor(acceptor);
        challengeRepository.saveAndFlush(challenge);
        Long acceptorId = acceptor.getId();

        // Get all the challengeList where acceptor equals to acceptorId
        defaultChallengeShouldBeFound("acceptorId.equals=" + acceptorId);

        // Get all the challengeList where acceptor equals to (acceptorId + 1)
        defaultChallengeShouldNotBeFound("acceptorId.equals=" + (acceptorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChallengeShouldBeFound(String filter) throws Exception {
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(challenge.getId().intValue())))
            .andExpect(jsonPath("$.[*].matchTime").value(hasItem(DEFAULT_MATCH_TIME.toString())))
            .andExpect(jsonPath("$.[*].challengeStatus").value(hasItem(DEFAULT_CHALLENGE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChallengeShouldNotBeFound(String filter) throws Exception {
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChallengeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChallenge() throws Exception {
        // Get the challenge
        restChallengeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Update the challenge
        Challenge updatedChallenge = challengeRepository.findById(challenge.getId()).get();
        // Disconnect from session so that the updates on updatedChallenge are not directly saved in db
        em.detach(updatedChallenge);
        updatedChallenge.matchTime(UPDATED_MATCH_TIME).challengeStatus(UPDATED_CHALLENGE_STATUS).status(UPDATED_STATUS);

        restChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChallenge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChallenge))
            )
            .andExpect(status().isOk());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getMatchTime()).isEqualTo(UPDATED_MATCH_TIME);
        assertThat(testChallenge.getChallengeStatus()).isEqualTo(UPDATED_CHALLENGE_STATUS);
        assertThat(testChallenge.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, challenge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(challenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(challenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challenge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChallengeWithPatch() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Update the challenge using partial update
        Challenge partialUpdatedChallenge = new Challenge();
        partialUpdatedChallenge.setId(challenge.getId());

        partialUpdatedChallenge.challengeStatus(UPDATED_CHALLENGE_STATUS);

        restChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChallenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChallenge))
            )
            .andExpect(status().isOk());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getMatchTime()).isEqualTo(DEFAULT_MATCH_TIME);
        assertThat(testChallenge.getChallengeStatus()).isEqualTo(UPDATED_CHALLENGE_STATUS);
        assertThat(testChallenge.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateChallengeWithPatch() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Update the challenge using partial update
        Challenge partialUpdatedChallenge = new Challenge();
        partialUpdatedChallenge.setId(challenge.getId());

        partialUpdatedChallenge.matchTime(UPDATED_MATCH_TIME).challengeStatus(UPDATED_CHALLENGE_STATUS).status(UPDATED_STATUS);

        restChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChallenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChallenge))
            )
            .andExpect(status().isOk());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getMatchTime()).isEqualTo(UPDATED_MATCH_TIME);
        assertThat(testChallenge.getChallengeStatus()).isEqualTo(UPDATED_CHALLENGE_STATUS);
        assertThat(testChallenge.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, challenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(challenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(challenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();
        challenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(challenge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeDelete = challengeRepository.findAll().size();

        // Delete the challenge
        restChallengeMockMvc
            .perform(delete(ENTITY_API_URL_ID, challenge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
