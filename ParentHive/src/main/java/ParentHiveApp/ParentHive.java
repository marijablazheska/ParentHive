package ParentHiveApp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ParentHive {
    public static void main(String[] args) {
        // 1. Load the .env file
        // ignoreIfMissing() prevents crashes in production if you use actual environment variables instead of a .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // 2. Push all variables into System Properties so Spring can read them
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(ParentHive.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}