/**
 * class for snack
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.type.Category;
import ch.juventus.kiosk.lib.entity.article.type.SnackType;

public class Snack extends ArticleBase {
    private SnackType snackType;

    public Snack(String name, double price, SnackType snackType) {
        super(name, Category.SNACK, price);
        this.snackType = snackType;
    }

    public SnackType getSnackType() {
        return snackType;
    }
}
