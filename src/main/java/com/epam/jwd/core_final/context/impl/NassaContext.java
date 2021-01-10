package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.Main;
import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// todo
public class NassaContext implements ApplicationContext {
    private static final NassaContext INSTANCE = new NassaContext();
    // no getters/setters for them
    private final Collection<CrewMember> crewMembers = new ArrayList<>();
    private final Collection<Spaceship> spaceships = new ArrayList<>();
    private final Collection<FlightMission> flightMissions = new ArrayList<>();

    private NassaContext() {
    }

    public static NassaContext getInstance() {
        return INSTANCE;
    }

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        Collection<T> collection;
        if (FlightMission.class.equals(tClass)) {
            collection = (Collection<T>) flightMissions;
        } else {
            if (Spaceship.class.equals(tClass)) {
                collection = (Collection<T>) spaceships;
            } else {
                collection = (Collection<T>) crewMembers;
            }
        }
        return collection;
    }

    /**
     * You have to read input files, populate collections
     *
     * @throws InvalidStateException
     */
    @Override
    public void init() throws InvalidStateException {
        ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
        final File inputCrewFile = new File("./src/main/resources/" + applicationProperties.getInputRootDir()
                + '/' + applicationProperties.getCrewFileName());
        final File inputSpaceshipsFile = new File("./src/main/resources/" + applicationProperties.getInputRootDir()
                + '/' + applicationProperties.getSpaceshipsFileName());
        try {
            final List<String> crew = Files.readAllLines(inputCrewFile.toPath());
            final List<String> spaceships = Files.readAllLines(inputSpaceshipsFile.toPath());
            populateCrewStorage(crew);
            populateSpaceshipStorage(spaceships);
        } catch (IOException e) {
            Main.LOGGER.error("IOException");
        }
    }

    private void populateCrewStorage(List<String> crew) throws InvalidStateException {
        int[] crewArgs = setCrewArgsPositions(crew.get(0));
        for (String line : crew) {
            if (line.charAt(0) != '#') {
                for (String crewMember : line.split(";")) {
                    try {
                        addCrewMember(crewMember, crewArgs);
                    } catch (RuntimeException e) {
                        Main.LOGGER.error(e.getMessage());
                    }
                }
            }
        }
    }

    private void addCrewMember(String crewMember, int[] args) throws RuntimeException {
        Object[] values = new Object[3];
        int i = 0;
        for (Object arg : crewMember.split(",")) {
            values[args[i]] = arg;
            i++;
        }
        CrewMember member = new CrewMemberFactory().create(values);
        if (member != null) {
            try {
                crewMembers.add(CrewServiceImpl.getInstance().createCrewMember(member));
            } catch (DuplicateException e) {
                Main.LOGGER.error(e.getMessage());
            }
        }
    }

    private int[] setCrewArgsPositions(String crewTemplate) throws InvalidStateException {
        int[] crewArgs = new int[3];
        int i = 0;
        for (String arg : crewTemplate.substring(1, crewTemplate.indexOf(';')).split(",")) {
            if (i > 2) {
                throw new InvalidStateException("Too many states");
            }
            switch (arg) {
                case "name":
                    crewArgs[i] = 0;
                    break;
                case "role":
                    crewArgs[i] = 1;
                    break;
                case "rank":
                    crewArgs[i] = 2;
                    break;
                default:
                    throw new InvalidStateException("Invalid state " + arg);
            }
            i++;
        }
        return crewArgs;
    }

    private void populateSpaceshipStorage(List<String> spaceships) throws InvalidStateException {
        int[] spaceshipsArgs = setSpaceshipsArgsPositions(spaceships.get(0));
        for (String spaceship : spaceships) {
            if (spaceship.charAt(0) != '#') {
                addSpaceship(spaceship, spaceshipsArgs);
            }
        }
    }

    private void addSpaceship(String line, int[] args) {
        Object[] values = new Object[Role.values().length * 2 + 2];
        int i = 0;
        String[] spaceshipArgs = line.split(";");
        for (Object arg : spaceshipArgs) {
            if (args[i] != 2) {
                values[args[i]] = arg;
            }
            i++;
        }
        i = 0;
        for (String arg : spaceshipArgs) {
            if (args[i] == 2) {
                int j = 2;
                for (Object crew : arg.substring(1, arg.length() - 1).split("[:,]")) {
                    values[j] = crew;
                    j++;
                }
                break;
            }
            i++;
        }
        Spaceship spaceship = new SpaceshipFactory().create(values);
        if (spaceship != null) {
            try {
                spaceships.add(SpaceshipServiceImpl.getInstance().createSpaceship(spaceship));
            } catch (DuplicateException e) {
                Main.LOGGER.error(e.getMessage());
            }
        }
    }

    private int[] setSpaceshipsArgsPositions(String spaceshipsTemplate) throws InvalidStateException {
        int[] spaceshipsArgs = new int[3];
        int i = 0;
        for (String arg : spaceshipsTemplate.substring(1, spaceshipsTemplate.indexOf('{')).split(";")) {
            if (i > 2) {
                throw new InvalidStateException("Too many states");
            }
            switch (arg) {
                case "name":
                    spaceshipsArgs[i] = 0;
                    break;
                case "distance":
                    spaceshipsArgs[i] = 1;
                    break;
                default:
                    if (arg.contains("crew")) {
                        spaceshipsArgs[i] = 2;
                        break;
                    }
                    throw new InvalidStateException("Invalid state " + arg);
            }
            i++;
        }
        return spaceshipsArgs;
    }
}
