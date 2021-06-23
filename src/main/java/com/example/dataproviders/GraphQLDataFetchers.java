package com.example.dataproviders;

import com.example.entities.Author;
import com.example.entities.Book;
import graphql.schema.DataFetcher;
import io.micronaut.core.annotation.Introspected;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@Introspected
@RequiredArgsConstructor
public class GraphQLDataFetchers {

  private final BookRepository bookRepository;

  public DataFetcher<Book> getBookByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String bookId = dataFetchingEnvironment.getArgument("id");
      return bookRepository.getById(bookId);
    };
  }

  public DataFetcher<Author> getAuthorDataFetcher() {
    return dataFetchingEnvironment -> {
      Book book = dataFetchingEnvironment.getSource();
      return bookRepository.getAllAuthors().stream()
          .filter(a -> a.getId().equals(book.getId()))
          .findFirst()
          .orElse(null);
    };
  }
}
