package ru.otus.hw.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.Product;
import ru.otus.hw.service.ProductGeneratorService;

import java.util.List;

/**
 * Компонент для периодической генерации и отправки продуктов.
 */
@RequiredArgsConstructor
@Slf4j
@Component
/*для возможности отключения планировщика для тестов*/
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class ProductScheduler {

    private final ProductGeneratorService generatorService;

    private final MessageChannel inputChannel;

    @Value("${product.batch.size:5}")
    private int batchSize;

    /**
     * Метод периодически генерирует продукты и отправляет их во входной канал.
     */
    @Scheduled(fixedRate = 5000) // Запуск каждые 5 секунд
    public void generateAndSendProducts() {
        List<Product> products = generatorService.generateProducts(batchSize);
        log.info("Generated products {} ", products);

        // inputChannel
        inputChannel.send(MessageBuilder.withPayload(products).build());
    }
}
