package com.test.interfaces;

public interface SiteScraper {

    Machine scrape(String url);

    String getDomain();
}
