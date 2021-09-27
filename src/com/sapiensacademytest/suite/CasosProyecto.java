package com.sapiensacademytest.suite;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.testng.Assert;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.sapiensacademy.ProyectoRepo.ProyectRepository;
import com.sapiensacademytest.repository.CartRepository;

public class CasosProyecto {
	WebDriver customDriver = new ChromeDriver();
	List<WebElement> listaDeProductos = new ArrayList<WebElement>();
	int cantidadLista;
	int numeroAlAzar;
	List <Integer> productosAlAzar = new ArrayList<Integer>();
	double totalProductos = 0;
	String mensajeDeVentana = new String();
	String mensajeCarrito = new String();
	WebDriverWait espera = new WebDriverWait(customDriver, 20);
	ProyectRepository repositorio = new ProyectRepository(customDriver);

	//Anotaciones 
	@BeforeSuite
	public void abrirNavegador() {
		customDriver.get("http://automationpractice.com/index.php");
		Dimension driverSize = new Dimension(1170,700);
		customDriver.manage().window().setSize(driverSize);
	}
	
	@AfterSuite
	public void cerrarNavegador() {
		customDriver.close();		
	}
	
	
	//0001 Elegir y agregar objetos al carrito 
	@Test
	public void elegirArticulosAzarCarritoDeCompras() throws InterruptedException {
		Assert.assertEquals(customDriver.getTitle(), "My Store");
		WebElement contenedorProductos = repositorio.homefeatureElement();
		listaDeProductos = repositorio.listarElementos(contenedorProductos);
		System.out.println("Este es el tamaño de la lista: " + listaDeProductos.size());
		
		if (listaDeProductos.size()<4) {
			cantidadLista = listaDeProductos.size();
		} else {
			cantidadLista = 3;
		}
		for (int i=0; i<cantidadLista; i++) {
			numeroAlAzar = (int) (Math.random()* listaDeProductos.size() + 1);
			if (productosAlAzar.contains(numeroAlAzar)) {
				i--;
			} else {
				productosAlAzar.add(numeroAlAzar);
			}
		}
		
		productosAlAzar.forEach(System.out::println);
	}
	
	@Test(dependsOnMethods= {"elegirArticulosAzarCarritoDeCompras"})
	public void anadirProductosAlCarrito() throws InterruptedException {
		for (int i=0; i<cantidadLista; i++) {
			customDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			String elementXpath = String.format("//*[@id=\"homefeatured\"]/li[%s]/div/div[2]/div[1]/span", productosAlAzar.get(i));
			String precioUnitarioDolar = listaDeProductos.get(productosAlAzar.get(i)-1).findElement(By.xpath(elementXpath)).getText();
			String precioUnitario = precioUnitarioDolar.replace("$", "");
			Double precio = Double.valueOf(precioUnitario);
			System.out.println("Valor unitario del producto: " + precio);
			totalProductos += precio;
			System.out.println("Total de los valores: " + totalProductos);
			repositorio.addToCart(listaDeProductos.get(productosAlAzar.get(i)-1)).click();
			if (i==0) {
				mensajeDeVentana = "There is 1 item in your cart.";
				mensajeCarrito = "Cart 1 Product";
			} else {
				mensajeDeVentana = String.format("There are %s items in your cart.", i+1);
				mensajeCarrito = String.format("Cart %s Products", i+1);				
			}
			WebElement ventanaDeProducto = espera.until(ExpectedConditions.visibilityOfElementLocated(repositorio.elementoVentanaPopUp()));
			Assert.assertEquals(customDriver.findElement(repositorio.elementoVentanaPopUp()).getText(), "Product successfully added to your shopping cart");
			
			Assert.assertEquals(customDriver.findElement(repositorio.elementoVentanaPopUp()).getText() , mensajeDeVentana);
			Assert.assertEquals(customDriver.findElement(repositorio.elementoMensajeCarrito()).getText() , mensajeCarrito);
			customDriver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span")).click();
			//Boolean ventanaDesaparecida = espera.until(ExpectedConditions.visibilityOfElementLocated(repositorio.elementoVentanaPopUp()));
		}	
	}
	//Agregarlos para que se corra primero el método de añadirse
	@Test(dependsOnMethods= {"anadirProductosAlCarrito"})
	public void verificarProductos() {
		//Verificaciones 
		Actions mouseAction = new Actions(customDriver);
		WebElement cartOptions = customDriver.findElement(repositorio.elementoMensajeCarrito());
		mouseAction.moveToElement(cartOptions).perform();
		totalProductos += 2.00;
		
		//Dos cifras
		DecimalFormat df2 = new DecimalFormat("#.00");
		
		String totalEnString = String.format("$%s", df2.format(totalProductos));
		totalEnString = totalEnString.replace("," , ".");
		
		WebElement ventanaTotal = espera.until(ExpectedConditions.visibilityOfElementLocated(repositorio.elementoVentanaTotal()));
		Assert.assertEquals(customDriver.findElement(repositorio.botonCheckout()).getText(), totalEnString);
		WebElement botonCheckout = espera.until(ExpectedConditions.visibilityOfElementLocated(By.id("button_order_cart")));		

	}

}
