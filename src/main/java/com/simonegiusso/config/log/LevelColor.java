package com.simonegiusso.config.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

import static ch.qos.logback.core.pattern.color.ANSIConstants.*;

public class LevelColor extends ForegroundCompositeConverterBase<ILoggingEvent> {

    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        return switch (level.toInt()) {
            case Level.ERROR_INT -> BOLD + RED_FG;
            case Level.WARN_INT -> YELLOW_FG;
            case Level.INFO_INT -> GREEN_FG;
            case Level.DEBUG_INT -> BLUE_FG;
            default -> WHITE_FG;
        };

    }

}