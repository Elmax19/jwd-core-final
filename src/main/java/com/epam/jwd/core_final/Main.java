package com.epam.jwd.core_final;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.exception.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);  // todo slf4j
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ApplicationMenu menu = Application.start();
            String input;
            do {
                menu.printAvailableOptions();
                input = SCANNER.next();
            } while (menu.handleUserInput(input));
        } catch (InvalidStateException e) {
            LOGGER.error(e.getMessage());
        }
    }
}