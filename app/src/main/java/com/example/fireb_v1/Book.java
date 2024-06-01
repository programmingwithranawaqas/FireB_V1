package com.example.fireb_v1;

public class Book {

    int issn;
    String name, author;
    double price;

    public Book() {
    }

    public Book(int issn, String name, String author, double price) {
        this.issn = issn;
        this.name = name;
        this.author = author;
        this.price = price;
    }

    public int getIssn() {
        return issn;
    }

    public void setIssn(int issn) {
        this.issn = issn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
