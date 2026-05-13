package ParentHiveApp.web.controller;

import ParentHiveApp.dto.AiSummaryDto;
import ParentHiveApp.service.GeminiService;
import ParentHiveApp.service.RateLimitException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<String> summarize(
            @RequestBody AiSummaryDto request) {

        try {
            String summary = geminiService.generateSummary(request.getPostContent());
            return ResponseEntity.ok(summary);
        } catch (RateLimitException e) {
            ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS);
            if (e.getRetryAfterSeconds() != null) {
                builder.header("Retry-After", e.getRetryAfterSeconds().toString());
            }
            return builder.body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("AI summary is not configured.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate summary.");
        }
    }
}