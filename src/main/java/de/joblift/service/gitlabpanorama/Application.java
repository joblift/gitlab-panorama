package de.joblift.service.gitlabpanorama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

	public static void main(String[] args) {
		System.setProperty("spring.main.banner-mode", "off");
		SpringApplication.run(Application.class, args);
	}

}
