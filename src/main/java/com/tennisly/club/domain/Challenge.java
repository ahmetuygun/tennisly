package com.tennisly.club.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tennisly.club.domain.enumeration.ChallengeStatus;
import com.tennisly.club.domain.enumeration.GeneralStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Challenge.
 */
@Entity
@Table(name = "challenge")
public class Challenge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "match_time")
    private Instant matchTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_status")
    private ChallengeStatus challengeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status;

    @JsonIgnoreProperties(value = { "challenge" }, allowSetters = true)
    @ManyToOne
    private Cord cord;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser" }, allowSetters = true)
    private Player proposer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser" }, allowSetters = true)
    private Player acceptor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Challenge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMatchTime() {
        return this.matchTime;
    }

    public Challenge matchTime(Instant matchTime) {
        this.setMatchTime(matchTime);
        return this;
    }

    public void setMatchTime(Instant matchTime) {
        this.matchTime = matchTime;
    }

    public ChallengeStatus getChallengeStatus() {
        return this.challengeStatus;
    }

    public Challenge challengeStatus(ChallengeStatus challengeStatus) {
        this.setChallengeStatus(challengeStatus);
        return this;
    }

    public void setChallengeStatus(ChallengeStatus challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public GeneralStatus getStatus() {
        return this.status;
    }

    public Challenge status(GeneralStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(GeneralStatus status) {
        this.status = status;
    }

    public Cord getCord() {
        return this.cord;
    }

    public void setCord(Cord cord) {
        this.cord = cord;
    }

    public Challenge cord(Cord cord) {
        this.setCord(cord);
        return this;
    }

    public Player getProposer() {
        return this.proposer;
    }

    public void setProposer(Player player) {
        this.proposer = player;
    }

    public Challenge proposer(Player player) {
        this.setProposer(player);
        return this;
    }

    public Player getAcceptor() {
        return this.acceptor;
    }

    public void setAcceptor(Player player) {
        this.acceptor = player;
    }

    public Challenge acceptor(Player player) {
        this.setAcceptor(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Challenge)) {
            return false;
        }
        return id != null && id.equals(((Challenge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Challenge{" +
            "id=" + getId() +
            ", matchTime='" + getMatchTime() + "'" +
            ", challengeStatus='" + getChallengeStatus() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
