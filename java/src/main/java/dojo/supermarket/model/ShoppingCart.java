package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            doHandleOffers(receipt, offers, catalog, p, productQuantities.get(p));

        }
    }

    private void doHandleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog, Product p, double quantity) {
        if (offers.containsKey(p)) {
            Offer offer = offers.get(p);
            double unitPrice = catalog.getUnitPrice(p);
            int quantityAsInt = (int) quantity;
            Discount discount = null;

            switch (offer.offerType) {
                case ThreeForTwo:
                    if (quantityAsInt > 2) {
                        discount = new Discount(p, "3 for 2", -(quantity * unitPrice - (((quantityAsInt / 3) * 2 * unitPrice) + quantityAsInt % 3 * unitPrice)));
                    }
                    break;
                case TwoForAmount:
                    discount = new Discount(p, "2 for " + offer.argument, -(unitPrice * quantity - (offer.argument * (quantityAsInt / 2) + quantityAsInt % 2 * unitPrice)));
                    break;
                case FiveForAmount:
                    if (quantityAsInt >= 5) {
                        discount = new Discount(p, 5 + " for " + offer.argument, -(unitPrice * quantity - (offer.argument * (quantityAsInt / 5) + quantityAsInt % 5 * unitPrice)));
                    }
                    break;
                case TenPercentDiscount:
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                    break;
            }
            if (discount != null)
                receipt.addDiscount(discount);
        }
    }

}
