package com.example.expensemanager.utils;
public class Account {
  public static final int EXPENSE = 1;
  private String name, id;
  private float balance;
  //Also add currency type, description

  public Account() {
  }

  public Account(String name, String id) {
    this.name = name;
    this.id = id;
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
    return "Account{" + "name='" + name + "'" + "\t\n id='" + id + "'" + '}';
  }
}
