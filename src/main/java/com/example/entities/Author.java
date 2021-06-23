package com.example.entities;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Introspected
@RequiredArgsConstructor
public class Author {

  private final String id;
  private final String firstName;
  private final String lastName;
}


