package com.sndp.sbm.orderservice.service;

import com.sndp.sbm.orderservice.dto.InventoryResponse;
import com.sndp.sbm.orderservice.dto.OrderLineItemsDto;
import com.sndp.sbm.orderservice.dto.OrderRequest;
import com.sndp.sbm.orderservice.event.OrderPlacedEvent;
import com.sndp.sbm.orderservice.model.Order;
import com.sndp.sbm.orderservice.model.OrderLineItems;
import com.sndp.sbm.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.transaction.TransactionScoped;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional

public class OrderServiceClass {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call Inventory Service, and place order if product is in
        // stock
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                // publish Order Placed Event
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        });

    }
//    public String placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//
//       List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//               .map(this::mapToDto)
//               .toList();
//
//        order.setOrderLineItemsList(orderLineItemsList);
//
//       List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
//
////Call the Inventory Service from the Order Service, check the product is available in the inventory or not.
//        InventoryResponse [] inventoryResponsArray = webClientBuilder.build().get()
//                .uri("http://inventory-service/api/inventory",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                        .retrieve()
//                                .bodyToMono(InventoryResponse[].class)
//                                        .block();
//        boolean allProductsInStock = false;
//        if (inventoryResponsArray != null) {
//            allProductsInStock = Arrays.stream(inventoryResponsArray).allMatch(InventoryResponse::isInStock);
//        }
//        if(allProductsInStock){
//            orderRepository.save(order);   //Save Method for Order
//            return "Order Placed successfully";
//
//        } else {
//            throw new IllegalAccessException("This product is out of stock, please try again later");
//        }
////       orderRepository.save(order);
//    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

}


/* A UUID (Universally Unique Identifier) is a 128-bit identifier that
is guaranteed to be unique across all devices and time.
It's often used to uniquely identify resources or entities in distributed systems,
databases, and other scenarios where ensuring global uniqueness is essential.*/