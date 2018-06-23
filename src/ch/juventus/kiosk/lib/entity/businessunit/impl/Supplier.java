/**
 * class for supplier
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.businessunit.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;

import java.util.Set;

public class Supplier {
    private String name;
    private Set<ArticleBase> articles;

    public Supplier(String name, Set<ArticleBase> articles) {
        this.name = name;
        this.articles = articles;
    }

    public String getName() {
        return name;
    }

    public Set<ArticleBase> getArticles() {
        return articles;
    }
}
