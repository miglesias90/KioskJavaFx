/**
 * base class for drinks
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.base;

import ch.juventus.kiosk.lib.entity.article.type.Category;
import ch.juventus.kiosk.lib.entity.article.type.LiquidUnit;

public abstract class DrinkBase extends ArticleBase{
    private double volume;
    private LiquidUnit liquidUnit;

    protected DrinkBase(String name, Category category, double price, double volume, LiquidUnit liquidUnit) {
        super(name, category, price);
        this.volume = volume;
        this.liquidUnit = liquidUnit;
    }

    public double getVolume() {
        return volume;
    }

    public LiquidUnit getLiquidUnit() {
        return liquidUnit;
    }
}
