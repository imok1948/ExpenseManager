package com.example.expensemanager.utils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ExpenseModel {
  private float amount;
  private String description;
  private CategoryType category;
  private Calendar calendar;
  private Account account;
  private HashMap<String, ExpensePhoto> expensePhotosHashMap;

  public ExpenseModel(float amount, String description, CategoryType category, Calendar calendar, Account account, HashMap<String, ExpensePhoto> expensePhotosHashMap) {
    this.amount = amount;
    this.description = description;
    this.category = category;
    this.calendar = calendar;
    this.account = account;
    this.expensePhotosHashMap = expensePhotosHashMap;
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
    expensePhotosHashMap = new HashMap<>();
  }

  @Override
  public String toString() {
    return "ExpenseModel{" + "amount=" + amount + "\t\n description='" + description + "'" + "\t\n category=" + category + "\t\n calendar=" + calendar + "\t\n account=" + account + "\t\n Total expensePhotosHashMap=" + expensePhotosHashMap.size() + '}';
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


}
