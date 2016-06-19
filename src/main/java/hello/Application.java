package hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

/**
 * To upload files with Servlet 3.0 containers, you need to register a MultipartConfigElement class (which would be a
 * <multipart-config> in web.xml). Spring Boot provides registration of that bean.
 */
@SpringBootApplication
public class Application {

    public static String ROOT = "upload-dir";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init() {
        // crating root folder at startup
        return (String[] args) -> new File(ROOT).mkdir();
    }

}