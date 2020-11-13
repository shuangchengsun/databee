package com.alan.databee.common.util.log;

import org.slf4j.Logger;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 4:02 下午
 */
public class LoggerUtil {
    private static String separate = "|";

    public static void info(Logger logger, Object... objects) {
        if (logger.isInfoEnabled()) {
            String message = messageBuild(objects);
            logger.info(message);
        }
    }

    public static void debug(Logger logger, Object... objects) {
        if (logger.isDebugEnabled()) {
            String message = messageBuild(objects);
            logger.debug(message);
        }
    }

    public static void error(Logger logger, Object... objects) {
        String message = messageBuild(objects);
        logger.error(message);
    }

    public static void monitor(Logger logger, Object... objects) {
        info(logger, objects);
    }

    public static void warn(Logger logger, Object... objects) {
        if (logger.isWarnEnabled()) {
            String message = messageBuild(objects);
            logger.warn(message);
        }
    }

    private static String messageBuild(Object... objects) {
        StringBuilder message = new StringBuilder();
        message.append(separate);
        for (Object object : objects) {
            message.append(object.toString());
            message.append(separate);

        }
        return message.toString();
    }
}
