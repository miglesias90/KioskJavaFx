/**
 * thread class for client simulation
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.presentation.client.thread;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.logic.impl.CommandHandler;

import java.util.HashMap;

import java.util.Map;
import java.util.Set;

public class ClientRunnable implements Runnable {
    String name;
    Kiosk kiosk;

    public ClientRunnable(String name, Kiosk kiosk) {
        this.name = name;
        this.kiosk = kiosk;
    }

    @Override
    public void run() {
        try {
            Set<ArticleBase> articleBases = CommandHandler.getInstance().getArticles(kiosk);
            Map<ArticleBase, Integer> shoppingCard = new HashMap<>();

            Thread.sleep(2000);


            double amount = 0;
            for (ArticleBase articleBase : articleBases) {
                shoppingCard.put(articleBase, 1);
                amount += articleBase.getPrice();
            }

            Thread.sleep(5000);
            CommandHandler.getInstance().buyArticles(name, kiosk, shoppingCard, amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
