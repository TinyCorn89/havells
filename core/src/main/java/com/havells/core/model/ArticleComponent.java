package com.havells.core.model;

public class ArticleComponent {
    private String parentPagePath;
    private String authorName;
    private String articleTitle;
    private String date;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getParentPagePath() {
        return parentPagePath;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    public void setParentPagePath(String parentPagePath) {
        this.parentPagePath = parentPagePath;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
