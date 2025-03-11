package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany
    private List<Ticket> tickets;
    private LocalDateTime orderDate;
    @ManyToOne
    private User user;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public Order setId(long id) {
        this.id = id;
        return this;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Order setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return "Order{"
                + "id=" + id
                + ", tickets=" + tickets
                + ", orderDate=" + orderDate
                + ", user=" + user
                + '}';
    }
}
