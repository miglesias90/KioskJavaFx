/**
 * interface for command handler
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.logic.base;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;

import java.util.Map;
import java.util.Set;

public interface ICommandHandler {

    /**
     * Gets all kiosk from dataStore
     *
     * @return Set with kiosks
     */
    Set<Kiosk> getKiosks();

    /**
     * Gets all kiosk from dataStore which are opened
     *
     * @return Set with open kiosks
     */
    Set<Kiosk> getOpenKiosks();

    /**
     * Find kiosk by name
     *
     * @param name wildcard for name
     *
     * @return matching kiosk or null if not found
     */
    Kiosk getKioskByName(String name);

    /**
     * Adds kiosk to dataStore
     *
     * @param name name of kiosk
     * @param location kiosk location
     * @param cash cash of kiosk
     * @param employees kiosk employees
     *
     * @throws Exception If parameter are empty
     */
    void addKisok(String name, String location, Double cash, Set<String> employees) throws Exception;

    /**
     * Changes kiosk state to opposite
     *
     * @param kiosk kiosk to change state
     * @throws Exception If parameter are empty
     */
    void changeKioskState(Kiosk kiosk) throws Exception;

    /**
     * Get articles from kiosk
     *
     * @param kiosk selected kiosk
     *
     * @return Set of kiosk articles
     *
     * @throws Exception If parameter is empty
     */
    Set<ArticleBase> getArticles(Kiosk kiosk) throws Exception;

    /**
     * Gets amount of article
     *
     * @param kiosk kiosk
     * @param article searching article
     *
     * @return amount of article
     * @throws Exception If parameter is empty
     */
    int getArticleAmount(Kiosk kiosk, ArticleBase article) throws Exception;

    /**
     * Checks if customer is allowed to buy article
     *
     * @param article article to check
     * @param customerAge age of customer
     *
     * @return true if he is allowed
     *
     * @throws Exception If parameter is empty or customer is not allowed
     */
    boolean allowedToBuy(ArticleBase article, int customerAge) throws Exception;


    /**
     * Buy articles from kiosk
     *
     * @param name name of customer
     * @param kiosk kiosk to buy from
     * @param articles articles to buy
     * @param amount amount of articles cost
     *
     * @throws Exception If parameter is emtpy
     */
    void buyArticles(String name, Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) throws Exception;

    /**
     * Get articles from supplier
     *
     * @param kiosk kiosk of supplier
     *
     * @return articles from supplier
     *
     * @throws Exception If parameter is empty
     */
    Set<ArticleBase> getSupplierArticles(Kiosk kiosk) throws Exception;

    /**
     * Buy article from supplier
     *
     * @param kiosk kiosk of supplier
     * @param articles articles to buy
     * @param amount amount of article cost
     *
     * @throws Exception If Parameter is empty
     */
    void buyArticlesFromSupplier(Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) throws Exception;

    /**
     * Generate excel from kiosk storage
     *
     * @param kiosk kiosk to generate inventory
     *
     * @throws Exception If parameter is empty
     */
    void generateInventory(Kiosk kiosk) throws Exception;


}
