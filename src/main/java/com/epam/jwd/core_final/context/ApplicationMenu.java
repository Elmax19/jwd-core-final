package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.Main;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default void printAvailableOptions() {
        System.out.println("Chose one of available options:");
        System.out.println("1 - find all crew members");
        System.out.println("2 - find all spaceships");
        System.out.println("3 - add new mission");
        System.out.println("4 - find all missions");
        System.out.println("5 - find all alive crew members");
        System.out.println("6 - find all alive spaceships");
        System.out.println("7 - update crew member details");
        System.out.println("8 - update spaceship details");
        System.out.println("9 - update mission details");
        System.out.println("10 - assign spaceship on mission");
        System.out.println("11 - assign crew member on mission");
        System.out.println("12 - cancel mission with real-time flight-status");
        System.out.println("13 - write missions in a file");
        System.out.println("Anything another - to exit");
    }

    default void allMembers(){
        CrewServiceImpl.getInstance().findAllCrewMembers().stream()
                .map(CrewMember::toString)
                .forEach(System.out::println);
    }

    default void allShips(){
        SpaceshipServiceImpl.getInstance().findAllSpaceships().stream()
                .map(Spaceship::toString)
                .forEach(System.out::println);
    }

    default void allMissions(){
        MissionServiceImpl.getInstance().findAllMissions().stream()
                .map(FlightMission::toString)
                .forEach(System.out::println);
    }

    default void allAliveMembers(){
        CrewServiceImpl.getInstance().findAllCrewMembersByCriteria(CrewMemberCriteria.builder()
                .isReadyForNextMissions(true)
                .build()).stream()
                .map(CrewMember::toString)
                .forEach(System.out::println);
    }

    default void allAliveShips(){
        SpaceshipServiceImpl.getInstance().findAllSpaceshipsByCriteria(SpaceshipCriteria.builder()
                .isReadyForNextMissions(true)
                .build()).stream()
                .map(Spaceship::toString)
                .forEach(System.out::println);
    }

    default void addNewMission(Scanner scanner, String properties){
        Object[] missionArgs = enterMissionArgs(scanner,properties);
        FlightMission flightMission = new FlightMissionFactory().create(missionArgs);
        if (flightMission != null) {
            flightMission = MissionServiceImpl.getInstance().createMission(flightMission);
            if (flightMission != null) {
                getApplicationContext().retrieveBaseEntityList(FlightMission.class).add(flightMission);
            }
        }
    }

    default Object[] enterMissionArgs(Scanner scanner, String properties){
        Object[] missionArgs = new Object[5];
        System.out.println("Enter the following parameters:");
        System.out.print("Mission name - ");
        missionArgs[0] = scanner.nextLine();
        System.out.print("Start date (" + properties + ") - ");
        missionArgs[1] = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(properties));
        System.out.print("End date (" + properties + ") - ");
        missionArgs[2] = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(properties));
        System.out.print("Distance - ");
        missionArgs[3] = scanner.nextLong();
        System.out.print("Mission result (ID) - ");
        missionArgs[4] = scanner.nextInt();
        return missionArgs;
    }

    default void updateMember(Scanner scanner){
        Object[] crewUpdateArgs = new Object[5];
        System.out.println("Enter the following parameters:");
        System.out.print("Name - ");
        crewUpdateArgs[0] = scanner.nextLine();
        System.out.print("Role id - ");
        crewUpdateArgs[1] = scanner.next();
        System.out.print("Rank id - ");
        crewUpdateArgs[2] = scanner.next();
        CrewMember crewMember = CrewServiceImpl.getInstance().updateCrewMemberDetails(
                new CrewMemberFactory().create(crewUpdateArgs));
        if (crewMember == null) {
            Main.LOGGER.error("This CrewMember is dead!");
        }
    }

    default void updateShip(Scanner scanner){
        Object[] spaceshipUpdateArgs = new Object[Role.values().length * 2 + 2];
        System.out.println("Enter the following parameters:");
        System.out.print("Name - ");
        spaceshipUpdateArgs[0] = scanner.nextLine();
        for (int i = 2; i <= Role.values().length * 2; i += 2) {
            spaceshipUpdateArgs[i] = Integer.toString(i / 2);
            System.out.print("Count of members with role "
                    + Role.values()[Integer.parseInt((String) spaceshipUpdateArgs[i]) - 1] + " - ");
            spaceshipUpdateArgs[i + 1] = scanner.next();
        }
        System.out.print("Distance - ");
        spaceshipUpdateArgs[1] = scanner.next();
        Spaceship spaceship = SpaceshipServiceImpl.getInstance().updateSpaceshipDetails(
                new SpaceshipFactory().create(spaceshipUpdateArgs));
        if (spaceship == null) {
            Main.LOGGER.error("This Spaceship is dead!");
        }
    }

    default void updateMission(Scanner scanner, String properties){
        Object[] missionUpdateArgs = enterMissionArgs(scanner,properties);
        FlightMission mission = MissionServiceImpl.getInstance().updateMissionDetails(
                new FlightMissionFactory().create(missionUpdateArgs));
        if (mission == null) {
            Main.LOGGER.error("This FlightMission has assigned Entities");
        }
    }

    default void assignSpaceship(Scanner scanner){
        System.out.print("Enter spaceship name: ");
        String spaceshipName = scanner.nextLine();
        Optional<Spaceship> ship = SpaceshipServiceImpl.getInstance().findSpaceshipByCriteria(SpaceshipCriteria.builder()
                .name(spaceshipName)
                .build());
        System.out.print("Enter flight mission name: ");
        String missionName = scanner.nextLine();
        if (ship.isPresent()) {
            try {
                SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(ship.get(), missionName);
            } catch (RuntimeException e) {
                Main.LOGGER.error(e.getMessage());
            }
        } else {
            Main.LOGGER.error("No such Spaceship");
        }
    }

    default void assignMember(Scanner scanner){
        System.out.print("Enter crew member name: ");
        String crewMemberName = scanner.nextLine();
        Optional<CrewMember> member = CrewServiceImpl.getInstance().findCrewMemberByCriteria(CrewMemberCriteria.builder()
                .name(crewMemberName)
                .build());
        System.out.print("Enter flight mission name: ");
        String missionName = scanner.nextLine();
        if (member.isPresent()) {
            try {
                CrewServiceImpl.getInstance().assignCrewMemberOnMission(member.get(), missionName);
            } catch (RuntimeException e) {
                Main.LOGGER.error(e.getMessage());
            }
        } else {
            Main.LOGGER.error("No such CrewMember");
        }
    }

    default void cancelMission(Scanner scanner){
        System.out.print("Enter mission name - ");
        String missionName = scanner.nextLine();
        Optional<FlightMission> flightMissionInProgress = MissionServiceImpl.getInstance().findMissionByCriteria(FlightMissionCriteria.builder()
                .name(missionName)
                .build());
        if (flightMissionInProgress.isPresent()) {
            if (flightMissionInProgress.get().getMissionResult() == MissionResult.IN_PROGRESS) {
                flightMissionInProgress.get().cancelMission();
            } else {
                Main.LOGGER.error("Such FlightMission isn't in progress");
            }
        } else {
            Main.LOGGER.error("No such FlightMission");
        }
    }

    default void writeAllMissions(){
        File outputFile = new File("./src/main/resources/"
                + ApplicationProperties.getInstance().getOutputRootDir() + '/'
                + ApplicationProperties.getInstance().getMissionsFileName());
        JsonFactory jsonFactory = new JsonFactory();
        try {
            JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(outputFile, JsonEncoding.UTF8);
            List<FlightMission> flightMissionList = MissionServiceImpl.getInstance().findAllMissions();
            for (FlightMission outputMission : flightMissionList) {
                writeMission(jsonGenerator, outputMission);
            }
            jsonGenerator.close();
        } catch (IOException e) {
            Main.LOGGER.error("IOException");
        }
    }

    default void writeMission(JsonGenerator jsonGenerator, FlightMission outputMission) throws IOException{
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", outputMission.getId());
        jsonGenerator.writeStringField("missionName", outputMission.getName());
        jsonGenerator.writeStringField("startDate", outputMission.getStartDate().toString());
        jsonGenerator.writeStringField("enDate", outputMission.getEndDate().toString());
        jsonGenerator.writeNumberField("distance", outputMission.getDistance());
        jsonGenerator.writeArrayFieldStart("assignedCrewMembers");
        for (CrewMember assignedMember : outputMission.getAssignedCrew()) {
            jsonGenerator.writeString(assignedMember.getName());
        }
        jsonGenerator.writeEndArray();
        if (outputMission.getAssignedSpaceship() == null) {
            jsonGenerator.writeStringField("assignedSpaceship", "");
        } else {
            jsonGenerator.writeStringField("assignedSpaceship",
                    outputMission.getAssignedSpaceship().getName());
        }
        jsonGenerator.writeStringField("missionResult",
                outputMission.getMissionResult().toString());
        jsonGenerator.writeEndObject();
    }

    default boolean handleUserInput(String o) {
        Scanner scanner = new Scanner(System.in);
        String properties = ApplicationProperties.getInstance().getDateTimeFormat();
        switch (o) {
            case "1":
                allMembers();
                break;
            case "2":
                allShips();
                break;
            case "3":
                addNewMission(scanner,properties);
                break;
            case "4":
                allMissions();
                break;
            case "5":
                allAliveMembers();
                break;
            case "6":
                allAliveShips();
                break;
            case "7":
                updateMember(scanner);
                break;
            case "8":
                updateShip(scanner);
                break;
            case "9":
                updateMission(scanner,properties);
                break;
            case "10":
                assignSpaceship(scanner);
                break;
            case "11":
                assignMember(scanner);
                break;
            case "12":
                cancelMission(scanner);
                break;
            case "13":
                writeAllMissions();
                break;
            default:
                return false;
        }
        return true;
    }
}