package ru.zalimannard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {
        logger.info("Hello world");
    }
}
