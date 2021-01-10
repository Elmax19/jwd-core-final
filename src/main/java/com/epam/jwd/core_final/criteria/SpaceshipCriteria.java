package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Spaceship;

import java.util.Objects;
/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {
    private final Long flightDistance;
    private final Boolean isReadyForNextMissions;

    private SpaceshipCriteria(Long id, String name, Long flightDistance, Boolean isReadyForNextMissions) {
        super(id, name);
        this.flightDistance = flightDistance;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }


    public Long getFlightDistance() {
        return flightDistance;
    }

    public Boolean getIsReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceshipCriteria that = (SpaceshipCriteria) o;
        return Objects.equals(flightDistance, that.flightDistance) &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightDistance, isReadyForNextMissions);
    }

    public static SpaceshipCriteriaBuilder builder() {
        return new SpaceshipCriteriaBuilder();
    }

    public static class SpaceshipCriteriaBuilder extends CriteriaBuilder<SpaceshipCriteriaBuilder> {
        private Long id;
        private String name;
        private Long flightDistance;
        private Boolean isReadyForNextMissions;

        @Override
        public SpaceshipCriteriaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public SpaceshipCriteriaBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SpaceshipCriteriaBuilder flightDistance(Long flightDistance) {
            this.flightDistance = flightDistance;
            return this;
        }

        public SpaceshipCriteriaBuilder isReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
            return this;
        }

        public SpaceshipCriteria build() {
            return new SpaceshipCriteria(
                    this.id,
                    this.name,
                    this.flightDistance,
                    this.isReadyForNextMissions);
        }
    }
}