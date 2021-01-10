package com.epam.jwd.core_final.domain;
import java.util.Objects;


/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {

    private static Long count = 0L;
    private final Role role;
    private final Rank rank;
    private Boolean isReadyForNextMissions;

    public CrewMember(String name, Role role, Rank rank) {
        super(name, ++count);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = true;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewMember that = (CrewMember) o;
        return role == that.role &&
                rank == that.rank &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, rank, isReadyForNextMissions);
    }

    public Boolean getIsReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setNotReadyForNextMissions() {
        this.isReadyForNextMissions = false;
    }

    @Override
    public void reduceCount() {
        count--;
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                super.toString() +
                ", role=" + role +
                ", rank=" + rank +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}