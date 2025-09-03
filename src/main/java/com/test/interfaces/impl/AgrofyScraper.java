package com.test.interfaces.impl;

import com.test.domain.MachineEntity;
import com.test.interfaces.Machine;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AgrofyScraper extends AbstractScraper {

    private static final Logger logger = LoggerFactory.getLogger(AgrofyScraper.class);

    private static final String XPATH_PRICE = "//*[@id=\"ProductInfo\"]/div[2]/span";
    private static final String XPATH_CONTRACT_TYPE = "//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[1]/span/ul/li";
    private static final String XPATH_CITY = "//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[4]/span/ul/li";
    private static final String XPATH_MARK = "//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[6]/span/ul/li";
    private static final String XPATH_MODEL = "//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[7]/span/ul/li";
    private static final String XPATH_YEAR_MODEL = "//*[@id=\"pdp-body\"]/div[3]/div/div[2]/section[2]/span/span[1]/div[2]/div/ul/li[9]/span/ul/li";
    private static final String XPATH_WORKER_HOURS = "//*[@id='Descrição']";
    private static final String XPATH_CAROUSEL = "//*[@id=\"ProductCarousel\"]/div";
    private static final String XPATH_IMAGE_CONTAINER = "//*[@id=\"pdp-body\"]/div[3]/div[1]/div/div/div/div[2]/div[1]/div";

    public AgrofyScraper(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getDomain() {
        return "agrofy";
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        MachineEntity machinery = new MachineEntity();

        machinery.setPrice(getText(wait, XPATH_PRICE));
        machinery.setContractType(getText(wait, XPATH_CONTRACT_TYPE));
        machinery.setCity(getText(wait, XPATH_CITY));
        machinery.setMark(getText(wait, XPATH_MARK));
        machinery.setModel(getText(wait, XPATH_MODEL));
        machinery.setYearModel(getText(wait, XPATH_YEAR_MODEL));

        String workerHoursText = getText(wait, XPATH_WORKER_HOURS);
        machinery.setWorkedHours(hoursFilter(workerHoursText));

        driver.findElement(By.xpath(XPATH_CAROUSEL)).click();
        WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_IMAGE_CONTAINER)));
        captureImages(machinery, container);

        return machinery;
    }

    private String getText(WebDriverWait wait, String xpath) {

        try {

            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
        } catch (Exception e) {
            logger.warn("Failed to get text for XPath: {}", xpath);
            return null;
        }
    }

    private void captureImages(MachineEntity machinery, WebElement container) {

        List<String> imageUrls = new ArrayList<>();
        List<WebElement> slides = container.findElements(By.cssSelector(".slide img"));

        for (WebElement img : slides) {
            String src = img.getAttribute("src");
            if (src != null && !src.isEmpty()) {
                imageUrls.add(src);
            }
        }
        machinery.setImages(imageUrls);
    }

    private String hoursFilter(String workerHours) {

        Pattern patternHoras = Pattern.compile("Horas:\\s*([\\d\\.]+)");
        Matcher matcherHoras = patternHoras.matcher(workerHours);
        return matcherHoras.find() ? matcherHoras.group(1) : null;
    }
}
