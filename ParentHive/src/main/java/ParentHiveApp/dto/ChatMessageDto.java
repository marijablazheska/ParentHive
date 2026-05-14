package ParentHiveApp.dto;

import java.util.List;

public class ChatMessageDto {

    private String message;
    private List<Turn> history;  // previous turns so Gemini has context

    public static class Turn {
        private String role;    // "user" or "model"
        private String text;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Turn> getHistory() { return history; }
    public void setHistory(List<Turn> history) { this.history = history; }
}
