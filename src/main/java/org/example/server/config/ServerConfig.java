import org.example.server.model.Cheese;
import org.example.server.model.Field;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    // @Bean
    // public Cheese cheese() {
    //     return new Cheese(); 
    // }

    @Bean
    public Field field() {
        return new Field();
    }
}
