/**
 * class implementation for command handler
 *
 * @author Miguel Iglesias
 */


package ch.juventus.kiosk.logic.impl;

import ch.juventus.kiosk.data.impl.DataStore;
import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.impl.*;
import ch.juventus.kiosk.lib.entity.article.type.LiquidUnit;
import ch.juventus.kiosk.lib.entity.article.type.MagazineType;
import ch.juventus.kiosk.lib.entity.article.type.SnackType;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Supplier;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;
import ch.juventus.kiosk.lib.exception.NoMoneyException;
import ch.juventus.kiosk.lib.exception.UnderAgeException;
import ch.juventus.kiosk.lib.util.Util;
import ch.juventus.kiosk.logic.base.ICommandHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandHandler implements ICommandHandler {
    private static CommandHandler instance;

    public static CommandHandler getInstance() {
        if(instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    public CommandHandler() {
    }


    @Override
    public Set<Kiosk> getKiosks() {
        return DataStore.getInstance().getKioskList();
    }

    @Override
    public Set<Kiosk> getOpenKiosks() {
        Set<Kiosk> kiosks = new HashSet<>();

        for (Kiosk kiosk : DataStore.getInstance().getKioskList()) {
            if(kiosk.getState() == KioskState.OPEN)
                kiosks.add(kiosk);
        }

        return kiosks;
    }

    @Override
    public Kiosk getKioskByName(String name) {
        for (Kiosk kiosk : DataStore.getInstance().getKioskList()) {
            if(kiosk.getName() == name)
                return kiosk;
        }

        return null;
    }

    @Override
    public void addKisok(String name, String location, Double cash, Set<String> employees) throws Exception {
        if (Util.stringIsEmptyOrNull(name)) {
           throw new Exception("Name is null or empty");
        }

        if (Util.stringIsEmptyOrNull(location)) {
            throw new Exception("Location is null or empty");
        }

        if (cash == null) {
            throw new Exception("Cash is null or empty");
        }

        if(employees == null || employees.isEmpty()) {
            throw new Exception("Employees is null or empty");
        }


        Supplier supplier = new Supplier(name + " Lieferant", getSupplierArticles());
        DataStore.getInstance().addKiosk(name, location, KioskState.CLOSED, cash, employees, getInitStorage(), supplier);
    }

    @Override
    public void changeKioskState(Kiosk kiosk) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        DataStore.getInstance().changeKioskState(kiosk);
    }


    @Override
    public Set<ArticleBase> getArticles(Kiosk kiosk) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        return DataStore.getInstance().getArticles(kiosk);
    }

    @Override
    public int getArticleAmount(Kiosk kiosk, ArticleBase article) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        if (article == null) {
            throw new Exception("Article is null");
        }

        return DataStore.getInstance().getArticleAmount(kiosk, article);
    }


    @Override
    public boolean allowedToBuy(ArticleBase article, int customerAge) throws Exception {
        if (article == null) {
            throw new Exception("Article is null");
        }

        if(customerAge < 18) {
            switch (article.getCategory()) {
                case ALCOHOL:
                    if(!((Alcohol) article).isApprovedForMinors()) {
                        throw new UnderAgeException(customerAge);
                    }
                    break;
                case TOBACCO:
                    if(!((Tobacco) article).isApprovedForMinors()) {
                        throw new UnderAgeException(customerAge);
                    }
                    break;
            }
        }


        return true;
    }

    @Override
    public void buyArticles(String name, Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        if (articles == null || articles.isEmpty()) {
            throw new Exception("Articles null or empty");
        }

        if (amount <= 0) {
            throw new Exception("Amount equal or under zero");
        }

        DataStore.getInstance().buyArticles(name, kiosk, articles, amount);

    }

    @Override
    public Set<ArticleBase> getSupplierArticles(Kiosk kiosk) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }


        return DataStore.getInstance().getSupplierArticles(kiosk);
    }

    @Override
    public void buyArticlesFromSupplier(Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        if (articles == null || articles.isEmpty()) {
            throw new Exception("Articles null or empty");
        }

        if (amount <= 0) {
            throw new Exception("Amount equal or under zero");
        }

        if(amount > kiosk.getCash()) {
            throw new NoMoneyException(kiosk.getCash(), amount);
        }

        DataStore.getInstance().buyArticlesFromSupplier(kiosk, articles, amount);
    }

    @Override
    public void generateInventory(Kiosk kiosk) throws Exception {
        if (kiosk == null) {
            throw new Exception("Kiosk is null");
        }

        if (!DataStore.getInstance().kioskExists(kiosk)) {
            throw new Exception("Kiosk does not exist");
        }

        DataStore.getInstance().generateInventory(kiosk);
    }

    private Map<ArticleBase, Integer> getInitStorage() {
        ArticleBase article1 = new Alcohol("Bier", 2.50, 0.5, LiquidUnit.LITER);
        ArticleBase article2 = new Magazine("CT", 10.0, MagazineType.IT);
        ArticleBase article3 = new Snack("Pringels", 2.30, SnackType.SOUR);
        ArticleBase article4 = new Softdrink("Coca Cola", 1.80);
        ArticleBase article5 = new Tobacco("Marlboro Rot", 8.80);

        Map<ArticleBase, Integer> articles = new HashMap<>();
        articles.put(article1, 100);
        articles.put(article2, 100);
        articles.put(article3, 100);
        articles.put(article4, 100);
        articles.put(article5, 100);

        return articles;
    }


    private Set<ArticleBase> getSupplierArticles() {
        ArticleBase article1 = new Alcohol("Bier", 2.50, 0.5, LiquidUnit.LITER);
        ArticleBase article2 = new Magazine("CT", 10.0, MagazineType.IT);
        ArticleBase article3 = new Snack("Pringels", 2.30, SnackType.SOUR);
        ArticleBase article4 = new Softdrink("Coca Cola", 1.80);
        ArticleBase article5 = new Tobacco("Marlboro Rot", 8.80);

        Set<ArticleBase> articles = new HashSet<>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        articles.add(article4);
        articles.add(article5);

        return articles;
    }
}
