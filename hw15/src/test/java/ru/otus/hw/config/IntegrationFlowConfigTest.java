package ru.otus.hw.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import ru.otus.hw.model.Product;
import ru.otus.hw.model.ProductType;
import ru.otus.hw.service.ProductGeneratorService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IntegrationFlowConfigTest {

    @Autowired
    private ProductGeneratorService generatorService;

    @Autowired
    private MessageChannel inputChannel;

    @Test
    void contextLoads() {
        // Проверяем, что контекст загружается корректно
        assertNotNull(generatorService);
        assertNotNull(inputChannel);
    }

    @Test
    void testProductGenerator() {
        // Проверяем генерацию продуктов
        List<Product> products = generatorService.generateProducts(5);
        assertEquals(5, products.size());
    }

    @Test
    void testSendToInputChannel() {
        // Создаем тестовый продукт
        Product product = new Product(ProductType.BEVERAGE, "Test Cola", BigDecimal.valueOf(2.50));

        // Отправляем в канал и проверяем, что не возникает исключений
        inputChannel.send(new GenericMessage<>(product));
    }
}