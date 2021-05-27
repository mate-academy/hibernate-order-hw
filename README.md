# Implement order

- Create models
    - Order
- Create DAO
    - OrderDao
- Create service  
    - OrderService
        ```java
        public interface OrderService {
            Order completeOrder(ShoppingCart shoppingCart);
            
            List<Order> getOrdersHistory(User user);
        }
        ```    

Attention: you can have some problem with naming of the table. Let's use `orders` instead of `order`.

__You can check yourself using this__ [checklist](https://mate-academy.github.io/jv-program-common-mistakes/hibernate/add-order/add-order-hw)  
