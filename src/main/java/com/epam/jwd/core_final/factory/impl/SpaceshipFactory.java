package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;

import java.util.HashMap;
import java.util.Map;

public class SpaceshipFactory implements EntityFactory<Spaceship> {
    @Override
    public Spaceship create(Object... args) {
        if (!preProc(args)) {
            return null;
        }
        Map<Role, Short> crew = new HashMap<>();
        crew.put(Role.values()[0], (short) 0);
        crew.put(Role.values()[1], (short) 0);
        crew.put(Role.values()[2], (short) 0);
        crew.put(Role.values()[3], (short) 0);
        for (int i = 2; i < args.length; i += 2) {
            crew.put(Role.values()[Integer.parseInt((String) args[i]) - 1], Short.parseShort((String) args[i + 1]));
        }
        return new Spaceship((String) args[0], crew, Long.parseLong((String) args[2]));
    }

    private boolean preProc(Object... args) {
        return args[0] != null && args.length % 2 == 0 && Long.parseLong((String) args[1]) >= 0L;
    }
}