package com.example.expensemanager.utils;
import java.util.Calendar;
import java.util.HashMap;

public class ExpenseModel {
  private float amount;
  private String description;
  private CategoryType category;
  private long timestamp;
  private Account account;
  private HashMap<String, ExpensePhoto> expensePhotosHashMap;

  public ExpenseModel(float amount, String description, CategoryType category, long timestamp, Account account, HashMap<String, ExpensePhoto> expensePhotosHashMap) {
    this.amount = amount;
    this.description = description;
    this.category = category;
    this.timestamp = timestamp;
    this.account = account;
    this.expensePhotosHashMap = expensePhotosHashMap;
  }

  public ExpenseModel(float amount, String description, CategoryType category, long timestamp, Account account) {
    this.amount = amount;
    this.description = description;
    this.category = category;
    this.timestamp = timestamp;
    this.account = account;
  }

  public ExpenseModel() {
    amount = 0;
    description = "";
    category = null;
    account = null;
    expensePhotosHashMap = new HashMap<>();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("");
    for (String key : expensePhotosHashMap.keySet()) {
      sb.append(expensePhotosHashMap.get(key) + "\t\n");
    }
    return "ExpenseModel{" + "amount=" + amount + "\t\n description='" + description + "'" + "\t\n category=" + category + "\t\n calendar=" + timestamp + "\t\n account=" + account + "\t\n Total expensePhotosHashMap=" + expensePhotosHashMap.size() + "\t\n Photos=" + sb + '}';
  }


  public HashMap<String, ExpensePhoto> getExpensePhotosHashMap() {
    return expensePhotosHashMap;
  }

  public void setExpensePhotosHashMap(HashMap<String, ExpensePhoto> expensePhotosHashMap) {
    this.expensePhotosHashMap = expensePhotosHashMap;
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

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }


}
