package org.minitest.authapi.process;

import org.minitest.authapi.auth.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/process")
public class ProcessController {
    private final RestTemplate restTemplate;
    private final ProcessingLogRepository logRepository;
    private final String dataApiUrl;
    private final String internalToken;

    public ProcessController(
            RestTemplate restTemplate,
            ProcessingLogRepository logRepository,
            @Value("${app.data-api-url}") String dataApiUrl,
            @Value("${app.internal-token}") String internalToken) {
        this.restTemplate = restTemplate;
        this.logRepository = logRepository;
        this.dataApiUrl = dataApiUrl;
        this.internalToken = internalToken;
    }

    @PostMapping
    public ProcessResponse process(@AuthenticationPrincipal AppUser user, @RequestBody ProcessRequest request) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authentication required");
        }
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text is required");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalToken);
        ProcessResponse response = restTemplate.postForObject(
                dataApiUrl + "/api/transform",
                new HttpEntity<>(new ProcessRequest(request.getText()), headers),
                ProcessResponse.class);

        if (response == null || response.getResult() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "data-api returned an invalid response");
        }

        logRepository.save(new ProcessingLog(user, request.getText(), response.getResult()));
        return response;
    }
}