package org.example.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@ComponentScan("org.example.server")
@PropertySource("classpath:db.properties")
public class ApplicationConfig {
   @Value("${db.url}")
   private String DB_URL;
   @Value("${db.user}")
   private String DB_USER;
   @Value("${db.password}")
   private String DB_PASSWD;
   @Value("${db.driver.name}")
   private String DB_DRIVER_NAME;
   
   private String sql_create_users_db = "CREATE TABLE IF NOT EXISTS Users (id SERIAL PRIMARY KEY, login VARCHAR(255) UNIQUE, pass VARCHAR(255), ip VARCHAR(255))";


   @Bean(name = "SpringDataSource")
   @Scope("singleton")
   public DataSource initSpringDataSource() {
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       try {
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USER);
            dataSource.setPassword(DB_PASSWD);
            dataSource.setDriverClassName(DB_DRIVER_NAME);
            dataSource.getConnection().createStatement().execute(sql_create_users_db);
       } catch(Exception e) {
            System.out.println("\033[31mDatabase connection error!!!\033[0m");
            e.printStackTrace();
       }
       return dataSource;
   }
}