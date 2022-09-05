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
            int x = 1;

            Discount discount1 = null;
            int x1;
            if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                x1 = 3;

                int numberOfXs = quantityAsInt / x1;
                if (quantityAsInt > 2) {
                    double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount1 = new Discount(p, "3 for 2", -discountAmount);
                }
            } else {
                x1 = x;
                Discount discount11 = null;
                int x11 = x1;

                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                        x11 = 2;
                        double total = offer.argument * (quantityAsInt / x11) + quantityAsInt % 2 * unitPrice;
                        double discountN = unitPrice * quantity - total;
                        discount11 = new Discount(p, "2 for " + offer.argument, -discountN);
                    int numberOfXs = quantityAsInt / x11;
                } else {

                    if (offer.offerType == SpecialOfferType.FiveForAmount) {

                        Discount discount111 = discount11;
                        int x111 = x11;
                        x111 = 5;
                        int numberOfXs = quantityAsInt / x111;
                        if (quantityAsInt >= 5) {
                            double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                            discount111 = new Discount(p, x111 + " for " + offer.argument, -discountTotal);
                        }
                        discount11 = discount111;
                    } else {
                        Discount discount111 = discount11;
                        int x111 = x11;
                        int numberOfXs = quantityAsInt / x111;

                        if (offer.offerType == SpecialOfferType.TenPercentDiscount) {

                            discount111 = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                        }

                        if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                            double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                            discount111 = new Discount(p, x111 + " for " + offer.argument, -discountTotal);
                        }
                        discount11 = discount111;
                    }
                }
                discount1 = discount11;
            }
            if (discount1 != null)
                receipt.addDiscount(discount1);
        }
    }

}
