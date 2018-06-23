/**
 * base class for article
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.base;

import ch.juventus.kiosk.lib.entity.article.type.Category;

public abstract class ArticleBase {
    private String name;
    private Category category;
    private double price;

    protected ArticleBase(String name, Category category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryText() {
        return category.getText();
    }

    public double getPrice() {
        return price;
    }

    /**
     * Checks if article is allowed for underaged customer
     *
     * @return true if is allowed for underaged customer
     */
    protected boolean isApprovedForMinors() {
        return true;
    }
}
