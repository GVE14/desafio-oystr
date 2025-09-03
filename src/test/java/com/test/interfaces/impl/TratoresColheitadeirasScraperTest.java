package com.test.interfaces.impl;

import com.test.domain.MachineEntity;
import com.test.interfaces.Machine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TratoresColheitadeirasScraperTest {

	private WebDriver driver;
	private TratoresColheitadeirasScraper scraper;

	@BeforeEach
	void setUp() {
		driver = Mockito.mock(WebDriver.class);
		scraper = new TratoresColheitadeirasScraper(driver);
	}

	@Test
	void testScrapeDataFromMachine() {

		WebElement price = mock(WebElement.class);
		WebElement model = mock(WebElement.class);
		WebElement mark = mock(WebElement.class);
		WebElement yearModel = mock(WebElement.class);
		WebElement city = mock(WebElement.class);
		WebElement zoomButton = mock(WebElement.class);
		WebElement image1 = mock(WebElement.class);
		WebElement image2 = mock(WebElement.class);

		when(price.getText()).thenReturn("R$ 350.000");
		when(model.getText()).thenReturn("Case 8120");
		when(mark.getText()).thenReturn("Case IH");
		when(yearModel.getText()).thenReturn("2018");
		when(city.getText()).thenReturn("Localização:\nUberlândia/MG");

		when(image1.getAttribute("src")).thenReturn("http://site.com/image1.jpg");
		when(image2.getAttribute("src")).thenReturn("http://site.com/image2.jpg");

		when(driver.findElement(By.xpath("//*[@id=\"ProductPrice-product-template\"]/span")))
				.thenReturn(price);
		when(driver.findElement(By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[4]/strong")))
				.thenReturn(model);
		when(driver.findElement(By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[3]/strong")))
				.thenReturn(mark);
		when(driver.findElement(By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[6]/strong")))
				.thenReturn(yearModel);
		when(driver.findElement(By.xpath("//*[@id=\"page-content\"]/div[2]/div/div[1]/div/div[2]/div[2]/ul/li[2]")))
				.thenReturn(city);
		when(driver.findElement(By.xpath("//*[@id=\"linkImagem\"]/i")))
				.thenReturn(zoomButton);

		when(driver.findElements(By.cssSelector("#gallery img")))
				.thenReturn(List.of(image1, image2));

		Machine result = scraper.scrape("http://fake-url.com");

		assertNotNull(result);
		assertTrue(result instanceof MachineEntity);

		MachineEntity machine = (MachineEntity) result;
		assertEquals("R$ 350.000", machine.getPrice());
		assertEquals("Case 8120", machine.getModel());
		assertEquals("Case IH", machine.getMark());
		assertEquals("2018", machine.getYearModel());
		assertEquals("Uberlândia/MG", machine.getCity());
		assertEquals(2, machine.getImages().size());
		assertTrue(machine.getImages().contains("http://site.com/image1.jpg"));

		verify(zoomButton, times(1)).click();
	}
}
