package com.example.expensemanager.utils;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ExpenseModel {
  private float amount;
  private String description;
  private CategoryType category;
  private Calendar calendar;
  private Account account;
  private List<ExpensePhotos> photos;

  public ExpenseModel(float amount, String description, CategoryType category, Calendar calendar, Account account, List<ExpensePhotos> photos) {
    this.amount = amount;
    this.description = description;
    this.category = category;
    this.calendar = calendar;
    this.account = account;
    this.photos = photos;
  }

  public ExpenseModel(float amount, String description, CategoryType category, Calendar calendar, Account account) {
    this.amount = amount;
    this.description = description;
    this.category = category;
    this.calendar = calendar;
    this.account = account;
  }

  public ExpenseModel() {
    amount = 0;
    description = "";
    category = null;
    account = null;
    photos = new LinkedList<>();
  }

  @Override
  public String toString() {
    String[] monthStrings = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    return "ExpenseModel{" + "amount=" + amount + "\t\n description='" + description + "'" + "\t\n category=" + category.getName() + "\t\n calendar=(" + calendar.get(Calendar.DATE) + " " + monthStrings[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + ")\t\n account=" + account.getName() + "\t\n photos=" + photos + '}';
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CategoryType getCategory() {
    return category;
  }

  public void setCategory(CategoryType category) {
    this.category = category;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public List<ExpensePhotos> getPhotos() {
    return photos;
  }

  public void setPhotos(List<ExpensePhotos> photos) {
    this.photos = photos;
  }
}
