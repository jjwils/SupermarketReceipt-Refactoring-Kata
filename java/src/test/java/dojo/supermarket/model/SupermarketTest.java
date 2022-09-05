package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupermarketTest {
    public static final Product TOOTHBRUSH = new Product("toothbrush", ProductUnit.Each);
    public static final Product APPLES = new Product("apples", ProductUnit.Kilo);

    // Todo: test all kinds of discounts are applied properly

    @Test
    public void tenPercentDiscount() {

        // ASSERT
//        assertEquals(4.975, receipt.getTotalPrice(), 0.01);
//        assertEquals(Collections.emptyList(), receipt.getDiscounts());
//        assertEquals(1, receipt.getItems().size());
//        ReceiptItem receiptItem = receipt.getItems().get(0);
//        assertEquals(apples, receiptItem.getProduct());
//        assertEquals(price, receiptItem.getPrice());
//        assertEquals(quantity * price, receiptItem.getTotalPrice());
//        assertEquals(quantity, receiptItem.getQuantity());
//
//        String printReceipt = new ReceiptPrinter().printReceipt(receipt);
        //Approvals.verify(getReceipt(APPLES,1.99, 2.5, SpecialOfferType.TenPercentDiscount, TOOTHBRUSH, 10.0));

        CombinationApprovals.verifyAllCombinations((product, price, quantity, specialOfferType, specialOfferProduct, specialOfferArgument, numberOfTimestoAdd) -> getReceipt(product, price, quantity, specialOfferType, specialOfferProduct, specialOfferArgument, numberOfTimestoAdd),
                new Product[]{APPLES},
                new Double[]{1.99},
                new Double[]{2.5, 3.0, 5.0, 1.0},
                new SpecialOfferType[]{SpecialOfferType.TenPercentDiscount, SpecialOfferType.ThreeForTwo, SpecialOfferType.TwoForAmount, SpecialOfferType.FiveForAmount},
                new Product[]{TOOTHBRUSH, APPLES},
                new Double[]{10.0},
                new Integer[]{2, 0});

    }

    private String getReceipt(Product product, double price, double quantity, SpecialOfferType specialOfferType, Product specialOfferProduct, double specialOfferArgument, int numberOfTimestoAdd) {
        SupermarketCatalog catalog = new FakeCatalog();
        catalog.addProduct(product, price);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(specialOfferType, specialOfferProduct, specialOfferArgument);

        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < numberOfTimestoAdd; i++) {
            cart.addItemQuantity(product, quantity);

        }


        // ACT
        String printReceipt = new ReceiptPrinter().printReceipt(teller.checksOutArticlesFrom(cart));
        return printReceipt;
    }


}
