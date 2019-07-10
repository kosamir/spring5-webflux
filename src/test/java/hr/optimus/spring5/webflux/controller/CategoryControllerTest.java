package hr.optimus.spring5.webflux.controller;

//import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

	WebTestClient client;
	@Mock
	private CategoryRepository categoryRepository;
	@InjectMocks
	private CategoryController categoryController;

	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {

	}

	@BeforeEach
	public void setUp() throws Exception {
		client = WebTestClient.bindToController(categoryController).build();
	}

	@Test
	public void testFindAll() throws Exception {

		BDDMockito.given(categoryRepository.findAll()).willReturn(
				Flux.just(new Category("Category 1"), new Category("Category 2"), new Category("Category 3")));

		client.get().uri(("/api/v1/categories/")).exchange().expectBodyList(Category.class).hasSize(3);
	}

	@Test
	public void testFindById() throws Exception {
		BDDMockito.given(categoryRepository.findById("1234567890"))
				.willReturn(Mono.just(new Category("Categorytotest")));

		BodySpec<Category, ?> categporyBodySpec = client.get().uri("/api/v1/categories/1234567890").exchange()
				.expectBody(Category.class);

		System.out.println("test");

		categporyBodySpec.isEqualTo(new Category("Categorytotest"));

		categporyBodySpec.consumeWith(category -> {
			System.out.println(category.getResponseBody().getId());
			assertNull(category.getResponseBody().getId());
			assertEquals("Categorytotest", category.getResponseBody().getName());
		});

	}

	@Test
	void testCreateNewCategory() throws Exception {
		
		BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
					.willReturn(Flux.just(new Category("cat1")));
				
		
		Mono<Category> toSave = Mono.just(new Category("cat 1"));
		client.post()
				.uri("/api/v1/categories/")
				.body(toSave, Category.class)
				.exchange()
				.expectStatus()
				.isCreated();		
	}

	@Test
	void testUpdateCategory() throws Exception {
		BDDMockito.given(categoryRepository.save(any(Category.class)))
				.willReturn(Mono.just(new Category("categorie 1")));
		
		Mono<Category> toUpdate = Mono.just(new Category("cat 1"));
		
		client.put()
				.uri("/api/v1/categories/123")
				.body(toUpdate, Category.class)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Category.class)
				.consumeWith(consumer -> assertEquals("categorie 1", consumer.getResponseBody().getName()));

	}
}
