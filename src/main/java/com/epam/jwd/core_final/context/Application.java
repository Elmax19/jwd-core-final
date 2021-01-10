package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaMenu;

import com.epam.jwd.core_final.exception.InvalidStateException;

import java.util.function.Supplier;

public interface Application {

    static ApplicationMenu start() throws InvalidStateException {
        final NassaMenu nassaMenu = new NassaMenu();
        final Supplier<ApplicationContext> applicationContextSupplier = nassaMenu::getApplicationContext;
        applicationContextSupplier.get().init();
        return applicationContextSupplier::get;
    }
}
