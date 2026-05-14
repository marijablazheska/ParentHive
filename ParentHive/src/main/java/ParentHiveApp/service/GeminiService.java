package ParentHiveApp.service;

import ParentHiveApp.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

@Service
public class GeminiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String modelName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generateSummary(String postContent) {
        ensureApiKeyLoaded();

        String prompt = """
            Summarize this parenting forum post and its replies in 3 concise sentences.
            Focus on: the main concern raised, the key advice given, and the overall conclusion.

            %s
            """.formatted(postContent);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName
                + ":generateContent?key=" + apiKey;

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
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                Integer retryAfterSeconds = parseRetryAfterSeconds(e.getResponseHeaders());
                throw new RateLimitException("Too many requests. Please try again in a minute.", retryAfterSeconds);
            }
            System.err.println("=== Gemini API error: " + e.getStatusCode() + " ===");
            System.err.println(e.getResponseBodyAsString());
            return "Failed to generate summary (API error).";
        } catch (Exception e) {
            System.err.println("=== Gemini unexpected error ===");
            e.printStackTrace();
            return "Failed to generate summary.";
        }
    }

    public String chat(String userMessage, List<ChatMessageDto.Turn> history) {
        ensureApiKeyLoaded();

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName
                + ":generateContent?key=" + apiKey;

        try {
            com.fasterxml.jackson.databind.node.ArrayNode contents = mapper.createArrayNode();

            // System context — give the bot a persona relevant to ParentHive
            ObjectNode systemTurn = mapper.createObjectNode();
            systemTurn.put("role", "user");
            systemTurn.putArray("parts").addObject().put("text",
                    "You are a helpful parenting assistant on ParentHive, a forum for parents and professionals. " +
                            "Give warm, practical, evidence-based advice. Keep answers concise.");
            contents.add(systemTurn);

            // Add a model acknowledgement so Gemini accepts the system turn
            ObjectNode systemAck = mapper.createObjectNode();
            systemAck.put("role", "model");
            systemAck.putArray("parts").addObject().put("text", "Understood! I'm here to help.");
            contents.add(systemAck);

            // Add conversation history so Gemini remembers what was said
            if (history != null) {
                for (ChatMessageDto.Turn turn : history) {
                    ObjectNode turnNode = mapper.createObjectNode();
                    turnNode.put("role", turn.getRole());
                    turnNode.putArray("parts").addObject().put("text", turn.getText());
                    contents.add(turnNode);
                }
            }

            // Add the new user message
            ObjectNode userTurn = mapper.createObjectNode();
            userTurn.put("role", "user");
            userTurn.putArray("parts").addObject().put("text", userMessage);
            contents.add(userTurn);

            ObjectNode body = mapper.createObjectNode();
            body.set("contents", contents);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            return root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText("I couldn't generate a response.");

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RateLimitException("Too many requests. Please try again in a moment.", null);
            }
            LOGGER.error("Gemini chat API error: {} — {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Sorry, I ran into an error. Please try again.";
        } catch (Exception e) {
            LOGGER.error("Gemini chat unexpected error", e);
            return "Sorry, something went wrong.";
        }
    }

    private void ensureApiKeyLoaded() {
        if (apiKey != null && !apiKey.isBlank()) {
            return;
        }

        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey != null && !envKey.isBlank()) {
            apiKey = envKey;
            LOGGER.info("Gemini API key loaded from environment variable.");
            return;
        }

        String workingDir = System.getProperty("user.dir");
        String parentDir = Path.of(workingDir).resolve("..").normalize().toString();
        String moduleDirFromWorking = Path.of(workingDir).resolve("ParentHive").normalize().toString();
        String moduleDirFromParent = Path.of(parentDir).resolve("ParentHive").normalize().toString();

        String dotenvKey = firstNonBlank(
                loadDotenvKey(workingDir),
                loadDotenvKey(parentDir),
                loadDotenvKey(moduleDirFromWorking),
                loadDotenvKey(moduleDirFromParent)
        );

        if (dotenvKey == null || dotenvKey.isBlank()) {
            LOGGER.warn("Gemini API key not found. Checked working dir and common module paths.");
            throw new IllegalStateException("Gemini API key is not configured.");
        }

        apiKey = dotenvKey;
        LOGGER.info("Gemini API key loaded from .env file.");
    }

    private String loadDotenvKey(String directory) {
        return Dotenv.configure()
                .directory(directory)
                .ignoreIfMissing()
                .load()
                .get("GEMINI_API_KEY");
    }

    private Integer parseRetryAfterSeconds(HttpHeaders headers) {
        if (headers == null) {
            return null;
        }
        String retryAfter = headers.getFirst("Retry-After");
        if (retryAfter == null || retryAfter.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(retryAfter.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}