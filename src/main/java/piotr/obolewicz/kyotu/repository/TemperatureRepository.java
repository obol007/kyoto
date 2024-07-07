package piotr.obolewicz.kyotu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import piotr.obolewicz.kyotu.model.CityAverageTemperature;

import java.util.List;

public interface TemperatureRepository extends JpaRepository<CityAverageTemperature, Long> {

    List<CityAverageTemperature> findByCityName(String cityName);

}
