package com.example.entities.exceptions;

public class EntityNotFoundException extends RuntimeException {

  private static final String MESSAGE_TEMPLATE = "Entity of class %s with id %s not found";

  public EntityNotFoundException(String id, Class<?> entityClass) {
    super(String.format(MESSAGE_TEMPLATE, entityClass, id));
  }
}
