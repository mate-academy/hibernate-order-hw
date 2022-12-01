package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.CinemaHall;

public interface CinemaHallDao extends GenericDao<CinemaHall> {
    Optional<CinemaHall> get(Long id);

    List<CinemaHall> getAll();
}
