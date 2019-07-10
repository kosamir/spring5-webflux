package hr.optimus.spring5.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/api/v1/categories")
@Slf4j
public class CategoryController {
	
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}
	
	@GetMapping(path = {"","/"})
	public Flux<Category> findAll(){
		log.info("running find all");
		return categoryRepository.findAll();  
	}
	@GetMapping(path = "/{id}")
	public Mono<Category> findById(@PathVariable String id){
		log.info("running find by id:" + id);
		return categoryRepository.findById(id);
	}
	
	

}
