package hr.optimus.spring5.webflux.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value=CategoryController.API_V1_CATEGORIES)
@Slf4j
public class CategoryController {
	
	static final String API_V1_CATEGORIES = "/api/v1/categories";
	
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
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(path= {"","/"," "})
	public Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream){
		return categoryRepository.saveAll(categoryStream).then();
	}
	
	
	@PutMapping(path = "/{id}")
	public Mono<Category> updateCategory(@RequestBody Category category, @PathVariable String id){
		category.setId(id);
		return categoryRepository.save(category);
	}
	
	

}
