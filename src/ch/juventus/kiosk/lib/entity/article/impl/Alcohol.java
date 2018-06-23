/**
 * class for alcohol
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.impl;

import ch.juventus.kiosk.lib.entity.article.base.DrinkBase;
import ch.juventus.kiosk.lib.entity.article.type.Category;
import ch.juventus.kiosk.lib.entity.article.type.LiquidUnit;


public class Alcohol extends DrinkBase {

    public Alcohol(String name, double price, double volume, LiquidUnit liquidUnit) {
        super(name, Category.ALCOHOL, price, volume, liquidUnit);
    }

    @Override
    public boolean isApprovedForMinors() {
        return false;
    }
}
