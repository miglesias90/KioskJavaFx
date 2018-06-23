/**
 * class for tobacco
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.type.Category;

public class Tobacco extends ArticleBase {

    public Tobacco(String name, double price) {
        super(name, Category.TOBACCO, price);
    }

    @Override
    public boolean isApprovedForMinors() {
        return false;
    }
}
