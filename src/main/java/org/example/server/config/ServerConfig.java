import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.example.server.model.Cheese;
import org.example.server.model.Fild;
import org.example.server.model.User;

@Configuration
public class ServerConfig {

    @Bean
    public Cheese cheese() {
        return new Cheese(10); 
    }

    @Bean
    public Fild fild() {
        return new Fild();
    }
}
