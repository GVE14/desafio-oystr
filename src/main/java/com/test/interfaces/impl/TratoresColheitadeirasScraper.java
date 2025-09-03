package com.test.interfaces.impl;

import com.test.domain.MachineEntity;
import com.test.interfaces.Machine;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TratoresColheitadeirasScraper extends AbstractScraper {

    private static final Logger logger = LoggerFactory.getLogger(TratoresColheitadeirasScraper.class);

    // XPaths e seletores como constantes
    private static final By XPATH_PRICE = By.xpath("//*[@id=\"ProductPrice-product-template\"]/span");
    private static final By XPATH_MODEL = By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[4]/strong");
    private static final By XPATH_MARK = By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[3]/strong");
    private static final By XPATH_YEAR_MODEL = By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/p[6]/strong");
    private static final By XPATH_CITY = By.xpath("//*[@id=\"page-content\"]/div[2]/div/div[1]/div/div[2]/div[2]/ul/li[2]");
    private static final By XPATH_ZOOM_BUTTON = By.xpath("//*[@id=\"linkImagem\"]/i");
    private static final By XPATH_IMAGES = By.cssSelector("#gallery img");

    public TratoresColheitadeirasScraper(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getDomain() {
        return "tratoresecolheitadeiras";
    }

    @Override
    public Machine scrape(String url) {

        try {

            return scrapeDataFromMachine(url);
        } catch (Exception e) {
            logger.error("Error scraping machine data from URL: {}", url, e);
            throw new RuntimeException("Scraping failed for URL: " + url, e);
        }
    }

    private Machine scrapeDataFromMachine(String url) {

        loadPage(url);

        MachineEntity machinery = new MachineEntity();

        machinery.setPrice(getText(XPATH_PRICE));
        machinery.setModel(getText(XPATH_MODEL));
        machinery.setMark(getText(XPATH_MARK));
        machinery.setYearModel(getText(XPATH_YEAR_MODEL));
        machinery.setCity(filterCity(getText(XPATH_CITY)));

        clickElement(XPATH_ZOOM_BUTTON);
        captureImages(machinery);

        return machinery;
    }

    private String getText(By locator) {

        try {

            WebElement element = driver.findElement(locator);
            return element.getText();
        } catch (Exception e) {
            logger.warn("Failed to get text for locator: {}", locator, e);
            return null;
        }
    }

    private void clickElement(By locator) {

        try {

            WebElement element = driver.findElement(locator);
            element.click();
        } catch (Exception e) {
            logger.warn("Failed to click element for locator: {}", locator, e);
        }
    }

    private String filterCity(String text) {

        if (text == null) return null;
        String[] parts = text.split("\n");
        return parts.length > 1 ? parts[1] : text;
    }

    private void captureImages(MachineEntity machinery) {

        List<String> imageUrls = new ArrayList<>();
        List<WebElement> images = driver.findElements(XPATH_IMAGES);

        for (WebElement img : images) {
            String src = img.getAttribute("src");
            if (src != null && !src.isEmpty()) {
                imageUrls.add(src);
            }
        }

        machinery.setImages(imageUrls);
    }
}
