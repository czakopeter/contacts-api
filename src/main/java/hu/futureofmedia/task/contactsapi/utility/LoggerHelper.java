package hu.futureofmedia.task.contactsapi.utility;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggerHelper {

    public static void requestLog(Logger logger, HttpServletRequest request, Object... params) {
        logger.info("HTTP request: {} {}{}; Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() == null ? "" : "?" + request.getQueryString(),
                params.length == 0 ?
                        "no request body" :
                        Arrays.stream(params)
                                .map(Object::toString)
                                .collect(Collectors.joining(", ")));
    }
}
