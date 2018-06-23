/**
 * class for softdrink
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.type.Category;

public class Softdrink extends ArticleBase {

    public Softdrink(String name, double price) {
        super(name, Category.SOFTDRINK, price);
    }
}
