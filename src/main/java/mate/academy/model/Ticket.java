package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import mate.academy.exception.MovieSessionSetException;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_session_id", nullable = false)
    private MovieSession movieSession;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public void setMovieSession(MovieSession movieSession) {
        if (movieSession.getShowTime().isBefore(LocalDateTime.now())) {
            throw new MovieSessionSetException("Movie session is already passed. "
                    + "Movie Session: " + movieSession + ". User: " + user);
        }
        this.movieSession = movieSession;
    }

    @Override
    public String toString() {
        return "Ticket{"
            + "id=" + id
            + ", movieSession=" + movieSession
            + ", user=" + user
            + '}';
    }

}
