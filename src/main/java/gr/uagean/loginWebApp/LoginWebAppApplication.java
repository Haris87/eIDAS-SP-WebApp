package gr.uagean.loginWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LoginWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginWebAppApplication.class, args);
	}
}
