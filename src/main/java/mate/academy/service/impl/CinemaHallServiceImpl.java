package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.CinemaHallDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.CinemaHall;
import mate.academy.service.CinemaHallService;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    @Inject
    private CinemaHallDao cinemaHallDao;

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        return cinemaHallDao.add(cinemaHall);
    }

    @Override
    public CinemaHall get(Long id) {
        return cinemaHallDao.get(id).orElseThrow(
                () -> new RuntimeException("cinemaHall not found"));
    }

    @Override
    public List<CinemaHall> getAll() {
        return cinemaHallDao.getAll();
    }

    @Override
    public Boolean update(CinemaHall cinemaHall) {
        CinemaHall currentCinemaHall = get(cinemaHall.getId());
        cinemaHallDao.update(cinemaHall);
        return currentCinemaHall.equals(get(cinemaHall.getId()));
    }

    @Override
    public Boolean delete(Long id) {
        return cinemaHallDao.delete(id);
    }
}
