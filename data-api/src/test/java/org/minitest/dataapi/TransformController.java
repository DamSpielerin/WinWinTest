package org.minitest.dataapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transform")
public class TransformController {
    private final String internalToken;

    public TransformController(@Value("${app.internal-token}") String internalToken) {
        this.internalToken = internalToken;
    }

    @PostMapping
    public TransformResponse transform(
            @RequestHeader(value = "X-Internal-Token", required = true) String token,
            @RequestBody TransformRequest request) {

        String result = new StringBuilder(request.getText()).reverse().toString().toUpperCase();
        return new TransformResponse(result);
    }

}
