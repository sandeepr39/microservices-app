package com.sndp.sbm.orderservice.repository;


import com.sndp.sbm.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {


    /* Creating the data repo for product-service and extending it to MySqlDB and
     adding Product and identifier Key (coz ID is  String) as generics  */


}
