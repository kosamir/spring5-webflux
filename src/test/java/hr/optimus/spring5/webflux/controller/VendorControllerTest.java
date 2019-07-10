package hr.optimus.spring5.webflux.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;

import hr.optimus.spring5.webflux.model.Vendor;
import hr.optimus.spring5.webflux.repository.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class VendorControllerTest {

	WebTestClient client;
	@Mock
	private VendorRepository vendorRepository;
	@InjectMocks
	private VendorController vendorController;

	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	protected void setUp() throws Exception {

		client = WebTestClient.bindToController(vendorController).build();
	}

	@Test
	public void testGetAllVendors() throws Exception {
		BDDMockito.given(vendorRepository.findAll()).willReturn(Flux.just(new Vendor("Vendor1 Name", "Vendor1 Last name"),
				new Vendor("Vendor2 Name", "Vendor2 Last name"),
				new Vendor("Vendor3 Name", "Vendor3 Last name")));
		
		client.get().uri("/api/v1/vendors")
			.exchange()
			.expectBodyList(Vendor.class)
			.hasSize(3)
			.consumeWith(
					consumer->assertEquals("Vendor1 Name", consumer.getResponseBody().get(0).getName()));
	
	}

	@Test
	public void testGetVendorById() throws Exception {
		BDDMockito.given(vendorRepository.findById("2346")).willReturn(Mono.just(new Vendor("v1","v1-1")));
		
		client
			.get()
			.uri("/api/v1/vendors/2346")
			.exchange()
			.expectBody(Vendor.class)
			.consumeWith(consumer -> assertEquals("v1", consumer.getResponseBody().getName()));
		
	}
	
	@Test
	void testCreateNewVendor() throws Exception {
		
		BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
				.willReturn(Flux.just(new Vendor()));
		
		Publisher<Vendor> toUpdate = Mono.just(new Vendor("ven2", "ven2_2"));
		
		client.post()
				.uri("/api/v1/vendors")
				.body(toUpdate, Vendor.class)
				.exchange()
				.expectStatus()
				.isCreated();
		
	}
	
	@Test
	void testUpdateExistingVendor() throws Exception {
		
		BDDMockito.given(vendorRepository.save(any(Vendor.class)))
					.willReturn(Mono.just(new Vendor("vendo1","vendor11")));
		
		Mono<Vendor> toUpdate = Mono.just(new Vendor("ven1", "ven11"));
		
		client.put()
				.uri("/api/v1/vendors/1234")
				.body(toUpdate, Vendor.class)
				.exchange()
				.expectBody(Vendor.class)
				.consumeWith(consumer->assertEquals("vendo1", consumer.getResponseBody().getName()));
		
		 ArgumentCaptor<Vendor> argumentCaptor = ArgumentCaptor.forClass(Vendor.class);
		
		verify(vendorRepository, times(1)).save(argumentCaptor.capture());
		
		
	}

}
