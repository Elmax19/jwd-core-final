package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DeadEntityException;
import com.epam.jwd.core_final.exception.DuplicateException;
import com.epam.jwd.core_final.exception.HaveAssignedEntityException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.service.BaseEntityService;
import com.epam.jwd.core_final.service.CrewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrewServiceImpl extends BaseEntityService<CrewMember> implements CrewService {
    private final static CrewServiceImpl INSTANCE = new CrewServiceImpl();

    private CrewServiceImpl() {
        super(CrewMember.class);
    }

    public static CrewServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return new ArrayList<>(super.findAll());
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
        return super.findAll().stream()
                .filter(Objects::nonNull)
                .filter(crew -> crewMemberCriteria.getId() == null
                        || crew.getId().equals(crewMemberCriteria.getId()))
                .filter(crew -> crewMemberCriteria.getName() == null
                        || crew.getName().equals(crewMemberCriteria.getName()))
                .filter(crew -> crewMemberCriteria.getRole() == null
                        || crew.getRole().equals(crewMemberCriteria.getRole()))
                .filter(crew -> crewMemberCriteria.getRank() == null
                        || crew.getRank().equals(crewMemberCriteria.getRank()))
                .filter(crew -> crewMemberCriteria.getIsReadyForNextMissions() == null
                        || crew.getIsReadyForNextMissions().equals(crewMemberCriteria.getIsReadyForNextMissions()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        if (criteria == null) {
            return Optional.empty();
        } else {
            CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
            return findAllCrewMembersByCriteria(crewMemberCriteria).stream()
                    .findFirst();
        }
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        CrewMember previousMember = super.findAll().stream()
                .filter(crew -> crew.getName().equals(crewMember.getName()))
                .findAny()
                .get();
        if (!previousMember.getIsReadyForNextMissions()) {
            crewMember.reduceCount();
            return null;
        }
        super.findAll().remove(previousMember);
        super.findAll().add(crewMember);
        return crewMember;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember, String missionName) throws RuntimeException {
        if (!crewMember.getIsReadyForNextMissions()) {
            throw new DeadEntityException("CrewMember is dead!");
        }
        Optional<FlightMission> flightMission
                = MissionServiceImpl.getInstance().findMissionByCriteria(FlightMissionCriteria.builder()
                .name(missionName)
                .build());
        if (flightMission.isPresent()) {
            if (flightMission.get().getAssignedSpaceship() == null) {
                throw new UnknownEntityException("There is no assigned Spaceship on such FlightMission");
            }
            if (flightMission.get().getAssignedCrew().stream()
                    .anyMatch(crew -> crew.getName().equals(crewMember.getName()))) {
                throw new HaveAssignedEntityException("This CrewMember is already assigned on such FlightMission");
            }
            Spaceship spaceship = flightMission.get().getAssignedSpaceship();
            if (flightMission.get().getAssignedCrew().stream()
                    .filter(crew -> crew.getRole().equals(crewMember.getRole()))
                    .count() == spaceship.getCrew().get(crewMember.getRole())) {
                throw new HaveAssignedEntityException("No more places with role " + crewMember.getRole()
                        + " in such FlightMission");
            }
            flightMission.get().addCrewMember(crewMember);
            if (flightMission.get().getMissionResult().equals(MissionResult.FAILED)) {
                crewMember.setNotReadyForNextMissions();
            }
        } else {
            throw new UnknownEntityException("No such FlightMission");
        }
    }

    @Override
    public CrewMember createCrewMember(CrewMember crewMember) throws RuntimeException {
        if (super.findAll().stream()
                .anyMatch(crew -> (crew.getName().equals(crewMember.getName())))) {
            crewMember.reduceCount();
            throw new DuplicateException("This CrewMember already exists!");
        }
        return crewMember;
    }
}
