package br.com.treld.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by rsouza on 16/07/16.
 */
@Document(collection = "authors")
public class Author {

	@Id
	@NotEmpty(message = "the author most have a username")
	private String username;
	@NotEmpty(message = "the author most have a password")
	private String password;

	public Author(String username, String password) {
		this.username=username;
		this.password=password;
	}

	public Author() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
