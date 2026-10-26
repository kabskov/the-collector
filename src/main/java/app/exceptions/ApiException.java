package app.exceptions;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ApiException extends RuntimeException {
    private int code;
    private static final Logger logger = LoggerFactory.getLogger(ApiException.class);

    public ApiException(int code, String msg){
        super(msg);
        this.code = code;
        logException(code, msg, null);
    }

    public ApiException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        logException(code, msg, cause);
    }

    private static void logException(int code, String msg, Throwable cause) {
        if (code >= 500) {
            logger.error("ApiException (code={}): {}", code, msg, cause);
        } else {
            logger.warn("ApiException (code={}): {}", code, msg);
        }
    }
}