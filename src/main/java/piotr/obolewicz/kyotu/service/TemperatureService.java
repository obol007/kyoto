package piotr.obolewicz.kyotu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import piotr.obolewicz.kyotu.model.CityAverageTemperature;
import piotr.obolewicz.kyotu.repository.TemperatureRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final TemperatureRepository repository;

    public void saveAll(List<CityAverageTemperature> temperatures) {
        repository.saveAll(temperatures);
    }

    public List<CityAverageTemperature> getTemperaturesByCity(String cityName) {
        return repository.findByCityName(cityName);
    }
}

