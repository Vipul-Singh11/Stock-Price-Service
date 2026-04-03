package com.stock.stockpriceservice.scheduler;

import com.stock.stockpriceservice.entity.Stock;
import com.stock.stockpriceservice.repository.StockRepository;
import com.stock.stockpriceservice.service.StockService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PriceSimulationScheduler {

    private final StockRepository stockRepository;
    private final StringRedisTemplate redisTemplate;
    private final StockService stockService;
    private final Random random = new Random();

    /**
     * Runs every 5 seconds
     * Simulates price fluctuation between -2% and +2%
     */
    @Scheduled(fixedRate = 5000)
    public void simulatePrices() {

        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {

            BigDecimal currentPrice = stock.getCurrentPrice();

            double percentageChange = (random.nextDouble() - 0.5) * 0.04;

            BigDecimal newPrice = currentPrice.add(
                    currentPrice.multiply(BigDecimal.valueOf(percentageChange))
            ).setScale(2, RoundingMode.HALF_UP);

            stock.setCurrentPrice(newPrice);

            // Update Redis
            redisTemplate.opsForValue().set(
                    "stock:" + stock.getSymbol(),
                    newPrice.toString()
            );

            // Persist to DB
            stockService.updateStock(stock);
        }
    }
}