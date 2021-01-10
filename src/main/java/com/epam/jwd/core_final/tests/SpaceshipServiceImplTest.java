package com.epam.jwd.core_final.tests;

import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DeadEntityException;
import com.epam.jwd.core_final.exception.DuplicateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
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

public class SpaceshipServiceImplTest {
    private final SpaceshipServiceImpl spaceshipService = SpaceshipServiceImpl.getInstance();
    private final String properties = ApplicationProperties.getInstance().getDateTimeFormat();
    private Spaceship spaceship1;
    private Spaceship spaceship2;
    private Spaceship spaceship3;
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
    private final List<Spaceship> listOfTwoSpaceships = new ArrayList<>();

    @Before
    public void initTest(){
        spaceship1 = new SpaceshipFactory().create("Wolf1", "300", "1", "1", "2", "2", "3", "3", "4", "4");
        spaceship1.setNotReadyForNextMissions();
        spaceship1.reduceCount();
        spaceship2 = new SpaceshipFactory().create("Wolf2", "300", "1", "1", "2", "2", "3", "3", "4", "4");
        spaceship3 = new SpaceshipFactory().create("Wolf3", "100", "1", "1", "2", "2", "3", "3", "4", "4");
        spaceship3.changeFlightDistance(10L);
        spaceshipService.findAll().add(spaceship1);
        spaceshipService.findAll().add(spaceship2);
        listOfTwoSpaceships.add(spaceship1);
        listOfTwoSpaceships.add(spaceship2);
        flightMission1.setAssignedSpaceShift(spaceship3);
    }

    @Test
    public void findAllSpaceships(){
        Collection<Spaceship> actual = spaceshipService.findAllSpaceships();
        Assert.assertEquals("should have return all spaceships", spaceshipService.findAll(), actual);
    }

    @Test
    public void updateSpaceshipDetails_shouldReturnNull_whenShipIsDead(){
        Spaceship actual = spaceshipService.updateSpaceshipDetails(spaceship1);
        Assert.assertNull("should have returned null", actual);
    }

    @Test
    public void updateSpaceshipDetails_shouldUpdateSpaceship_whenShipIsNotDead(){
        Spaceship actual = spaceshipService.updateSpaceshipDetails(spaceship2);
        Assert.assertEquals("should have returned null", spaceship2, actual);
    }

    @Test
    public void findAllSpaceshipsByCriteria(){
        List<Spaceship> actual = spaceshipService.findAllSpaceshipsByCriteria(SpaceshipCriteria.builder()
                .id(spaceship1.getId())
                .flightDistance(spaceship1.getFlightDistance())
                .build());
        Assert.assertEquals("should have returned spaceships Wolf and Wolf2", listOfTwoSpaceships, actual);
    }

    @Test
    public void createSpaceship_shouldCreateNewShip_whenThereIsNoShipWithSimilarName(){
        Spaceship actual = spaceshipService.createSpaceship(spaceship3);
        Assert.assertEquals("should have returned spaceship Wolf3", spaceship3, actual);
    }

    @Test(expected = DuplicateException.class)
    public void createSpaceship_shouldTrowDuplicateException_whenThereIsShipWithSimilarName() throws RuntimeException {
        spaceshipService.createSpaceship(spaceship1);
    }

    @Test
    public void findSpaceshipByCriteria_shouldReturnOptionalEmpty_whenCriteriaIsNull() {
        Optional<Spaceship> actual = spaceshipService.findSpaceshipByCriteria(null);
        Assert.assertEquals("should have returned Optional.empty()", Optional.empty(), actual);
    }

    @Test
    public void findSpaceshipByCriteria_shouldReturnSpaceship_whenCriteriaIsNotNull() {
        Optional<Spaceship> actual = spaceshipService.findSpaceshipByCriteria(
                SpaceshipCriteria.builder().name("Wolf1").isReadyForNextMissions(false).build());
        Assert.assertEquals("should have returned the first spaceship",
                spaceshipService.findAll().stream().findFirst(), actual);
    }

    @Test(expected = DeadEntityException.class)
    public void assignSpaceshipOnMission_shouldTrowDeadEntityException_whenShipIsDead()throws RuntimeException{
        spaceshipService.assignSpaceshipOnMission(spaceship1, "first");
    }

    @Test(expected = UnknownEntityException.class)
    public void assignSpaceshipOnMission_shouldTrowUnknownEntityException_whenThereAreNoMissions()throws RuntimeException{
        MissionServiceImpl missionService = mock(MissionServiceImpl.class);
        when(missionService.findMissionByCriteria(FlightMissionCriteria.builder().name(anyString()).build()))
                .thenReturn(Optional.empty());
        spaceshipService.assignSpaceshipOnMission(spaceship3, "second");
    }
}