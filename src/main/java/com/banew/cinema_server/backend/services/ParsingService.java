package com.banew.cinema_server.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.FilmSimpleInfoDto;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class ParsingService {
    private Document parseDynamicUAKinoSearch(String request) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        Document html = null;

        try {
            driver.get("https://uakino.me/index.php?do=search");
            
            WebElement searchInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("searchinput"))
            );
            
            searchInput.sendKeys(request);
            searchInput.submit();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie-item")));

            html = Jsoup.parse(driver.getPageSource());
        } catch (Exception e) { }
        finally {
            driver.quit();
        }
        return html;
    }

    private FilmSimpleInfoDto generateFilmInfoByFilmPage(Document filmPage) {
        if (filmPage == null) {
            return null;
        }
        
        FilmSimpleInfoDto film = new FilmSimpleInfoDto();
        
        try {
            // Extract film title (ukrainian name)
            // Usually the title is in <h1> tag with itemprop="name"
            String ukName = filmPage.select("h1[itemprop=name]").text();
            if (ukName.isEmpty()) {
                // Fallback options if the title is not found in the expected place
                ukName = filmPage.select("div.full-title h1").text();
                if (ukName.isEmpty()) {
                    ukName = filmPage.select("h1").first() != null ? filmPage.select("h1").first().text() : "";
                }
            }
            film.setUk_name(ukName);
            
            Element enNameSpan = filmPage.select("span[itemprop=alternateName]").first();
            if (enNameSpan != null) {
                Element enNameSpanA = enNameSpan.firstElementChild();
                if (enNameSpanA != null) {
                    film.setEn_name(enNameSpan.text());
                }
            }

            List<String> photos = new ArrayList<>();
            Element photosContainer = filmPage.getElementsByClass("screens-section").first();
            if (photosContainer != null) {
                photosContainer.children().forEach(child -> {
                    photos.add(child.attr("abs:href"));
                });
            }
            film.setSrc_photos(photos);

            // Extract release year
            String yearText = filmPage.select("div.fi-item:contains(Рік виходу) div.fi-desc").text();
            if (!yearText.isEmpty()) {
                try {
                    film.setRelease_year(Long.parseLong(yearText.trim()));
                } catch (NumberFormatException e) {
                    // Handle parsing exception
                }
            }
            
            // Extract countries
            List<String> countries = new ArrayList<>();
            Elements countryElements = filmPage.select("div.fi-item:contains(Країна) div.fi-desc a");
            for (Element country : countryElements) {
                countries.add(country.text());
            }
            film.setCountries(countries);
            
            // Extract genres
            List<String> genres = new ArrayList<>();
            Elements genreElements = filmPage.select("div.fi-item:contains(Жанр) div.fi-desc a");
            for (Element genre : genreElements) {
                genres.add(genre.text());
            }
            film.setGenres(genres);
            
            // Extract director
            String director = filmPage.select("div.fi-item:contains(Режисер) div.fi-desc").text();
            film.setDirector(director);
            
            // Extract poster URL
            String posterUrl = filmPage.select("div.film-poster a img").attr("abs:src");
            film.setSrc_poster(posterUrl);
            
            // Extract actors
            List<String> actors = new ArrayList<>();
            Elements actorElements = filmPage.select("div.fi-item:contains(Актори) div.fi-desc a");
            for (Element actorElement : actorElements) {
                actors.add(actorElement.text());
            }
            film.setActors(actors);
            
            // Extract duration
            String duration = filmPage.select("div.fi-item:contains(Тривалість) div.fi-desc").text();
            film.setDuration(duration);
            
            // Extract voice acting
            String voiceActing = filmPage.select("div.fi-item:contains(Мова озвучення) div.fi-desc").text();
            film.setVoice_acting(voiceActing);
            
            // Extract age limit
            String ageLimit = filmPage.select("div.fi-item:contains(Вік. рейтинг) div.fi-desc").text();
            film.setAge_limit(ageLimit);
            

            // IMDB
            Element imdbBlock = filmPage.getElementsByAttributeValue("src", "/templates/uakino/images/imdb-mini.png").first();
            if (imdbBlock != null) {
                film.setImdb(imdbBlock.parent().parent().lastElementChild().text());
            }
            
            // Extract film description/about
            String about = filmPage.select("div.full-text[itemprop=description]").text();
            if (about.isEmpty()) {
                about = filmPage.select("div.full-text").text();
            }
            film.setAbout(about);
            
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return film;
    }

    public Set<FilmSimpleInfoDto> findFilmsByUAKino(String request) throws BadRequestSendedException {
        Set<FilmSimpleInfoDto> result = new HashSet<>();

        Document siteWithFilmList = parseDynamicUAKinoSearch(request);

        if (siteWithFilmList == null) {
            throw new BadRequestSendedException("Помилка парсингу!");
        }

        Elements films_cards = siteWithFilmList.getElementsByClass("movie-item");
        for (Element card : films_cards) {
            try {
                Element link = card.getElementsByTag("a").first();
                Document filmPage = Jsoup.connect(link.attr("href")).get();
                
                if (filmPage.getElementsByClass("solototle").size() != 1) {
                    continue;
                }
                result.add(generateFilmInfoByFilmPage(filmPage));

            } catch (IOException e) {
                throw new BadRequestSendedException("Помилка парсингу!");
            }
        }

        return result;
    }
}
