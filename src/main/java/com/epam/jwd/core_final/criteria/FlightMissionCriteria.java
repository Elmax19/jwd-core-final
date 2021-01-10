package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long distance;
    private final MissionResult missionResult;

    private FlightMissionCriteria(Long id, String name, LocalDate startDate, LocalDate endDate, Long distance, MissionResult missionResult) {
        super(id, name);
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.missionResult = missionResult;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getDistance() {
        return distance;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightMissionCriteria that = (FlightMissionCriteria) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(distance, that.distance) &&
                missionResult == that.missionResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, distance, missionResult);
    }

    public static FlightMissionCriteriaBuilder builder() {
        return new FlightMissionCriteriaBuilder();
    }

    public static class FlightMissionCriteriaBuilder extends CriteriaBuilder<FlightMissionCriteriaBuilder> {
        private Long id;
        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long distance;
        private MissionResult missionResult;

        @Override
        public FlightMissionCriteriaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public FlightMissionCriteriaBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FlightMissionCriteriaBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public FlightMissionCriteriaBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public FlightMissionCriteriaBuilder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public FlightMissionCriteriaBuilder missionResult(MissionResult missionResult) {
            this.missionResult = missionResult;
            return this;
        }

        public FlightMissionCriteria build() {
            return new FlightMissionCriteria(
                    this.id,
                    this.name,
                    this.startDate,
                    this.endDate,
                    this.distance,
                    this.missionResult);
        }
    }
}