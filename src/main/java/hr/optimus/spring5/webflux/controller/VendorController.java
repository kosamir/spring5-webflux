package hr.optimus.spring5.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	

}