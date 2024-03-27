package com.sndp.sbm.orderservice.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data // Lombok
@Builder
/* Lombok  create builder method,
so that we can use this without initializing
in other classes
 */

@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {

    private long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;


}
