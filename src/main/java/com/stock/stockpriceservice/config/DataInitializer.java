package com.stock.stockpriceservice.config;

import com.stock.stockpriceservice.entity.Stock;
import com.stock.stockpriceservice.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final StockRepository stockRepository;

    @PostConstruct
    public void loadInitialStocks() {

        if (stockRepository.count() == 0) {

            List<Stock> stocks = List.of(
                    new Stock(null, "AAPL", "Apple Inc", BigDecimal.valueOf(180)),
                    new Stock(null, "TSLA", "Tesla Inc", BigDecimal.valueOf(250)),
                    new Stock(null, "GOOGL", "Google Inc", BigDecimal.valueOf(2800)),
                    new Stock(null, "MSFT", "Microsoft Corp", BigDecimal.valueOf(320))
            );

            stockRepository.saveAll(stocks);

            System.out.println("Initial stocks loaded.");
        }
    }
}