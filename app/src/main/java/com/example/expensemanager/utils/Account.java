package com.example.expensemanager.utils;
public class Account {
  private String type, name, id;

  public Account(String type, String name, String id) {
    this.type = type;
    this.name = name;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "com.example.expensemanager.utils.Account{" + "type='" + type + "'" + "\t\n name='" + name + "'" + "\t\n id='" + id + "'" + '}';
  }
}
