package com.sapiensacademy.ProyectoRepo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProyectRepository {
	
	public ProyectRepository(WebDriver navegador) 
	{
		this.driver = navegador;
		PageFactory.initElements(driver, this);
	}
	
	WebDriver driver;
	
	@FindBy(id="homefeatured")
	WebElement homeFeatured;
	
	//@FindBy(xpath= "//*[@id=\"layer_cart\"]/div[1]/div[1]/h2")
	//WebElement ventanaPopUp;
	
	
	//By homeFeatured = By.id("homefeatured");
	By listaElementos = By.tagName("li");
	By addToCartButton = By.linkText("Add to cart");
	By ventanaPopUp = By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[1]/h2");
	By mensajeCarrito = By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/a");
	By ventanaTotal = By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/div/div/div/div/div[2]/span[1]");
	By botonCheckout1 = By.xpath("//*[@id=\"header\"]/div[3]/div/div/div[3]/div/div/div/div/div/div[2]/span[1]");
	
	
	public WebElement homefeatureElement() {
		//return driver.findElement(homeFeatured);
		return homeFeatured;
	}
	
	public List<WebElement> listarElementos(WebElement contenedorProductos) {
		return contenedorProductos.findElements(listaElementos);
	}
	
	public WebElement addToCart(WebElement productoLista) {
		return productoLista.findElement(addToCartButton);
	}
	
	public By elementoVentanaPopUp() {
		return ventanaPopUp;
	}
	public By elementoMensajeCarrito() {
		return mensajeCarrito;
	}
	public By elementoVentanaTotal() {
		return elementoVentanaTotal();
	}
	public By botonCheckout() {
		return botonCheckout();
	}
}
