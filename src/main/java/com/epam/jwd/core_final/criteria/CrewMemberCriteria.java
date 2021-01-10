package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;

import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

import java.util.Objects;


/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private final Role role;
    private final Rank rank;
    private final Boolean isReadyForNextMissions;

    private CrewMemberCriteria(Long id, String name, Role role, Rank rank, Boolean isReadyForNextMissions) {
        super(id, name);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public Boolean getIsReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewMemberCriteria that = (CrewMemberCriteria) o;
        return role == that.role &&
                rank == that.rank &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, rank, isReadyForNextMissions);
    }

    public static CrewMemberCriteriaBuilder builder() {
        return new CrewMemberCriteriaBuilder();
    }

    public static class CrewMemberCriteriaBuilder extends CriteriaBuilder<CrewMemberCriteriaBuilder> {
        private Long id;
        private String name;
        private Role role;
        private Rank rank;
        private Boolean isReadyForNextMissions;

        @Override
        public CrewMemberCriteriaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public CrewMemberCriteriaBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CrewMemberCriteriaBuilder Role(Role role) {
            this.role = role;
            return this;
        }

        public CrewMemberCriteriaBuilder Rank(Rank rank) {
            this.rank = rank;
            return this;
        }

        public CrewMemberCriteriaBuilder isReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
            return this;
        }

        public CrewMemberCriteria build() {
            return new CrewMemberCriteria(
                    this.id,
                    this.name,
                    this.role,
                    this.rank,
                    this.isReadyForNextMissions);
        }
    }

}