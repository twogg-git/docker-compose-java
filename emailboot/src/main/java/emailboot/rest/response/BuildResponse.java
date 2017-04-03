package emailboot.rest.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class BuildResponse {

    public static ResponseEntity<?> okStatus(String url, Object data) {
        return new ResponseEntity<>(new Response(HttpStatus.OK, url, "EmailBoot request response.", data), new HttpHeaders(), HttpStatus.OK);
    }

    public static ResponseEntity<?> status(HttpStatus status, String url, String description, Object data) {
        return new ResponseEntity<>(new Response(status, url, description, data), new HttpHeaders(), status);
    }

    public static ResponseEntity<?> createdStatus(String url, String description, String servicePath, Object createdItemId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path(servicePath).buildAndExpand(createdItemId).toUri());
        return new ResponseEntity<>(new Response(HttpStatus.CREATED, url, description), httpHeaders, HttpStatus.CREATED);
    }

    public static ResponseEntity<?> acceptedStatus(String url, String description) {
        return new ResponseEntity<>(new Response(HttpStatus.ACCEPTED, url, description), new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    public static String buildURL(HttpServletRequest req) {
        return "[" + req.getMethod() + "] " + req.getRequestURL().toString() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
    }
}