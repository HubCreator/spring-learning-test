package nextstep.helloworld.core.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationConstructorService {
    private final StationRepository stationRepository;

    @Autowired
    public StationConstructorService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public String sayHi() {
        return stationRepository.sayHi();
    }
}
