package com.test.interfaces.impl;

import com.test.interfaces.Machine;
import com.test.interfaces.SiteScraper;
import org.openqa.selenium.WebDriver;

public abstract class AbstractScraper implements SiteScraper {

    protected final WebDriver driver;

    public AbstractScraper(WebDriver driver) {
        this.driver = driver;
    }

    protected void loadPage(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar página: " + url, e);
        }
    }

    public abstract String getDomain();
    public abstract Machine scrape(String url);
}