package edu.sjsu.cmpe275.lab2.Lab2;

import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
//import org.assertj.core.api.Assert;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Lab2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab2ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	void contextLoads() {
	}

//	@Test
//	public void testGetAllUsers() {
//		HttpHeaders headers = new HttpHeaders();
//		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/users",
//				HttpMethod.GET, entity, String.class);
//		Assert.notNull(response.getBody());
//	}

//	@Test
//	public void testCreateUser() {
//		Player user = new Player();
//		user.setEmail("admin@gmail.com");
//		user.setFirstName("admin");
//		user.setLastName("admin");
//		user.setDescription("admin");
//		user.setAddress("admin");
//		user.setSponsor("admin");
//		ResponseEntity<Player> postResponse = restTemplate.postForEntity(getRootUrl() + "/createPlayers", user, Player.class);
//		Assert.notNull(postResponse);
//		Assert.notNull(postResponse.getBody());
//	}
}
