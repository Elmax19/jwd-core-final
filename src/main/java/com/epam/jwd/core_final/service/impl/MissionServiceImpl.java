package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.service.BaseEntityService;
import com.epam.jwd.core_final.service.MissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MissionServiceImpl extends BaseEntityService<FlightMission> implements MissionService {
    private final static MissionServiceImpl INSTANCE = new MissionServiceImpl();

    private MissionServiceImpl() {
        super(FlightMission.class);
    }

    public static MissionServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<FlightMission> findAllMissions() {
        return new ArrayList<>(super.findAll());
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        FlightMissionCriteria flightMissionCriteria = (FlightMissionCriteria) criteria;
        return super.findAll().stream()
                .filter(Objects::nonNull)
                .filter(crew -> flightMissionCriteria.getId() == null
                        || crew.getId().equals(flightMissionCriteria.getId()))
                .filter(crew -> flightMissionCriteria.getName() == null
                        || crew.getName().equals(flightMissionCriteria.getName()))
                .filter(crew -> flightMissionCriteria.getStartDate() == null
                        || crew.getStartDate().equals(flightMissionCriteria.getStartDate()))
                .filter(crew -> flightMissionCriteria.getEndDate() == null
                        || crew.getEndDate().equals(flightMissionCriteria.getEndDate()))
                .filter(crew -> flightMissionCriteria.getDistance() == null
                        || crew.getDistance().equals(flightMissionCriteria.getDistance()))
                .filter(crew -> flightMissionCriteria.getMissionResult() == null
                        || crew.getMissionResult().equals(flightMissionCriteria.getMissionResult()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        if (criteria == null) {
            return Optional.empty();
        } else {
            FlightMissionCriteria flightMissionCriteria = (FlightMissionCriteria) criteria;
            return findAllMissionsByCriteria(flightMissionCriteria).stream()
                    .findFirst();
        }
    }

    @Override
    public FlightMission updateMissionDetails(FlightMission flightMission) {
        FlightMission previousFlightMission = super.findAll().stream()
                .filter(mission -> mission.getName().equals(flightMission.getName()))
                .findFirst()
                .get();
        if (previousFlightMission.getAssignedCrew().size() > 0 || previousFlightMission.getAssignedSpaceship() != null) {
            flightMission.reduceCount();
            return null;
        }
        super.findAll().remove(previousFlightMission);
        super.findAll().add(flightMission);
        return flightMission;
    }

    @Override
    public FlightMission createMission(FlightMission flightMission) {
        if (super.findAll().stream()
                .anyMatch(mission -> mission.getName().equals(flightMission.getName()))) {
            flightMission.reduceCount();
            return null;
        }
        return flightMission;
    }
}
