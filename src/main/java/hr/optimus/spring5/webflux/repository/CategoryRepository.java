package hr.optimus.spring5.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hr.optimus.spring5.webflux.model.Category;

@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

}
