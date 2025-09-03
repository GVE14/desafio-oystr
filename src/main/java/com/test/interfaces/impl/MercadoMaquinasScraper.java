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
public class MercadoMaquinasScraper extends AbstractScraper {

    private static final Logger logger = LoggerFactory.getLogger(MercadoMaquinasScraper.class);

    private static final By XPATH_CONTRACT_TYPE = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[1]/span");
    private static final By XPATH_YEAR_MODEL = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[6]/span[2]");
    private static final By XPATH_MARK = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[4]/span[2]/a");
    private static final By XPATH_MODEL = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/ul/li[5]/span[2]/a");
    private static final By XPATH_WORKER_HOURS = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[3]/div[3]/ul[3]/li/span[2]");
    private static final By XPATH_CITY = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[2]/div[1]/div/div/span[2]");
    private static final By XPATH_PRICE = By.xpath("//*[@id=\"ad-details\"]/div[1]/div[1]/div/div[2]/div/div/span");
    private static final By XPATH_IMAGE_ANCHORS = By.cssSelector("#ad-thumbnails a");

    public MercadoMaquinasScraper(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getDomain() {
        return "mercadomaquinas";
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

        machinery.setContractType(getText(XPATH_CONTRACT_TYPE));
        machinery.setYearModel(getText(XPATH_YEAR_MODEL));
        machinery.setMark(getText(XPATH_MARK));
        machinery.setModel(getText(XPATH_MODEL));
        machinery.setWorkedHours(getText(XPATH_WORKER_HOURS));
        machinery.setCity(getText(XPATH_CITY));
        machinery.setPrice(getText(XPATH_PRICE));

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

    private void captureImages(MachineEntity machinery) {

        List<String> imageUrls = new ArrayList<>();
        List<WebElement> anchors = driver.findElements(XPATH_IMAGE_ANCHORS);

        for (WebElement a : anchors) {
            String href = a.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                imageUrls.add(href);
            }
        }

        machinery.setImages(imageUrls);
    }
}
