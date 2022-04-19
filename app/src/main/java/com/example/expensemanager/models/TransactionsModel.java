package com.example.expensemanager.models;
public class TransactionsModel {
  private int transactionImage;
  private String transactionDescription, transactionCategory, transactionDate;
  private String transactionAmount;

  private Boolean isDate = false;

  private String dateDate, dateMonth, dateYear, dateDay;
  private boolean endBlock = false;

  public TransactionsModel(Boolean isDate, String dateDate, String dateMonth, String dateYear, String dateDay) {
    this.isDate = isDate;
    this.dateDate = dateDate;
    this.dateMonth = dateMonth;
    this.dateYear = dateYear;
    this.dateDay = dateDay;
  }

  public TransactionsModel(Boolean isDate, int transactionImage, String transactionDescription, String transactionCategory, String transactionAmount, String transactionDate) {
    this.isDate = isDate;
    this.transactionImage = transactionImage;
    this.transactionDescription = transactionDescription;
    this.transactionCategory = transactionCategory;
    this.transactionAmount = transactionAmount;
    this.transactionDate = transactionDate;
  }

  public TransactionsModel(boolean endBlock) {
    this.endBlock = endBlock;
  }

  @Override
  public String toString() {
    return "TransactionsModel{" + "transactionImage=" + transactionImage + "\n transactionDescription='" + transactionDescription + '\'' + "\n transactionCategory='" + transactionCategory + '\'' + "\n transactionAmount='" + transactionAmount + '\'' + "\n transactionDate='" + transactionDate + '\'' + "\n isDate=" + isDate + "\n dateDate='" + dateDate + '\'' + "\n dateMonth='" + dateMonth + '\'' + "\n dateYear='" + dateYear + '\'' + "\n dateDay='" + dateDay + '\'' + "\n endBlock=" + endBlock + '}';
  }

  public Boolean isDate() {
    return isDate;
  }

  public String getDateDate() {
    return dateDate;
  }

  public void setDateDate(String dateDate) {
    this.dateDate = dateDate;
  }

  public String getDateMonth() {
    return dateMonth;
  }

  public void setDateMonth(String dateMonth) {
    this.dateMonth = dateMonth;
  }

  public String getDateYear() {
    return dateYear;
  }

  public void setDateYear(String dateYear) {
    this.dateYear = dateYear;
  }

  public String getDateDay() {
    return dateDay;
  }

  public void setDateDay(String dateDay) {
    this.dateDay = dateDay;
  }

  public int getTransactionImage() {
    return transactionImage;
  }

  public void setTransactionImage(int transactionImage) {
    this.transactionImage = transactionImage;
  }

  public String getTransactionDescription() {
    return transactionDescription;
  }

  public void setTransactionDescription(String transactionDescription) {
    this.transactionDescription = transactionDescription;
  }

  public String getTransactionCategory() {
    return transactionCategory;
  }

  public void setTransactionCategory(String transactionCategory) {
    this.transactionCategory = transactionCategory;
  }

  public String getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(String transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public String getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(String transactionDate) {
    this.transactionDate = transactionDate;
  }

  public boolean isEndBlock() {
    return endBlock;
  }
}
