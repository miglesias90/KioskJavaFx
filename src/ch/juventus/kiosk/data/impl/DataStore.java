/**
 * class for datastore
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.data.impl;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.article.impl.*;
import ch.juventus.kiosk.lib.entity.article.type.LiquidUnit;
import ch.juventus.kiosk.lib.entity.article.type.MagazineType;
import ch.juventus.kiosk.lib.entity.article.type.SnackType;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.data.base.IDataStore;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Supplier;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataStore implements IDataStore {
    private static DataStore instance;
    private Map<Kiosk, Map<ArticleBase, Integer>> kioskList;


    /**
     * Return actual instance
     *
     * @return instance of DataStore
     */
    public static DataStore getInstance() {
        if(instance == null) {
            instance = new DataStore();
        }
        return instance;
    }


    private DataStore() {
        kioskList = new HashMap<>();

        initData();
    }

    @Override
    public Set<Kiosk> getKioskList() {
        return kioskList.keySet();
    }

    @Override
    public boolean kioskExists(Kiosk kiosk) {
        return kioskList.containsKey(kiosk);
    }

    @Override
    public void addKiosk(String name, String location, KioskState state, Double cash, Set<String> employees, Map<ArticleBase, Integer> storage, Supplier supplier) {
        kioskList.put(new Kiosk(name, location, state, cash, employees, supplier), storage);
    }

    @Override
    public void changeKioskState(Kiosk kiosk) {
        synchronized (kioskList) {
            if (kioskList.containsKey(kiosk)) {
                kiosk.changeState();
                Map<ArticleBase, Integer> storage = kioskList.get(kiosk);
                kioskList.put(kiosk, storage);
            }
        }
    }

    @Override
    public Set<ArticleBase> getArticles(Kiosk kiosk) {
        Set<ArticleBase> articles = new HashSet<>();

        for(ArticleBase articleBase : kioskList.get(kiosk).keySet()) {
            if(kioskList.get(kiosk).get(articleBase) > 0) {
                articles.add(articleBase);
            }
        }

        return articles;
    }

    @Override
    public int getArticleAmount(Kiosk kiosk, ArticleBase article) {
        if (kioskList.get(kiosk).keySet().contains(article))
            return kioskList.get(kiosk).get(article);

        return 0;
    }

    @Override
    public void buyArticles(String name, Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) {
       synchronized (kioskList) {
           System.out.println(name + " an Kasse");

           Map<ArticleBase, Integer> storage = kioskList.get(kiosk);

           for (ArticleBase articleBase : articles.keySet()) {
               int restAmount = articles.get(articleBase);

               int actualAmount = storage.get(articleBase);

               storage.put(articleBase, actualAmount - restAmount);
           }

           kiosk.addCash(amount);

           kioskList.put(kiosk, storage);
           System.out.println(name + " hat bezahlt");
       }
    }

    @Override
    public Set<ArticleBase> getSupplierArticles(Kiosk kiosk) {
       synchronized (kioskList) {
           Set<ArticleBase> articles = new HashSet<>();

           if (kioskList.keySet().contains(kiosk)) {
               articles.addAll(kiosk.getSupplier().getArticles());
           }

           return articles;
       }
    }

    @Override
    public void buyArticlesFromSupplier(Kiosk kiosk, Map<ArticleBase, Integer> articles, double amount) {
        synchronized (kioskList) {
            Map<ArticleBase, Integer> storage = kioskList.get(kiosk);

            for (ArticleBase articleBase : articles.keySet()) {
                int restAmount = articles.get(articleBase);

                int actualAmount = storage.get(articleBase);

                storage.put(articleBase, actualAmount + restAmount);
            }

            kiosk.restCash(amount);

            kioskList.put(kiosk, storage);
        }
    }

    @Override
    public void generateInventory(Kiosk kiosk) throws IOException {
        FileOutputStream fileOut = null;

        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Artikel");

            int rownum = 1;
            for (ArticleBase articleBase : kioskList.get(kiosk).keySet()) {
                XSSFRow row = sheet.createRow(rownum);
                XSSFCell cellName = row.createCell(0);
                XSSFCell cellPrice = row.createCell(1);
                XSSFCell cellAmount = row.createCell(2);

                cellName.setCellValue(articleBase.getName());
                cellPrice.setCellValue(articleBase.getPrice());
                cellAmount.setCellValue(kioskList.get(kiosk).get(articleBase));

                rownum++;
            }

            fileOut = new FileOutputStream("C://kiosk//test.xlsx");
            wb.write(fileOut);

        } catch (IOException e) {
            throw e;
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }


    /**
     * Init kiosk dat for simulation
     */
    private void initData() {

        Set<String> employees = new HashSet<>();
        employees.add("Miguel Igesias");
        employees.add("Marc Pfändler");
        employees.add("Alexandra Hauri");

        ArticleBase article1 = new Alcohol("Bier", 2.50, 0.5, LiquidUnit.LITER);
        ArticleBase article2 = new Magazine("CT", 10.0, MagazineType.IT);
        ArticleBase article3 = new Snack("Pringels", 2.30, SnackType.SOUR);
        ArticleBase article4 = new Softdrink("Coca Cola", 1.80);
        ArticleBase article5 = new Tobacco("Marlboro Rot", 8.80);

        Map<ArticleBase, Integer> articles= new HashMap<>();
        articles.put(article1, 100);
        articles.put(article2, 100);
        articles.put(article3, 100);
        articles.put(article4, 100);
        articles.put(article5, 100);

        addKiosk("Bhf Altstetteb", "Altstetten", KioskState.CLOSED, 2000.00, employees, articles, new Supplier("Bhf Altstetten Lieferant", articles.keySet()));
        addKiosk("HB Zürich", "Zürich", KioskState.CLOSED, 2000.00, employees, articles, new Supplier("Bhf Zürich Lieferant", articles.keySet()));
        addKiosk("Bhf Schlieren", "Dietikon", KioskState.CLOSED, 2000.00, employees, articles, new Supplier("Bhf Schlieren Lieferant", articles.keySet()));
    }
}
