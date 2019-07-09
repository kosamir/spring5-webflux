package hr.optimus.spring5.webflux.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class Vendor {
	
	private String id;
	
	private String name;
	private String lastName;
	
	
	public Vendor(String p_name, String p_lastName) {
		this.name = p_name;
		this.lastName = p_lastName;
	}

}
