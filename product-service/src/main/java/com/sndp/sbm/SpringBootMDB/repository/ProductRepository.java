package com.sndp.sbm.SpringBootMDB.repository;

import com.sndp.sbm.SpringBootMDB.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, String> {
    /* Creating the data repo for product-service and extending it to MongoDB and
     adding Product and identifier Key (coz ID is  String) as generics  */



}
