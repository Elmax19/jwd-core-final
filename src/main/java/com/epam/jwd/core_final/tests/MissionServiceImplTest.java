package com.epam.jwd.core_final.tests;

import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
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

public class MissionServiceImplTest {
    private final String properties = ApplicationProperties.getInstance().getDateTimeFormat();
    private FlightMission flightMission1;
    private FlightMission flightMission2;
    private FlightMission flightMission3;
    private final List<FlightMission> firstTwoMissions = new ArrayList<>();
    private final MissionServiceImpl missionService = MissionServiceImpl.getInstance();

    @Before
    public void initTest(){
        flightMission1 = new FlightMissionFactory().create("first",
                LocalDate.parse("1111-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                LocalDate.parse("1112-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                10000L,
                2);
        flightMission1.reduceCount();
        flightMission2 = new FlightMissionFactory().create("second",
                LocalDate.parse("1111-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                LocalDate.parse("1112-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                10000L,
                4);
        flightMission3 = new FlightMissionFactory().create("third",
                LocalDate.parse("1321-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                LocalDate.parse("1322-11-11 11:11:11", DateTimeFormatter.ofPattern(properties)),
                10L,
                3);
        missionService.findAll().add(flightMission1);
        missionService.findAll().add(flightMission2);
        firstTwoMissions.add(flightMission1);
        firstTwoMissions.add(flightMission2);
        missionService.findAll().stream().findFirst().get().setAssignedSpaceShift(
                new Spaceship("Wolf", null, 0L));
    }

    @Test
    public void findMissionByCriteria_shouldReturnOptionalEmpty_whenCriteriaIsNull() {
        Optional<FlightMission> actual = missionService.findMissionByCriteria(null);
        Assert.assertEquals("should have returned Optional.empty()", Optional.empty(), actual);
    }

    @Test
    public void findMissionByCriteria_shouldReturnFlightMission_whenCriteriaIsNotNull() {
        Optional<FlightMission> actual = missionService.findMissionByCriteria(
                FlightMissionCriteria.builder().name("first").missionResult(MissionResult.FAILED).build());
        Assert.assertEquals("should have returned the first mission",
                missionService.findAll().stream().findFirst(), actual);
    }

    @Test
    public void updateMissionDetails_shouldReturnNull_whenMissionHaveAssignedEntities(){
        FlightMission actual = missionService.updateMissionDetails(flightMission1);
        Assert.assertNull("should have returned null", actual);
    }

    @Test
    public void updateMissionDetails_shouldReturnUpdatedMission_whenMissionHaveNoTAssignedEntities(){
        FlightMission actual = missionService.updateMissionDetails(flightMission2);
        Assert.assertEquals("should have returned the second mission", flightMission2, actual);
    }

    @Test
    public void createMission_shouldCreateNewMission_whenThereIsNotMissionWithSimilarName(){
        FlightMission actual = missionService.createMission(flightMission3);
        Assert.assertEquals("should have created a new mission", flightMission3, actual);
    }

    @Test
    public void createMission_shouldReturnNull_whenThereIsMissionWithSimilarName(){
        FlightMission actual = missionService.createMission(flightMission2);
        Assert.assertNull("should have returned null", actual);
    }

    @Test
    public void findAllMissions(){
        Collection<FlightMission> actual = missionService.findAllMissions();
        Assert.assertEquals("should have returned all missions", missionService.findAll(), actual);
    }

    @Test
    public void findAllMissionsByCriteria(){
        List<FlightMission> actual = missionService.findAllMissionsByCriteria(FlightMissionCriteria.builder()
                .id(flightMission1.getId())
                .startDate(flightMission1.getStartDate())
                .endDate(flightMission1.getEndDate())
                .distance(flightMission1.getDistance())
                .build());
        Assert.assertEquals("should have returned first and second mission", firstTwoMissions, actual);
    }
}