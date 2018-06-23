/**
 * enum for article categories
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.article.type;

public enum Category {
    ALCOHOL("Alkohol"),
    MAGAZINE("Magazin"),
    SNACK("Snack"),
    SOFTDRINK("Softdrink"),
    TOBACCO("Tabak");

    private String text;

    Category(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Category findByText(String text) {
        for (Category category : Category.values()) {
            if (category.getText().equals(text)) {
                return category;
            }
        }

        return null;
    }
}
