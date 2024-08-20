package edu.kit.kastel.sdq.newslist.service;

/**
 * @author Lucas Alber
 */
public class NewsItem {
    private final String title;
    private final String content;

    /**
     *
     * @param title the  title of the news item
     * @param content the content of the news item
     */
    public NewsItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * Returns the title of the news item
     * @return the title of the news item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the content of the news item
     * @return the content of the news item
     */
    public String getContent() {
        return content;
    }
}
