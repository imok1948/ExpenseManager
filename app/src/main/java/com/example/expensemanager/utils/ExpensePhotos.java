package com.example.expensemanager.utils;
public class ExpensePhotos {
  private String imageBase64;
  private String imageId;
  //imageId is MD5 of image stored in SQL along with its count because multiple expense may have same image
  //Delete the image if count==0

  public ExpensePhotos(String imageBase64, String imageId) {
    this.imageBase64 = imageBase64;
    this.imageId = imageId;
  }

  public String getImageBase64() {
    return imageBase64;
  }

  public void setImageBase64(String imageBase64) {
    this.imageBase64 = imageBase64;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  @Override
  public String toString() {
    return "com.example.expensemanager.utils.ExpensePhotos{" + "imageBase64='" + imageBase64 + "'" + "\t\n imageId='" + imageId + "'" + '}';
  }
}
