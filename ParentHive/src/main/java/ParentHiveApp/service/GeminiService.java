package ParentHiveApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generateSummary(String postContent) {

        String prompt = """
            Summarize this parenting forum post and its replies in 3 concise sentences.
            Focus on: the main concern raised, the key advice given, and the overall conclusion.

            %s
            """.formatted(postContent);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent?key=" + apiKey;

        try {
            // Build JSON safely with Jackson — never string-format user content into JSON.
            // String formatting breaks the moment the post contains quotes, backslashes, or newlines.
            ObjectNode part = mapper.createObjectNode().put("text", prompt);
            ObjectNode contents = mapper.createObjectNode();
            contents.putArray("parts").add(part);
            ObjectNode body = mapper.createObjectNode();
            body.putArray("contents").add(contents);

            String requestBody = mapper.writeValueAsString(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("=== Gemini request body ===");
            System.out.println(requestBody);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("=== Gemini response status: " + response.getStatusCode() + " ===");
            System.out.println("=== Gemini response body ===");
            System.out.println(response.getBody());

            JsonNode root = mapper.readTree(response.getBody());
            return root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText("No summary returned.");

        } catch (HttpClientErrorException e) {
            System.err.println("=== Gemini API error: " + e.getStatusCode() + " ===");
            System.err.println(e.getResponseBodyAsString());
            return "Failed to generate summary (API error).";
        } catch (Exception e) {
            System.err.println("=== Gemini unexpected error ===");
            e.printStackTrace();
            return "Failed to generate summary.";
        }
    }
}