package org.nds.logger;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoggerService {

    public static void audit(String message) {
        log.info(message);
        log.debug(message);
    }

    public static void audit(String message, Object... objects) {

        audit(message, true, objects);
    }

    public static void audit(String message, Boolean containsSensitiveData, Object... objects) {

        String formatedMessage = createMessage(message, objects);

        log.info(containsSensitiveData ? message : formatedMessage , objects);
        log.debug(formatedMessage, objects);
    }

    public static void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    private static String createMessage(String message, Object... objects) {

        for(int x=0;x<objects.length;x++) {
            message += " {}";
        }

        return message;
    }
}
