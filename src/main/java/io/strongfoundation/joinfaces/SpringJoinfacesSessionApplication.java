package io.strongfoundation.joinfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import io.strongfoundation.joinfaces.session.SessionFilter;

@SpringBootApplication
public class SpringJoinfacesSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJoinfacesSessionApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<SessionFilter> loggingFilter() {
		FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SessionFilter());
		registrationBean.addUrlPatterns("*.xhtml"); // Solo protege páginas JSF
		return registrationBean;
	}
}
