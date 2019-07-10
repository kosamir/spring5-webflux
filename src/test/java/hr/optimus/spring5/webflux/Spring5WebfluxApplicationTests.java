package hr.optimus.spring5.webflux;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import hr.optimus.spring5.webflux.repository.VendorRepository;

@RunWith(SpringRunner.class)
//@SpringBootTest
@DataMongoTest
class Spring5WebfluxApplicationTests {

	@Autowired
	private  CategoryRepository categoryRepository;
	
	@Autowired
	private  VendorRepository vendorRepository;
	
	@BeforeEach
	public void setUp() {
		System.out.println("******SET UP **************");
		categoryRepository.save(new Category("category1")).block();
		categoryRepository.save(new Category("category2")).block();
	}     
	
	@Test
	void contextLoads() {
		System.out.println("****contextLoads*****" + categoryRepository.count().block());
	}

}
