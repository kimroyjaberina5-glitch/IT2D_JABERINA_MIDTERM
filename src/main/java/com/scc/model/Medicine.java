
package com.scc.model;

public record Medicine(
        int id,
        String brandName,
        String category,
        double price,
        int stock
) {
    public Medicine(String brandName, String category,
                    double price, int stock) {
        this(0, brandName, category, price, stock);
    }
}
