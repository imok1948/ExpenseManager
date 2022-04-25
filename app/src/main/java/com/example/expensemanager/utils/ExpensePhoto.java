package com.example.expensemanager.utils;
import android.net.Uri;

public class ExpensePhoto {

  private Uri thumbnailURI, imageUri;
  private String imageId;

  //imageId is MD5 of image stored in SQL along with its count because multiple expense may have same image
  //Delete the image if count==0

  public ExpensePhoto(Uri thumbnailURI, Uri imageUri, String imageId) {
    this.thumbnailURI = thumbnailURI;
    this.imageUri = imageUri;
    this.imageId = imageId;
  }

  public Uri getThumbnailURI() {
    return thumbnailURI;
  }

  public void setThumbnailURI(Uri thumbnailURI) {
    this.thumbnailURI = thumbnailURI;
  }

  public Uri getImageUri() {
    return imageUri;
  }

  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }



  @Override
  public String toString() {
    return "ExpensePhotos{" + "thumbnailURI=" + thumbnailURI + "\t\n imageUri=" + imageUri + "\t\n imageId='" + imageId + "'" + '}';
  }
}
