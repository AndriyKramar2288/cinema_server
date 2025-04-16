package com.banew.cinema_server.backend.services;

import java.time.Duration;
import java.util.HashSet;
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

import com.banew.cinema_server.backend.entities.Actor;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Rate;

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
            
            searchInput.sendKeys("Нікчемний я");
            searchInput.submit();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie-item")));

            html = Jsoup.parse(driver.getPageSource());
        } catch (Exception e) { }
        finally {
            driver.quit();
        }
        return html;
    }

    private Film generateFilmByFilmPage(Document filmPage) {
        if (filmPage == null) {
            return null;
        }
        
        Film film = new Film();
        
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
            
            // No english name directly visible in the provided HTML
            // Could be implemented if available elsewhere on the page
            
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
            Set<String> countries = new HashSet<>();
            Elements countryElements = filmPage.select("div.fi-item:contains(Країна) div.fi-desc a");
            for (Element country : countryElements) {
                countries.add(country.text());
            }
            film.setCountries(countries);
            
            // Extract genres
            Set<String> genres = new HashSet<>();
            Elements genreElements = filmPage.select("div.fi-item:contains(Жанр) div.fi-desc a");
            for (Element genre : genreElements) {
                genres.add(genre.text());
            }
            film.setGenres(genres);
            
            // Extract director
            String director = filmPage.select("div.fi-item:contains(Режисер) div.fi-desc").text();
            film.setDirector(director);
            
            // Extract poster URL
            String posterUrl = filmPage.select("div.film-poster a img").attr("src");
            film.setSrc_poster(posterUrl);
            
            // Extract photo URLs (if available)
            Set<String> photoUrls = new HashSet<>();
            Elements photoElements = filmPage.select("div.film-gallery a");
            for (Element photo : photoElements) {
                photoUrls.add(photo.attr("href"));
            }
            film.setSrc_photos(photoUrls);
            
            // Extract actors
            Set<Actor> actors = new HashSet<>();
            Elements actorElements = filmPage.select("div.fi-item:contains(Актори) div.fi-desc a");
            for (Element actorElement : actorElements) {
                Actor actor = Actor.builder()
                        .fullname(actorElement.text())
                        .build();
                actors.add(actor);
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
            
            // Extract ratings
            Set<Rate> ratings = new HashSet<>();
            
            // IMDb rating
            Element imdbElement = filmPage.select("div.fi-item:contains(imdb) div.fi-desc").first();
            if (imdbElement != null) {
                String imdbRating = imdbElement.text();
                if (!imdbRating.isEmpty()) {
                    String[] ratingParts = imdbRating.split("/");
                    if (ratingParts.length > 0) {
                        Rate imdb = Rate.builder()
                                .name("IMDb")
                                .rate(ratingParts[0])
                                .build();
                        ratings.add(imdb);
                    }
                }
            }
            
            film.setRating(ratings);
            
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

    public Set<Film> findFilmsByUAKino(String request) {
        Set<Film> result = new HashSet<>();

        Document siteWithFilmList = parseDynamicUAKinoSearch(request);

        if (siteWithFilmList == null) {
            System.out.println("ABOBAAAAAAAAAA");
            return Set.of();
        }

        Elements films_cards = siteWithFilmList.getElementsByClass("movie-item");
        films_cards.forEach(card -> {
            try {
                Element link = card.getElementsByTag("a").first();
                Document filmPage = Jsoup.connect(link.attr("href")).get();
                
                if (filmPage.getElementsByClass("solototle").size() != 1) {
                    return;
                }

                result.add(generateFilmByFilmPage(filmPage));
            } catch (Exception e) { }
        });

        return result;
    }
}
