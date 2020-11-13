package com.alan.databee.common.util.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogLevelFilter extends Filter<ILoggingEvent> {
    Level minLevel;
    Level maxLevel;

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        } else {
            Level level = iLoggingEvent.getLevel();
            int levelInt = level.levelInt;
            int minLevelInt = this.minLevel.levelInt;
            int maxLevelInt = this.maxLevel.levelInt;
            if(levelInt <= maxLevelInt && levelInt >= minLevelInt){
                return FilterReply.NEUTRAL;
            }else {
                return FilterReply.DENY;
            }
        }
    }


    public void setMinLevel(Level minLevel) {
        this.minLevel = minLevel;
    }

    public void setMaxLevel(Level maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void start() {
        if (this.minLevel != null && this.maxLevel!=null) {
            super.start();
        }else {
            this.minLevel = Level.DEBUG;
            this.maxLevel = Level.WARN;
            super.start();
        }
    }
}
