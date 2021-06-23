package com.example.dataproviders;

import com.example.entities.Author;
import com.example.entities.Book;
import java.util.Collection;
import java.util.Set;

public interface BookRepository {

  Set<Book> getAll();

  Book getById(String id);

  void save(Book book);

  void saveAll(Collection<Book> books);

  Set<Author> getAllAuthors();
}
