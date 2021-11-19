package com.tennisly.club.service.criteria;

import com.tennisly.club.domain.enumeration.ChallengeStatus;
import com.tennisly.club.domain.enumeration.GeneralStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.tennisly.club.domain.Challenge} entity. This class is used
 * in {@link com.tennisly.club.web.rest.ChallengeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /challenges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChallengeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ChallengeStatus
     */
    public static class ChallengeStatusFilter extends Filter<ChallengeStatus> {

        public ChallengeStatusFilter() {}

        public ChallengeStatusFilter(ChallengeStatusFilter filter) {
            super(filter);
        }

        @Override
        public ChallengeStatusFilter copy() {
            return new ChallengeStatusFilter(this);
        }
    }

    /**
     * Class for filtering GeneralStatus
     */
    public static class GeneralStatusFilter extends Filter<GeneralStatus> {

        public GeneralStatusFilter() {}

        public GeneralStatusFilter(GeneralStatusFilter filter) {
            super(filter);
        }

        @Override
        public GeneralStatusFilter copy() {
            return new GeneralStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter matchTime;

    private ChallengeStatusFilter challengeStatus;

    private GeneralStatusFilter status;

    private LongFilter cordId;

    private LongFilter proposerId;

    private LongFilter acceptorId;

    private Boolean distinct;

    public ChallengeCriteria() {}

    public ChallengeCriteria(ChallengeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matchTime = other.matchTime == null ? null : other.matchTime.copy();
        this.challengeStatus = other.challengeStatus == null ? null : other.challengeStatus.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.cordId = other.cordId == null ? null : other.cordId.copy();
        this.proposerId = other.proposerId == null ? null : other.proposerId.copy();
        this.acceptorId = other.acceptorId == null ? null : other.acceptorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ChallengeCriteria copy() {
        return new ChallengeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getMatchTime() {
        return matchTime;
    }

    public InstantFilter matchTime() {
        if (matchTime == null) {
            matchTime = new InstantFilter();
        }
        return matchTime;
    }

    public void setMatchTime(InstantFilter matchTime) {
        this.matchTime = matchTime;
    }

    public ChallengeStatusFilter getChallengeStatus() {
        return challengeStatus;
    }

    public ChallengeStatusFilter challengeStatus() {
        if (challengeStatus == null) {
            challengeStatus = new ChallengeStatusFilter();
        }
        return challengeStatus;
    }

    public void setChallengeStatus(ChallengeStatusFilter challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public GeneralStatusFilter getStatus() {
        return status;
    }

    public GeneralStatusFilter status() {
        if (status == null) {
            status = new GeneralStatusFilter();
        }
        return status;
    }

    public void setStatus(GeneralStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCordId() {
        return cordId;
    }

    public LongFilter cordId() {
        if (cordId == null) {
            cordId = new LongFilter();
        }
        return cordId;
    }

    public void setCordId(LongFilter cordId) {
        this.cordId = cordId;
    }

    public LongFilter getProposerId() {
        return proposerId;
    }

    public LongFilter proposerId() {
        if (proposerId == null) {
            proposerId = new LongFilter();
        }
        return proposerId;
    }

    public void setProposerId(LongFilter proposerId) {
        this.proposerId = proposerId;
    }

    public LongFilter getAcceptorId() {
        return acceptorId;
    }

    public LongFilter acceptorId() {
        if (acceptorId == null) {
            acceptorId = new LongFilter();
        }
        return acceptorId;
    }

    public void setAcceptorId(LongFilter acceptorId) {
        this.acceptorId = acceptorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChallengeCriteria that = (ChallengeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matchTime, that.matchTime) &&
            Objects.equals(challengeStatus, that.challengeStatus) &&
            Objects.equals(status, that.status) &&
            Objects.equals(cordId, that.cordId) &&
            Objects.equals(proposerId, that.proposerId) &&
            Objects.equals(acceptorId, that.acceptorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchTime, challengeStatus, status, cordId, proposerId, acceptorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChallengeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matchTime != null ? "matchTime=" + matchTime + ", " : "") +
            (challengeStatus != null ? "challengeStatus=" + challengeStatus + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (cordId != null ? "cordId=" + cordId + ", " : "") +
            (proposerId != null ? "proposerId=" + proposerId + ", " : "") +
            (acceptorId != null ? "acceptorId=" + acceptorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
