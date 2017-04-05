package emailboot.rest;

import emailboot.logger.service.LoggerService;
import emailboot.rest.response.BuildResponse;
import emailboot.util.exception.BusinessExceptionEnum;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class RestLog {

    private static final Logger logger = Logger.getLogger(RestLog.class.getName());

    @Autowired
    private LoggerService loggerService;

    @RequestMapping(value = "/logger/subject/{subject}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLoggerBySuject(@PathVariable String subject, HttpServletRequest req) {
        return BuildResponse.okStatus(BuildResponse.buildURL(req), loggerService.getEmailBySubject(subject));
    }
    
    @RequestMapping(value = "/logger/serial/{serial}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLoggerBySerial(@PathVariable String serial, HttpServletRequest req) {
        return BuildResponse.okStatus(BuildResponse.buildURL(req), loggerService.getEmailBySerial(serial));
    }

    @RequestMapping(value = "/logger", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getListByDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endDate,
            @RequestParam("onlyDelivered") boolean onlyDelivered,
            HttpServletRequest req) {
        if (onlyDelivered) {
            return BuildResponse.okStatus(BuildResponse.buildURL(req), loggerService.getDeliveredByDates(startDate, endDate));
        } else {
            return BuildResponse.okStatus(BuildResponse.buildURL(req), loggerService.getListByDates(startDate, endDate));
        }
    }

    /********************************************
     * Exception handlers and responses
     ********************************************/
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {
        if (e instanceof HttpMessageNotReadableException) {
            //logger.log(Level.INFO, "MessageNotReadableException " + e.getContent(), e);
            return BuildResponse.status(HttpStatus.NOT_ACCEPTABLE, BuildResponse.buildURL(req), e.getMessage(), null);
        } else if (e instanceof TypeMismatchException) {
            //logger.log(Level.INFO, "TypeMismatchException "+ e.getContent(), e);
            return BuildResponse.status(HttpStatus.NOT_ACCEPTABLE, BuildResponse.buildURL(req), e.getMessage(), null);
        } else {
            logger.log(Level.SEVERE, "Internal Server Error" + "\nURL: " + BuildResponse.buildURL(req) + "\nCause: " + e.getMessage(), e);
            return BuildResponse.status(HttpStatus.INTERNAL_SERVER_ERROR, BuildResponse.buildURL(req), BusinessExceptionEnum.EXCEPTION_SERVER_ERROR.getMessage(), null);
        }
    }

}
