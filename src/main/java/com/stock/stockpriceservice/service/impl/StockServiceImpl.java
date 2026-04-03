package com.stock.stockpriceservice.service.impl;

import com.stock.stockpriceservice.entity.Stock;
import com.stock.stockpriceservice.exception.ResourceNotFoundException;
import com.stock.stockpriceservice.repository.StockRepository;
import com.stock.stockpriceservice.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll(); // we’ll optimize later
    }

    @Override
    public Stock getStockBySymbol(String symbol) {

        String redisKey = "stock:" + symbol;

        try {
            // 1️⃣ Check Redis
            String cachedJson = redisTemplate.opsForValue().get(redisKey);

            if (cachedJson != null) {
                try {
                    return objectMapper.readValue(cachedJson, Stock.class);
                } catch (Exception e) {
                    // ❗ Handle corrupted/old data in Redis
                    redisTemplate.delete(redisKey);
                }
            }

            // 2️⃣ Fetch from DB
            Stock stock = stockRepository.findBySymbol(symbol)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Stock not found with symbol: " + symbol));

            // 3️⃣ Store in Redis
            redisTemplate.opsForValue().set(
                    redisKey,
                    objectMapper.writeValueAsString(stock)
            );

            return stock;

        } catch (Exception e) {
            throw new RuntimeException("Error handling Redis", e);
        }
    }

    /**
     * Updates stock in Redis + DB
     */
    @Override
    @Transactional
    public void updateStock(Stock stock) {

        try {
            // ❗ IMPORTANT: Save to DB FIRST
            Stock savedStock = stockRepository.save(stock);

            // Then update Redis with latest DB state
            redisTemplate.opsForValue().set(
                    "stock:" + savedStock.getSymbol(),
                    objectMapper.writeValueAsString(savedStock)
            );

            // Publish event
            redisTemplate.convertAndSend(
                    "stock-price-updates",
                    savedStock.getSymbol() + ":" + savedStock.getCurrentPrice()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error updating Redis", e);
        }
    }
}