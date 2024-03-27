package com.sndp.inventory.inventoryservice.service;

import com.sndp.inventory.inventoryservice.dto.InventoryResponse;
import com.sndp.inventory.inventoryservice.model.Inventory;
import com.sndp.inventory.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows //don't use in production
//    public boolean isInStock(String skuCode) {
        public List<InventoryResponse> isInStock (List<String> skuCode) {
        Thread.sleep(10000);
            log.info("Checking Inventory");
            return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                    .map(inventory ->
                            InventoryResponse.builder()
                                    .skuCode(inventory.getSkuCode())
                                    .isInStock(inventory.getQuantity() > 0)
                                    .build()
                    ).toList();
        }
    }

