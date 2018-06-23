/**
 * class for magazine
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.type.Category;
import ch.juventus.kiosk.lib.entity.article.type.MagazineType;

public class Magazine extends ArticleBase {
    private MagazineType magazineType;

    public Magazine(String name, double price, MagazineType magazineType) {
        super(name, Category.MAGAZINE, price);
        this.magazineType = magazineType;
    }

    public MagazineType getMagazineType() {
        return magazineType;
    }
}
