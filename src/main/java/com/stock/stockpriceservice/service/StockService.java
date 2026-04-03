package com.stock.stockpriceservice.service;

import com.stock.stockpriceservice.entity.Stock;

import java.util.List;

public interface StockService {

    List<Stock> getAllStocks();

    Stock getStockBySymbol(String symbol);

    void updateStock(Stock stock);
}