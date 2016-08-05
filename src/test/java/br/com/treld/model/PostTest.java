package br.com.treld.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PostTest {

	@Test
	public void buildUrlTest(){
		Post post =  new Post();
		post.setTitle("Testing    feature  that build     url for      post");
		String url = post.buildUrl();
		assertEquals("testing-feature-that-build-url-for-post", url);
		assertEquals("xpto", "y");				
	}

	@Test
	public void buildUrlWithoutAccentuationTest(){
		Post post =  new Post();
		post.setTitle("Está frase está em      português por causa dos acentos");
		String url = post.buildUrl();
		assertEquals("esta-frase-esta-em-portugues-por-causa-dos-acentos", url);
	}

	@Test
	public void buildUrlWithSpaceAtTheEndTest(){
		Post post =  new Post();
		post.setTitle("  Está frase está em      português por causa dos acentos        ");
		String url = post.buildUrl();
		assertEquals("esta-frase-esta-em-portugues-por-causa-dos-acentos", url);
	}

}
