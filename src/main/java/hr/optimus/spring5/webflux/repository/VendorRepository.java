package hr.optimus.spring5.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hr.optimus.spring5.webflux.model.Vendor;

@Repository
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {

}
