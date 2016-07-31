package br.com.treld.rest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;

import br.com.treld.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.treld.TreldTest;
import br.com.treld.model.Post;

/**
 * Created by edubranquinho on 29/07/16.
 */
@TreldTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PostControllerTest {

	private static final String POST_API = "/api/post/";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private Filter springSecurityFilterChain;

    @Autowired
    private PostRepository postRepository;

	private MockMvc mockMvc;
	private HttpSession session;
	private ObjectMapper mapper = new ObjectMapper();
	private Post postCreatedInTest = new Post();

	@Before
	public void setup() throws Exception {
        postRepository.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain)
				.build();
		login();
	}

	public void login() throws Exception {
		this.session = mockMvc.perform(post("/login").param("username", "treld").param("password", "treld")).andReturn()
				.getRequest().getSession();
	}

	/*
	 * This test validates the URI that is generated when a post is created. It
	 * also validates if the post was really created.
	 *
	 * @Author: Eduardo
	 */
	@Test
	public void validateUriPost() throws Exception {
		String postJson = " { \"title\":\"Creating rest api\", \"body\":\"open your ide...\" } ";
		ResultActions result = mockMvc.perform(post(POST_API).content(postJson)
				.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) session))
				.andExpect(status().isCreated());
		String localResourceCreated = result.andReturn().getResponse().getRedirectedUrl();
		ResultActions resultSearch = mockMvc.perform(get(localResourceCreated).session((MockHttpSession) session));
		String responseString = resultSearch.andReturn().getResponse().getContentAsString();
		postCreatedInTest = mapper.readValue(responseString, Post.class);
		assertTrue("Creating rest api".equals(postCreatedInTest.getTitle()));
	}

	@Test
	public void validateModification() throws Exception {
		String postJson = " { \"title\":\"Creating rest api part 2\", \"body\":\"open your eclipse...\" } ";
		mockMvc.perform(put(POST_API + postCreatedInTest.getId()).session((MockHttpSession) session)
				.content(postJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		ResultActions resultSearch = mockMvc
				.perform(get(POST_API + postCreatedInTest.getId()).session((MockHttpSession) session))
				.andExpect(status().isOk());
		String responseString = resultSearch.andReturn().getResponse().getContentAsString();
		Post postModified = mapper.readValue(responseString, Post.class);
		assertEquals(postModified.getTitle(), "Creating rest api part 2");
	}

	@Test
	public void validateDeletion() throws Exception {
		mockMvc.perform(delete(POST_API + postCreatedInTest.getId()).session((MockHttpSession) session))
				.andExpect(status().isNoContent());
	}

}
