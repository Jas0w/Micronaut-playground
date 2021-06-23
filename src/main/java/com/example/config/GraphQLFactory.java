package com.example.config;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import com.example.dataproviders.BookRepository;
import com.example.dataproviders.GraphQLDataFetchers;
import com.example.entities.Author;
import com.example.entities.Book;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Factory
public class GraphQLFactory {

  @Inject
  private BookRepository bookRepository;

  @Bean
  @Singleton
  public GraphQL graphQL(ResourceResolver resourceResolver, GraphQLDataFetchers graphQLDataFetchers) {
    Book first = new Book("1", "Lord of the rings", 10000, new Author("1", "John", "Talkien"));
    Book second = new Book("2", "Game of the thrones", 10000, new Author("2", "George", "Martin"));
    bookRepository.saveAll(Arrays.asList(first, second));

    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    Optional<InputStream> graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls");

    if (graphqlSchema.isPresent()) {
      typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(graphqlSchema.get()))));

      RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
          .type(newTypeWiring("Query")
              .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
          .type(newTypeWiring("Book").dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
          .build();

      SchemaGenerator schemaGenerator = new SchemaGenerator();
      GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

      return GraphQL.newGraphQL(graphQLSchema).build();
    }

    log.debug("No GraphQL services found, returning empty schema");
    return new GraphQL.Builder(GraphQLSchema.newSchema().build()).build();
  }
}
