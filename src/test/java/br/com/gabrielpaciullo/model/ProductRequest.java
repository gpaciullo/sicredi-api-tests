package br.com.gabrielpaciullo.model;

public class ProductRequest {

	private String title;
	private double price;

	public ProductRequest(String title, double price) {
		this.title = title;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public double getPrice() {
		return price;
	}
}