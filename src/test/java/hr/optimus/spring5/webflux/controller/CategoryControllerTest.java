package hr.optimus.spring5.webflux.controller;

//import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.model.Vendor;
import hr.optimus.spring5.webflux.repository.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
		
		given(categoryRepository.saveAll(any(Publisher.class)))
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
		given(categoryRepository.save(any(Category.class)))
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
	
	@Test
	void testPatchCategory() throws Exception {
		
		given(categoryRepository.findById("123")).willReturn(Mono.just(new Category("categorie 1")));
		
		BDDMockito.given(categoryRepository.save(any(Category.class)))
				.willReturn(Mono.just(new Category("categorie 1")));
		
		Mono<Category> toUpdate = Mono.just(new Category("cat 1"));
		
		client.patch()
				.uri("/api/v1/categories/123")
				.body(toUpdate, Category.class)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Category.class)
				.consumeWith(consumer -> assertEquals("categorie 1", consumer.getResponseBody().getName()));
		
		
		 ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository, times(1)).save(argumentCaptor.capture());
		verify(categoryRepository, times(1)).findById(any(String.class));

	}
	
	/**
	 * test patch when trying to update same category
	 * @throws Exception
	 */
	
	@Test
	void testPatchCategoryNoChanges() throws Exception {
		
		given(categoryRepository.findById("123")).willReturn(Mono.just(new Category("same category")));
		
		
		Mono<Category> toUpdate = Mono.just(new Category("same category"));
		

		client.patch()
				.uri("/api/v1/categories/123")
				.body(toUpdate, Category.class)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Category.class)
				.consumeWith(consumer -> assertEquals("same category", consumer.getResponseBody().getName()));
		
		
		ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository, never()).save(argumentCaptor.capture());
		verify(categoryRepository, times(1)).findById(any(String.class));

	}
}
