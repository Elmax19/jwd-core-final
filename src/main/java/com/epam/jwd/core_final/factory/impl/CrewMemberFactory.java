package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;

// do the same for other entities
public class CrewMemberFactory implements EntityFactory<CrewMember> {

    @Override
    public CrewMember create(Object... args) {
        if (!preProc(args)) {
            return null;
        }
        return new CrewMember((String) args[0], Role.values()[Integer.parseInt((String) args[1]) - 1],
                Rank.values()[Integer.parseInt((String) args[2]) - 1]);
    }

    private boolean preProc(Object... args) {
        return args[0] != null;
    }
}

