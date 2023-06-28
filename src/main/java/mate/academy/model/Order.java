package mate.academy.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    @JoinTable(name="orders_tickets",
                joinColumns = @JoinColumn(name="order_id"),
                inverseJoinColumns = @JoinColumn(name="ticket_id"))
    private List<Ticket> tickets;
    private LocalDateTime orderDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
