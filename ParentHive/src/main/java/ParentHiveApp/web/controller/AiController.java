package ParentHiveApp.web.controller;

import ParentHiveApp.dto.AiSummaryDto;
import ParentHiveApp.service.GeminiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/summarize")
    public String summarize(
            @RequestBody AiSummaryDto request) {

        return geminiService
                .generateSummary(request.getPostContent());
    }
}