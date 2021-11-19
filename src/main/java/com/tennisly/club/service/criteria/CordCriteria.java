package com.tennisly.club.service.criteria;

import com.tennisly.club.domain.enumeration.GeneralStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.tennisly.club.domain.Cord} entity. This class is used
 * in {@link com.tennisly.club.web.rest.CordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cords?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CordCriteria implements Serializable, Criteria {

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

    private StringFilter name;

    private StringFilter adress;

    private GeneralStatusFilter status;

    private LongFilter challengeId;

    private Boolean distinct;

    public CordCriteria() {}

    public CordCriteria(CordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.challengeId = other.challengeId == null ? null : other.challengeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CordCriteria copy() {
        return new CordCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAdress() {
        return adress;
    }

    public StringFilter adress() {
        if (adress == null) {
            adress = new StringFilter();
        }
        return adress;
    }

    public void setAdress(StringFilter adress) {
        this.adress = adress;
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

    public LongFilter getChallengeId() {
        return challengeId;
    }

    public LongFilter challengeId() {
        if (challengeId == null) {
            challengeId = new LongFilter();
        }
        return challengeId;
    }

    public void setChallengeId(LongFilter challengeId) {
        this.challengeId = challengeId;
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
        final CordCriteria that = (CordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(status, that.status) &&
            Objects.equals(challengeId, that.challengeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, adress, status, challengeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (adress != null ? "adress=" + adress + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (challengeId != null ? "challengeId=" + challengeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
