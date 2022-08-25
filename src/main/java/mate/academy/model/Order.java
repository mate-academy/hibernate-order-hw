package mate.academy.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<Ticket> tickets;
    private LocalDateTime localDateTime;

    public Order() {
    }

    public Order(Long id, User user, LocalDateTime localDateTime, List<Ticket> tickets) {
        this.id = id;
        this.user = user;
        this.localDateTime = localDateTime;
        this.tickets = tickets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", localDateTime=" + localDateTime +
                ", tickets=" + tickets +
                '}';
    }
}
