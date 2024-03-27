package com.sndp.sbm.orderservice.dto;

import com.sndp.sbm.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data // Lombok



@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

   private List<OrderLineItemsDto> orderLineItemsDtoList;

}
