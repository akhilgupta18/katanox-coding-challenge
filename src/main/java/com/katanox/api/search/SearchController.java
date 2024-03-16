package com.katanox.api.search;

import com.katanox.api.common.logger.LogWriterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("search")
public class SearchController {
    @Value("${env}")
    String environment;
    LogWriterService logWriterService;
    SearchService searchService;

    public SearchController(SearchService searchService) {
        this.logWriterService = new LogWriterService();
        this.searchService = searchService;
    }

    @PostMapping(
            path = "/",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    ResponseEntity<SearchResponse> search(
            @RequestBody SearchRequest request
    ) {
        SearchResponse response = searchService.search(request);

        if (Objects.equals(environment, "local")) {
            logWriterService.logStringToConsoleOutput(response.getAvailableRooms().toString());
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
