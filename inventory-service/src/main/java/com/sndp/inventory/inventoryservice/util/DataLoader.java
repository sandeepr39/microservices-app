package com.sndp.inventory.inventoryservice.util;

import com.sndp.inventory.inventoryservice.model.Inventory;
import com.sndp.inventory.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    /* CommandLineRunner is an interface in the Spring Framework used to run code after the Spring application
		context is initialized and before the application starts handling HTTP requests. It is part of Spring Boot's
		application lifecycle and provides a way to perform tasks or run code logic that should be executed once the
		application is fully up and running.
		1.Database Initialization
		2.Scheduled Tasks
		3.Configuration Loading
		4.Logging and Reporting and more
		 */
    public final InventoryRepository inventoryRepository;


       @Override
       public void run(String... args) throws Exception{
           /*String... args: This is a variable-length argument (varargs) parameter.
           It allows you to pass an arbitrary number of String arguments to the method.
            */

            Inventory inventory = new Inventory();
            inventory.setSkuCode("iphone_13");
            inventory.setQuantity(100);

            Inventory inventory1 = new Inventory();
            inventory1.setSkuCode("iphone_13_red");
            inventory1.setQuantity(0);

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
        }

    }


