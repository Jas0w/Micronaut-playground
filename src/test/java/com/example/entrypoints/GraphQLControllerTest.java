package com.example.entrypoints;

import static graphql.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.Map;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
public class GraphQLControllerTest {

  private static final String BOOK_BY_ID_QUERY = "{ \"query\": \"{ bookById(id:\\\"%s\\\") { name, pageCount, author { firstName, lastName} } }\" }";

  @Inject
  @Client("/")
  RxHttpClient client;

  @Test
  void shouldReturnBookDataById() {
    HttpRequest<String> req = HttpRequest.POST("/graphql", String.format(BOOK_BY_ID_QUERY, 1));
    HttpResponse<Map> res = client.toBlocking().exchange(req, Argument.of(Map.class));
    assertEquals(HttpStatus.OK, res.getStatus());
    assertNotNull(res.getBody());

    Map bookInfo = (Map) res.getBody(Map.class).get().get("data");
    Map bookById = (Map) bookInfo.get("bookById");
    assertEquals("Lord of the rings", bookById.get("name"));
    assertEquals(10000, bookById.get("pageCount"));

    Map author = (Map) bookById.get("author");
    assertEquals("John", author.get("firstName"));
    assertEquals("Talkien", author.get("lastName"));
  }

}
