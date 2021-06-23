package com.example.dataproviders.inmemory;

import com.example.dataproviders.BookRepository;
import com.example.entities.Author;
import com.example.entities.Book;
import com.example.entities.exceptions.EntityNotFoundException;
import io.micronaut.core.annotation.Introspected;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Singleton;

@Singleton
@Introspected
class BookInMemoryRepository implements BookRepository {

  private final Map<String, Book> storage = new HashMap<>();

  @Override
  public Set<Book> getAll() {
    return new HashSet<>(storage.values());
  }

  @Override
  public Book getById(String id) {
    return Optional.ofNullable(storage.get(id))
        .orElseThrow(() -> new EntityNotFoundException(id, Book.class));
  }

  @Override
  public void save(Book book) {
    storage.put(book.getId(), book);
  }

  @Override
  public void saveAll(Collection<Book> books) {
    books.forEach(this::save);
  }

  @Override
  public Set<Author> getAllAuthors() {
    return storage.values().stream()
        .map(Book::getAuthor)
        .collect(Collectors.toSet());
  }
}
