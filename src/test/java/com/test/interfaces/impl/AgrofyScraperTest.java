package com.test.interfaces.impl;

import com.test.domain.MachineEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgrofyScraperTest {

    private WebDriver driver;
    private AgrofyScraper scraper;

    @BeforeEach
    void setUp() {
        driver = mock(WebDriver.class);
        scraper = new AgrofyScraper(driver);
    }

    @Test
    void testScrapeDataFromMachine_success() {

        WebElement elementPrice = mock(WebElement.class);
        WebElement elementContractType = mock(WebElement.class);
        WebElement elementCity = mock(WebElement.class);
        WebElement elementMark = mock(WebElement.class);
        WebElement elementModel = mock(WebElement.class);
        WebElement elementYearModel = mock(WebElement.class);
        WebElement elementWorkerHours = mock(WebElement.class);
        WebElement carouselImg = mock(WebElement.class);
        WebElement container = mock(WebElement.class);
        WebElement img1 = mock(WebElement.class);
        WebElement img2 = mock(WebElement.class);

        when(elementPrice.getText()).thenReturn("R$ 120.000,00");
        when(elementContractType.getText()).thenReturn("Venda");
        when(elementCity.getText()).thenReturn("Campo Mourão/PR");
        when(elementMark.getText()).thenReturn("John Deere");
        when(elementModel.getText()).thenReturn("6190J");
        when(elementYearModel.getText()).thenReturn("2021");
        when(elementWorkerHours.getText()).thenReturn("Horas: 2500");
        when(img1.getAttribute("src")).thenReturn("http://site.com/img1.jpg");
        when(img2.getAttribute("src")).thenReturn("http://site.com/img2.jpg");

        when(driver.findElement(By.xpath("//*[@id=\"ProductInfo\"]/div[2]/span"))).thenReturn(elementPrice);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[1]/span/ul/li"))).thenReturn(elementContractType);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[4]/span/ul/li"))).thenReturn(elementCity);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[6]/span/ul/li"))).thenReturn(elementMark);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[7]/span/ul/li"))).thenReturn(elementModel);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[9]/span/ul/li"))).thenReturn(elementYearModel);
        when(driver.findElement(By.xpath("//*[@id='Descrição']"))).thenReturn(elementWorkerHours);
        when(driver.findElement(By.xpath("//*[@id=\"ProductCarousel\"]/div"))).thenReturn(carouselImg);
        when(driver.findElement(By.xpath("//*[@id=\"pdp-body\"]/div[3]/div[1]/div/div/div/div[2]/div[1]/div"))).thenReturn(container);

        when(container.findElements(any())).thenReturn(List.of(img1, img2));

        doNothing().when(carouselImg).click();

        MachineEntity result = (MachineEntity) scraper.scrape("http://fake-url.com");

        assertEquals("R$ 120.000,00", result.getPrice());
        assertEquals("Venda", result.getContractType());
        assertEquals("Campo Mourão/PR", result.getCity());
        assertEquals("John Deere", result.getMark());
        assertEquals("6190J", result.getModel());
        assertEquals("2021", result.getYearModel());
        assertEquals("2500", result.getWorkedHours());
        assertEquals(2, result.getImages().size());
        assertTrue(result.getImages().contains("http://site.com/img1.jpg"));

        verify(carouselImg, times(1)).click();
    }
}
