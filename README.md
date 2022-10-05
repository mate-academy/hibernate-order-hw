# Implement orders

- Create models
    - Order
- Create DAO
    - OrderDao. To get the orders for a specific user create method getByUser().
- Create service  
    - OrderService
        ```java
        public interface OrderService {
            Order completeOrder(ShoppingCart shoppingCart);
            
            List<Order> getOrdersHistory(User user);
        }
        ```    

Attention: you can have some problem with naming of the table. Let's use `orders` instead of `orders`.

__You can check yourself using this__ [checklist](https://mate-academy.github.io/jv-program-common-mistakes/hibernate/add-orders/add-orders-hw)  

### Model structure 
![pic](Hibernate_Cinema_Uml.png)
