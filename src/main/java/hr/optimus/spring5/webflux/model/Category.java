package hr.optimus.spring5.webflux.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class Category {
	
	@Id
	private String id;
	
	private String name;
	
	public Category(String p_name) {
		this.name = p_name;
	}

}
