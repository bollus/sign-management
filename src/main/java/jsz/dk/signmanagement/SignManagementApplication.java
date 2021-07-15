package jsz.dk.signmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SignManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignManagementApplication.class, args);
    }

}
