package mate.academy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
    private LocalDateTime dataShow;
    @OneToMany
    private List<Ticket> tickets;


    public Order() {
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

    public LocalDateTime getDataShow() {
        return dataShow;
    }

    public void setDataShow(LocalDateTime dataShow) {
        this.dataShow = dataShow;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, tickets, dataShow);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Order order = (Order) obj;
        return Objects.equals(id, order.id)
                && Objects.equals(user, order.user)
                && Objects.equals(tickets, order.tickets)
                && Objects.equals(dataShow, order.dataShow);
    }

    @Override
    public String toString() {
        return "Order{"
                + "id=" + id
                + ", user=" + user
                + ", dataShow=" + dataShow
                + ", tickets=" + tickets
                + '}';
    }
}
