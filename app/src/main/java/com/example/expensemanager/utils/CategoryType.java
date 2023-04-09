package com.example.expensemanager.utils;
public class CategoryType {
  private String id, name;
  private int image;

  public CategoryType() {
  }

  public CategoryType(String id, String name, int image) {
    this.id = id;
    this.name = name;
    this.image = image;
  }

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
    this.image = image;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "CategoryType{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", image=" + image + '}';
  }
}
