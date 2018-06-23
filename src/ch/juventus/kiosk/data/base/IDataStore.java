/**
 * Inerface for datastore
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.data.base;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Supplier;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface IDataStore {

    /**
     * Gets all kiosk from dataStore
     *
     * @return Set with kiosks
     */
    Set<Kiosk> getKioskList();

    /**
     * Profes if kiosk exists
     *
     * @param kiosk kisk to profe
     *
     * @return true if kiosk exists otherwise false
     */
    boolean kioskExists(Kiosk kiosk);

    /**
     * Adds kiosk to datatStore
     *
     * @param name name of kiosk
     * @param location kiosk location
     * @param state kiosk state
     * @param cash cash of kiosk
     * @param employees set wirh kiosk employees
     * @param storage set wirh articles
     * @param supplier supplier
     */
    void addKiosk(String name, String location, KioskState state, Double cash, Set<String> employees, Map<ArticleBase, Integer> storage, Supplier supplier);

    /**
     * Changes kiosk state to the opposite
     *
     * @param kiosk kiosk to be changed
     */
    void changeKioskState(Kiosk kiosk);

    /**
     * Gets alle articles from kiosk stoage
     *
     * @param kiosk kiosk wich storage schould be given
     *
     * @return set with articles where are not 0 in storage
     */
    Set<ArticleBase> getArticles(Kiosk kiosk);

    /**
     * Gets amount of an article
     *
     * @param kiosk kiosk
     * @param article article
     *
     * @return amount of article in storage
     */
    int getArticleAmount(Kiosk kiosk, ArticleBase article);

    /**
     * Buy articles from kiosk
     *
     * @param name name of customer
     * @param kiosk kiosk in which he buys
     * @param articles artcles to buy
     * @param amount amount of articles to buy
     */
    void buyArticles(String name, Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount);

    /**
     * Get articles from supplier
     *
     * @param kiosk kiosk wich ist meant
     *
     * @return Set of Aticles from supplier
     */
    Set<ArticleBase> getSupplierArticles(Kiosk kiosk);

    /**
     * Buy articles from supplier
     *
     * @param kiosk kiosk wich ist meant
     * @param articles articles to buy from supplier
     * @param amount amount of articles to buy
     */
    void buyArticlesFromSupplier(Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount);

    /**
     * Generate excel of kiosk storage
     *
     * @param kiosk which has to be selected for generation
     *
     * @throws IOException If an IO error ocurres
     */
    void generateInventory(Kiosk kiosk) throws IOException;

}
