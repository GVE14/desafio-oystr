package com.test.interfaces.impl;

import com.test.interfaces.Bot;
import com.test.interfaces.Machine;
import com.test.interfaces.SiteScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BotImpl implements Bot {

    private final Map<String, SiteScraper> scrapers;

    @Autowired
    public BotImpl(List<SiteScraper> scrapersList) {
        this.scrapers = scrapersList.stream()
                .collect(Collectors.toMap(SiteScraper::getDomain, s -> s));
    }

    @Override
    public Machine fetch(String url) {

        return scrapers.entrySet().stream()
                .filter(entry -> url.contains(entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("does not contain implementation for scraping this URL: " + url))
                .getValue()
                .scrape(url);
    }

}
