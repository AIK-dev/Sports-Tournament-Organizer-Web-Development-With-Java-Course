package com.tournament_organizer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


// This is a small utility class for formatting a response and creating headers for the responses sent back to user  when carrying out create,
// update or delete operations on any of the resources / entities contained and managed by the Sports Tournament Organizer Application
// backend.
//
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "Sports-Tournament-Organizer";

    private HeaderUtil() {
    }

    public static HttpHeaders createDefaultAlert(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        return headers;
    }

    public static HttpHeaders createCustomAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createCustomAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createCustomAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createCustomAlert("A " + entityName + " is deleted with identifier " + param, param);
    }

    public static HttpHeaders createEntityNotFoundAlert(String entityName, String param) {
        return createCustomAlert("A " + entityName + " was not found with the supplied identifier " + param, param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", defaultMessage);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }
}
