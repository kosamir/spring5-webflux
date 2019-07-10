package hr.optimus.spring5.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


  
@SpringBootApplication
public class Spring5WebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring5WebfluxApplication.class, args);
	}
	

	    @Bean
	    public CommonsRequestLoggingFilter logFilter() {
	        CommonsRequestLoggingFilter filter
	          = new CommonsRequestLoggingFilter();
	        filter.setIncludeQueryString(true);
	        filter.setIncludePayload(true);
	        filter.setMaxPayloadLength(10000);
	        filter.setIncludeHeaders(false);
//	        filter.setBeforeMessagePrefix(beforeMessagePrefix);
	        filter.setAfterMessagePrefix("REQUEST DATA : ");
	        return filter;
	    }   
	

}
