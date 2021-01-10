package com.epam.jwd.core_final.domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * missions name {@link String}
 * start date {@link java.time.LocalDate}
 * end date {@link java.time.LocalDate}
 * distance {@link Long} - missions distance
 * assignedSpaceShift {@link Spaceship} - not defined by default
 * assignedCrew {@link java.util.List<CrewMember>} - list of missions members based on ship capacity - not defined by default
 * missionResult {@link MissionResult}
 */
public class FlightMission extends AbstractBaseEntity {
    private static Long count = 0L;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long distance;
    private Spaceship assignedSpaceShift;
    private final List<CrewMember> assignedCrew = new ArrayList<>();
    private MissionResult missionResult;

    private FlightMission(String name, LocalDate startDate, LocalDate endDate, Long distance,
                          MissionResult missionResult) {
        super(name, ++count);
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

    public Spaceship getAssignedSpaceship() {
        return assignedSpaceShift;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightMission that = (FlightMission) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(assignedSpaceShift, that.assignedSpaceShift) &&
                Objects.equals(assignedCrew, that.assignedCrew) &&
                missionResult == that.missionResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, distance, assignedSpaceShift, assignedCrew, missionResult);
    }

    public void setAssignedSpaceShift(Spaceship assignedSpaceShift) {
        this.assignedSpaceShift = assignedSpaceShift;
    }

    public void cancelMission(){
        this.missionResult=MissionResult.CANCELLED;
    }

    public void addCrewMember(CrewMember crewMember) {
        this.assignedCrew.add(crewMember);
    }

    public static FlightMission.Builder builder() {
        return new FlightMission.Builder();
    }

    @Override
    public void reduceCount() {
        count--;
    }

    public static class Builder {
        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long distance;
        private MissionResult missionResult;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Builder missionResult(MissionResult missionResult) {
            this.missionResult = missionResult;
            return this;
        }

        public FlightMission build() {
            return new FlightMission(
                    this.name,
                    this.startDate,
                    this.endDate,
                    this.distance,
                    this.missionResult);
        }
    }

    @Override
    public String toString() {
        StringBuilder crew = new StringBuilder();
        for (CrewMember crewMember : assignedCrew) {
            crew.append(crewMember.getName()).append(",");
        }
        if (crew.length() == 0) {
            crew.append("null");
        }
        if (assignedSpaceShift != null) {
            return "FlightMission{" +
                    super.toString() +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", distance=" + distance +
                    ", assignedSpaceShift=" + assignedSpaceShift.getName() +
                    ", assignedCrew=" + crew +
                    " missionResult=" + missionResult +
                    '}';
        }
        return "FlightMission{" +
                super.toString() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", distance=" + distance +
                ", assignedSpaceShift=" + assignedSpaceShift +
                ", assignedCrew=" + crew +
                ", missionResult=" + missionResult +
                '}';
    }
}