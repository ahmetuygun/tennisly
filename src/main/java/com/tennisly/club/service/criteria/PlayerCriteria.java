package com.tennisly.club.service.criteria;

import com.tennisly.club.domain.enumeration.Gender;
import com.tennisly.club.domain.enumeration.GeneralStatus;
import com.tennisly.club.domain.enumeration.Level;
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
 * Criteria class for the {@link com.tennisly.club.domain.Player} entity. This class is used
 * in {@link com.tennisly.club.web.rest.PlayerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /players?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering Level
     */
    public static class LevelFilter extends Filter<Level> {

        public LevelFilter() {}

        public LevelFilter(LevelFilter filter) {
            super(filter);
        }

        @Override
        public LevelFilter copy() {
            return new LevelFilter(this);
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

    private StringFilter fullName;

    private GenderFilter gender;

    private LevelFilter level;

    private StringFilter phone;

    private GeneralStatusFilter status;

    private LongFilter internalUserId;

    private Boolean distinct;

    public PlayerCriteria() {}

    public PlayerCriteria(PlayerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PlayerCriteria copy() {
        return new PlayerCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public StringFilter fullName() {
        if (fullName == null) {
            fullName = new StringFilter();
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public LevelFilter getLevel() {
        return level;
    }

    public LevelFilter level() {
        if (level == null) {
            level = new LevelFilter();
        }
        return level;
    }

    public void setLevel(LevelFilter level) {
        this.level = level;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            internalUserId = new LongFilter();
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
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
        final PlayerCriteria that = (PlayerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(level, that.level) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(status, that.status) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, gender, level, phone, status, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fullName != null ? "fullName=" + fullName + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
