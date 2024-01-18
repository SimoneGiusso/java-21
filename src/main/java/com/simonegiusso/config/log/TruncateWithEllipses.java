package com.simonegiusso.config.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TruncateWithEllipses extends ClassicConverter {

    private static final int MAX_LENGTH = 45;

    @Override
    public String convert(ILoggingEvent event) {
        String threadName = event.getThreadName();

        if (threadName.length() > MAX_LENGTH) {
            return threadName.substring(0, MAX_LENGTH - 3) + "...";
        }

        return threadName;
    }
}