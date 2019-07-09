package hr.optimus.spring5.webflux.bootstrap;

import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.model.Vendor;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import hr.optimus.spring5.webflux.repository.VendorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
@AllArgsConstructor
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
	
	private final CategoryRepository categoryRepository;
	private final VendorRepository vendorRepository;
	
	

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
//		categoryRepository.deleteAll().block();
//		vendorRepository.deleteAll().block();
		
		if(categoryRepository.count().block() == 0) {
			Flux<Category> flux = categoryRepository.saveAll(Arrays.asList(
					new Category("Category 1"), 
					new Category("Category 2"),
					new Category("Category 3"), 
					new Category("Category 4")));
			
			Flux<Vendor> flux1 =  (Flux<Vendor>) vendorRepository.saveAll(Arrays.asList(
					new Vendor("VendorName1", "VendorSurname1"),
					new Vendor("VendorName2", "VendorSurname2"),
					new Vendor("VendorName3", "VendorSurname3"),
					new Vendor("VendorName4", "VendorSurname4")));
			
			flux.collectList().block();//.stream().forEach(System.out::println);
			flux1.collectList().block();
			
			log.debug("Saved categories!");
//			Flux <Category> flux3 = categoryRepository.findAll();
			categoryRepository.findAll().log().collectList().block().stream().forEach(System.out::println);
			
			log.debug("Saved vendors!");
//			Flux <Category> flux3 = categoryRepository.findAll();
			vendorRepository.findAll().log().collectList().block().stream().forEach(System.out::println);
			
			log.debug("Category repository in database:" + categoryRepository.findAll().collectList().block().size());
			log.debug("Vendor repository in database:" + vendorRepository.findAll().collectList().block().size());
		}else {
			log.debug("*****Database was filled previously*******");
		}
		
		
		

		
		
//		
	}

}
