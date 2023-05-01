## Common mistakes (hibernate-order-hw)

* Get information about order and tickets that were purchased in the method `getOrdersHistory`.
You can use inner join or a separate query for this purpose. `FetchType.EAGER` is a bad practice. 
See more [here](https://thorben-janssen.com/common-hibernate-mistakes-cripple-performance/#Mistake_1_Use_Eager_Fetching)
* Don't create method `completeOrder(ShoppingCart shoppingCart)` on the dao layer. 
Let's create `add(Order order)` instead. 
* Check carefully the relationships in `Order` class. You should use `OneToMany` with `tickets`.
* When passing tickets from shoppingCart to order - pass the copy of this list(you can use `ArrayList` constructor for this);
The reason is that Hibernate will automatically delete these tickets from shoppingCart(in DB) after noticing them in our order object. 
Let's do this manually using `clear()` method from `ShoppingCartService` after the order with tickets has been created.

* Remember to add `catch` blocks for operations of all types on DAO layer.
