package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.HaveAssignedEntityException;
import com.epam.jwd.core_final.exception.DeadEntityException;
import com.epam.jwd.core_final.exception.DuplicateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.service.BaseEntityService;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpaceshipServiceImpl extends BaseEntityService<Spaceship> implements SpaceshipService {
    private final static SpaceshipServiceImpl INSTANCE = new SpaceshipServiceImpl();

    private SpaceshipServiceImpl() {
        super(Spaceship.class);
    }

    public static SpaceshipServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Spaceship> findAllSpaceships() {
        return new ArrayList<>(super.findAll());
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;
        return findAllSpaceships().stream()
                .filter(spaceship -> spaceshipCriteria.getId() == null
                        || spaceship.getId().equals(spaceshipCriteria.getId()))
                .filter(spaceship -> spaceshipCriteria.getName() == null
                        || spaceship.getName().equals(spaceshipCriteria.getName()))
                .filter(spaceship -> spaceshipCriteria.getFlightDistance() == null
                        || spaceship.getFlightDistance().equals(spaceshipCriteria.getFlightDistance()))
                .filter(spaceship -> spaceshipCriteria.getIsReadyForNextMissions() == null
                        || spaceship.getIsReadyForNextMissions().equals(spaceshipCriteria.getIsReadyForNextMissions()))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        if (criteria == null) {
            return Optional.empty();
        } else {
            SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;
            return findAllSpaceshipsByCriteria(spaceshipCriteria).stream()
                    .findFirst();
        }
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship spaceship) {
        Spaceship previousShip = super.findAll().stream()
                .filter(ship -> ship.getName().equals(spaceship.getName()))
                .findFirst()
                .get();
        if (!previousShip.getIsReadyForNextMissions()) {
            spaceship.reduceCount();
            return null;
        }
        super.findAll().remove(previousShip);
        super.findAll().add(spaceship);
        return spaceship;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship, String missionName) throws RuntimeException {
        if (!spaceship.getIsReadyForNextMissions()) {
            throw new DeadEntityException("Spaceship is dead!");
        }
        Optional<FlightMission> flightMission = MissionServiceImpl.getInstance().findMissionByCriteria(FlightMissionCriteria.builder()
                .name(missionName)
                .build());
        if (flightMission.isPresent()) {
            if (flightMission.get().getAssignedSpaceship() != null) {
                throw new HaveAssignedEntityException("Such FlightMission already have assigned Spaceship");
            }
            flightMission.get().setAssignedSpaceShift(spaceship);
            if (flightMission.get().getMissionResult().equals(MissionResult.FAILED)) {
                spaceship.setNotReadyForNextMissions();
            }
            spaceship.changeFlightDistance(flightMission.get().getDistance());
        } else {
            throw new UnknownEntityException("No such FlightMission");
        }
    }

    @Override
    public Spaceship createSpaceship(Spaceship spaceship) throws RuntimeException {
        if (super.findAll().stream()
                .anyMatch(ship -> ship.getName().equals(spaceship.getName()))) {
            spaceship.reduceCount();
            throw new DuplicateException("This Spaceship already exists!");
        }
        return spaceship;
    }
}