package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "MovieSessions")
public class MovieSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @OneToOne
    @JoinColumn(name = "cinema_hall_id")
    private CinemaHall cinemaHall;
    private LocalDateTime showTime;

    public Long getId() {
        return id;
    }

    public MovieSession setId(Long id) {
        this.id = id;
        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public MovieSession setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public MovieSession setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
        return this;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public MovieSession setShowTime(int year, int month, int day, int hour, int minute) {
        this.showTime = showTime.of(year, month, day, hour, minute);
        return this;
    }

    @Override
    public String toString() {
        return "MovieSession{"
                + "id=" + id
                + ", movie=" + movie
                + ", cinemaHall="
                + cinemaHall
                + ", showTime="
                + showTime
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieSession that = (MovieSession) o;
        return Objects.equals(id, that.id)
                && Objects.equals(movie, that.movie)
                && Objects.equals(cinemaHall, that.cinemaHall)
                && Objects.equals(showTime, that.showTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, cinemaHall, showTime);
    }
}
