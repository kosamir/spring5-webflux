package hr.optimus.spring5.webflux.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hr.optimus.spring5.webflux.model.Category;
import hr.optimus.spring5.webflux.model.Vendor;
import hr.optimus.spring5.webflux.repository.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/v1/vendors")
public class VendorController {
	
	private VendorRepository vendorRepository;

	@Autowired
	public VendorController(VendorRepository vendorRepository) {
		super();
		this.vendorRepository = vendorRepository;
	}
	
	@GetMapping(path = {"","/", " "})
	public Flux<Vendor> getAllVendors(){
		return vendorRepository.findAll();
	}
	
	@GetMapping(path = "/{id}")
	public Mono<Vendor> getVendorById(@PathVariable String id){
		return vendorRepository.findById(id);
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(path = {"","/"," "})
	public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream){
		return vendorRepository.saveAll(vendorStream).then();
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PutMapping(path="/{id}")
	public Mono<Vendor> updateVendor(@RequestBody Vendor vendor, @PathVariable String id){
		vendor.setId(id);
		return vendorRepository.save(vendor).cache();
	}
	
	@ResponseStatus(code = HttpStatus.OK) 
	@PatchMapping(path = "/{id}")
	public Mono<Vendor> patchVendor(@RequestBody Vendor vendor, @PathVariable String id){
		
		Vendor vendorToUpdate = vendorRepository.findById(id).block();
		
		if(!vendorToUpdate.equals(vendor)) {
			vendorToUpdate.setName(vendor.getName());
			vendorToUpdate.setLastName(vendor.getLastName());
			return vendorRepository.save(vendorToUpdate);
		}
		return Mono.just(vendorToUpdate);
	}
	

}
