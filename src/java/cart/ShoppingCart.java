package cart;

import java.util.*;
import database.BookDetails;

public class ShoppingCart {

    private HashMap items = null;

    public ShoppingCart() {
        items = new HashMap();
    }

    public synchronized void add(String woodId, BookDetails plank) {
        if (items.containsKey(woodId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(woodId);
            scitem.incrementQuantity();
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(plank);
            items.put(woodId, newItem);
        }
    }

    public synchronized void remove(String woodId) {
        if (items.containsKey(woodId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(woodId);
            scitem.decrementQuantity();
            if (scitem.getQuantity() <= 0) {
                items.remove(woodId);
            }
        }
    }

    public synchronized List getItems() {
        List results = new ArrayList();
        Iterator i = items.values().iterator();
        while (i.hasNext()) {
            results.add(i.next());
        }
        return (results);
    }

    protected void finalize() throws Throwable {
        items.clear();
    }

    public synchronized int getNumberOfItems() {
        int numberOfItems = 0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            numberOfItems += item.getQuantity();
        }
        return numberOfItems;
    }

    public synchronized double getTotal() {
        double amount = 0.0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            BookDetails bookDetails = (BookDetails) item.getItem();
            amount += (item.getQuantity() * bookDetails.getPrice());
        }
        return roundOff(amount);
    }

    static private double roundOff(double x) {
        return Math.round(x * 100) / 100.0;
    }

    public synchronized void clear() {
        items.clear();
    }
}
