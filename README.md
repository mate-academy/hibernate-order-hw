# Implement order

- Create models
    - Order
- Create DAO
    - OrderDao. To get the order for a specific user create the method getByUser().
- Create service  
    - OrderService
        ```java
        public interface OrderService {
            Order completeOrder(ShoppingCart shoppingCart);
            
            List<Order> getOrdersHistory(User user);
        }
        ``` 
- In the `mate/academy/Main.main()` method create an instance of OrderService using injector and test all methods from it.   

__Attention!!!__: you can have some problems with the naming of the table. Let's use `orders` instead of `order`.

__You can check yourself using this__ [checklist](./checklist.md)

### Model structure 
![pic](Hibernate_Cinema_Uml.png)
