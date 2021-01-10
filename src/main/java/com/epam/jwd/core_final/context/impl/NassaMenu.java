package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;

public class NassaMenu implements ApplicationMenu {
    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.getInstance();
    }
}
