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
class MercadoMaquinasScraperTest {

    private WebDriver driver;
    private MercadoMaquinasScraper scraper;

    @BeforeEach
    void setUp() {
        driver = Mockito.mock(WebDriver.class);
        scraper = new MercadoMaquinasScraper(driver);
    }

    @Test
    void testScrapeDataFromMachine() {

        WebElement contractType = mock(WebElement.class);
        WebElement yearModel = mock(WebElement.class);
        WebElement mark = mock(WebElement.class);
        WebElement model = mock(WebElement.class);
        WebElement workerHours = mock(WebElement.class);
        WebElement city = mock(WebElement.class);
        WebElement price = mock(WebElement.class);
        WebElement image1 = mock(WebElement.class);
        WebElement image2 = mock(WebElement.class);

        when(contractType.getText()).thenReturn("Venda");
        when(yearModel.getText()).thenReturn("2020");
        when(mark.getText()).thenReturn("John Deere");
        when(model.getText()).thenReturn("6190J");
        when(workerHours.getText()).thenReturn("1500 horas");
        when(city.getText()).thenReturn("Campo Mourão/PR");
        when(price.getText()).thenReturn("R$ 500.000");

        when(image1.getAttribute("href")).thenReturn("http://site.com/image1.jpg");
        when(image2.getAttribute("href")).thenReturn("http://site.com/image2.jpg");

        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[1]/span")))
                .thenReturn(contractType);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[6]/span[2]")))
                .thenReturn(yearModel);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[4]/span[2]/a")))
                .thenReturn(mark);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[5]/span[2]/a")))
                .thenReturn(model);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[3]/div[3]/ul[3]/li/span[2]")))
                .thenReturn(workerHours);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[2]/div[1]/div/div/span[2]")))
                .thenReturn(city);
        when(driver.findElement(By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/div/span")))
                .thenReturn(price);

        when(driver.findElements(By.cssSelector("#ad-thumbnails a")))
                .thenReturn(List.of(image1, image2));

        Machine result = scraper.scrape("http://fake-url.com");

        assertNotNull(result);
        assertTrue(result instanceof MachineEntity);

        MachineEntity machine = (MachineEntity) result;
        assertEquals("Venda", machine.getContractType());
        assertEquals("2020", machine.getYearModel());
        assertEquals("John Deere", machine.getMark());
        assertEquals("6190J", machine.getModel());
        assertEquals("1500 horas", machine.getWorkedHours());
        assertEquals("Campo Mourão/PR", machine.getCity());
        assertEquals("R$ 500.000", machine.getPrice());
        assertEquals(2, machine.getImages().size());
        assertTrue(machine.getImages().contains("http://site.com/image1.jpg"));
    }
}