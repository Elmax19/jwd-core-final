package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.factory.EntityFactory;

import java.time.LocalDate;

public class FlightMissionFactory implements EntityFactory<FlightMission> {

    @Override
    public FlightMission create(Object... args) {
        if (!preProc(args)) {
            return null;
        }
        return FlightMission.builder()
                .name((String) args[0])
                .startDate((LocalDate) args[1])
                .endDate((LocalDate) args[2])
                .distance((Long) args[3])
                .missionResult(MissionResult.values()[(Integer) args[4] - 1])
                .build();
    }

    private boolean preProc(Object... args) {
        return args[0] != null && (Long) args[3] >= 0L;
    }
}
