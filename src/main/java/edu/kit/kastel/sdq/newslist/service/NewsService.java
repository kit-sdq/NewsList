package edu.kit.kastel.sdq.newslist.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class parsing the CSV files and fetching news for a SAML2 user.
 */
@Service
public class NewsService {

    @Value("${news.csv-path}")
    private String csvPath;

    @Value("${news.csv-format}")
    private String csvFormat;

    private final Map<String, List<NewsItem>> news;
    private final Log logger = LogFactory.getLog(this.getClass());

    public NewsService() {
        news = new HashMap<>();
    }

    @PostConstruct
    public void parseCSVs() {
        logger.debug("Parsing news items from CSVs");
        logger.debug("CSV path is " + csvPath + " , CSV format is " + csvFormat);

        CSVFormat format = CSVFormat.valueOf(csvFormat);
        List<File> files = null;
        try {
            files = Files.walk(Paths.get(csvPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(e -> e.getName().endsWith(".csv"))
                    .sorted()
                    .collect(Collectors.toList());

        } catch (IOException e) {
            logger.warn("CSVs could not be parsed.", e);
            return;
        }

        for (File file : files) {
            logger.debug("Parsing " + file.getAbsolutePath());
            try {
                for (CSVRecord record : format.parse(new FileReader(file))) {
                    if (record.size() != 2) {
                        logger.warn("Expected exactly two columns, but record " + record + " had " + record.size()  + ". Skipping!");
                        continue;
                    }

                    final String key = record.get(0);
                    if (!news.containsKey(key)) {
                        news.put(key, new LinkedList<>());
                    }


                    String title = file.getName().substring(0, file.getName().length() - 4).replaceAll("[_]", "");
                    String content = record.get(1);
                    news.get(key).add(new NewsItem(title, content));
                }

            } catch (IOException e) {
                logger.warn("File " + file + " could not be read.", e);
            }
        }

    }

    /**
     * Returns a sorted list of news items for this key.
     * @param key the key
     * @return a sorted list of news items for this key.
     */
    public List<NewsItem> getNewsForKey(final String key) {
        return news.getOrDefault(key, new ArrayList<>());
    }

}
