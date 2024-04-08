package com.sndp.sbm.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Lombok

@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

   private List<OrderLineItemsDto> orderLineItemsDtoList;

}
