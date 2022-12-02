package mate.academy.service;

import java.util.List;
import mate.academy.model.CinemaHall;

public interface CinemaHallService extends AbstractService<CinemaHall> {
    CinemaHall get(Long id);
    
    List<CinemaHall> getAll();
}
