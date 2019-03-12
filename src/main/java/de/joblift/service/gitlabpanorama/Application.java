package de.joblift.service.gitlabpanorama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// Alternative approach
		//SpringApplication app = new SpringApplication(Application.class);
		//app.setBannerMode(Mode.OFF);
		//app.run(args);

		System.setProperty("spring.main.banner-mode", "off");
		SpringApplication.run(Application.class, args);
	}

}
