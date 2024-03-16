package com.katanox.api.common.validators;

import com.katanox.api.search.SearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SearchValidator {
    public void validateSearchRequest(SearchRequest request) {
        if (request.checkout().isBefore(request.checkin().plusDays(1))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Checkin date must be at least 1 day before checkout date.");
        }
    }
}
