package com.epam.jwd.core_final.tests;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.DeadEntityException;
import com.epam.jwd.core_final.exception.DuplicateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrewServiceImplTest {
    private final String properties = ApplicationProperties.getInstance().getDateTimeFormat();
    private final CrewServiceImpl crewService = CrewServiceImpl.getInstance();
    private CrewMember crewMember1;
    private CrewMember crewMember2;
    private CrewMember crewMember3;
    private final List<CrewMember> listOfTwoMembers = new ArrayList<>();
    private final FlightMission flightMission1 = new FlightMissionFactory().create("first",
            LocalDate.parse("1111-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
            LocalDate.parse("1112-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
            10000L,
            2);
    private final FlightMission flightMission2 = new FlightMissionFactory().create("second",
            LocalDate.parse("1111-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
            LocalDate.parse("1112-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
            10000L,
            2);

    @Before
    public void initTest(){
        crewMember1 = new CrewMemberFactory().create("Anton", "1", "2");
        crewMember1.reduceCount();
        crewMember2 = new CrewMemberFactory().create("Oleg", "1", "2");
        crewMember3 = new CrewMemberFactory().create("Pavel", "3", "3");
        crewMember1.setNotReadyForNextMissions();
        crewService.findAll().add(crewMember1);
        crewService.findAll().add(crewMember2);
        listOfTwoMembers.add(crewMember1);
        listOfTwoMembers.add(crewMember2);
        flightMission1.getAssignedCrew().add(crewMember2);
    }

    @Test
    public void findAllCrewMembers(){
        Collection<CrewMember> actual = crewService.findAllCrewMembers();
        Assert.assertEquals("should have return all crew members", crewService.findAll(), actual);
    }

    @Test
    public void updateCrewMemberDetails_shouldReturnNull_whenMemberIsDead(){
        CrewMember actual = crewService.updateCrewMemberDetails(crewMember1);
        Assert.assertNull("should have returned null", actual);
    }

    @Test
    public void updateSpaceshipDetails_shouldUpdateCrewMember_whenMemberIsNotDead(){
        CrewMember actual = crewService.updateCrewMemberDetails(crewMember2);
        Assert.assertEquals("should have returned null", crewMember2, actual);
    }

    @Test
    public void findAllCrewMembersByCriteria(){
        List<CrewMember> actual = crewService.findAllCrewMembersByCriteria(CrewMemberCriteria.builder()
                .id(crewMember1.getId())
                .Rank(crewMember1.getRank())
                .Role(crewMember1.getRole())
                .build());
        Assert.assertEquals("should have returned spaceships Anton and Oleg", listOfTwoMembers, actual);
    }

    @Test
    public void createCrewMember_shouldCreateNewShip_whenThereIsNoMemberWithSimilarName(){
        CrewMember actual = crewService.createCrewMember(crewMember3);
        Assert.assertEquals("should have returned spaceship Pavel", crewMember3, actual);
    }

    @Test(expected = DuplicateException.class)
    public void createCrewMember_shouldTrowDuplicateException_whenThereIsMemberWithSimilarName() throws RuntimeException {
        crewService.createCrewMember(crewMember1);
    }

    @Test
    public void findCrewMemberByCriteria_shouldReturnOptionalEmpty_whenCriteriaIsNull() {
        Optional<CrewMember> actual = crewService.findCrewMemberByCriteria(null);
        Assert.assertEquals("should have returned Optional.empty()", Optional.empty(), actual);
    }

    @Test
    public void findCrewMemberByCriteria_shouldReturnCrewMember_whenCriteriaIsNotNull() {
        Optional<CrewMember> actual = crewService.findCrewMemberByCriteria(
                CrewMemberCriteria.builder().name("Anton").isReadyForNextMissions(false).build());
        Assert.assertEquals("should have returned the first member",
                crewService.findAll().stream().findFirst(), actual);
    }

    @Test(expected = DeadEntityException.class)
    public void assignCrewMemberOnMission_shouldTrowDeadEntityException_whenMemberIsDead()throws RuntimeException{
        crewService.assignCrewMemberOnMission(crewMember1, "first");
    }

    @Test(expected = UnknownEntityException.class)
    public void assignCrewMemberOnMission_shouldTrowUnknownEntityException_whenThereAreNoMissions()throws RuntimeException{
        MissionServiceImpl missionService = mock(MissionServiceImpl.class);
        when(missionService.findMissionByCriteria(FlightMissionCriteria.builder().name(anyString()).build()))
                .thenReturn(Optional.empty());
        crewService.assignCrewMemberOnMission(crewMember3, "second");
    }
}