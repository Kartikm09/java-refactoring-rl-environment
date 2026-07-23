package dev.kartikm09.orders.application;

public interface InventoryRepository {
    void reserve(String sku, int quantity);
    void release(String sku, int quantity);
    int available(String sku);
}
