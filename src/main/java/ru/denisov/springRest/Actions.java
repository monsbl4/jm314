package ru.denisov.springRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.denisov.springRest.Model.User;

import java.util.Objects;

@Component
public class Actions {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private String URL = "http://91.241.64.178:7081/api/users";


    @Autowired
    public Actions(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.headers.set("Cookie", String.join(";", Objects.requireNonNull(restTemplate.headForHeaders(URL).get("Set-Cookie"))));
    }

    private ResponseEntity<String> add() {
        User user = new User(3L, "James", "Brown", (byte) 32);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(URL, entity, String.class);
    }

    private ResponseEntity<String> update() {
        User user = new User(3L, "Thomas", "Shelby", (byte) 32);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class, 3);
    }

    private ResponseEntity<String> delete() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(URL + "/{id}", HttpMethod.DELETE, entity, String.class, 3);
    }

    public String getResult() {
        return add().getBody() + update().getBody() + delete().getBody();
    }
}