package ch.juventus.kiosk.presentation.model;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;

public class ArticleAmount {

    private ArticleBase article;
    private int amount;

    public ArticleAmount(ArticleBase article, int amount) {
        this.article = article;
        this.amount = amount;
    }

    public ArticleBase getArticle() {
        return article;
    }

    public String getArticleCategoryText() {
        return article.getCategoryText();
    }

    public String getArticleName() {
        return article.getName();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return amount * article.getPrice();
    }
}
