package de.joblift.service.gitlabpanorama.initialization;

import java.time.Instant;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.eventbus.EventBus;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.util.InstantDeserializer;


@Configuration
public class AppConfig {

	@Bean
	public EventBus eventBus() {
		Say.info("Init Eventbus");
		return new EventBus();
	}


	@Bean
	public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilder() {
		return builder -> {
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Instant.class, new InstantDeserializer());
			builder.modules(module);
		};
	}

}
