package piotr.obolewicz.kyotu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import piotr.obolewicz.kyotu.model.CityAverageTemperature;
import piotr.obolewicz.kyotu.service.TemperatureService;

import java.util.List;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class TemperatureController {

    private final TemperatureService service;

    @GetMapping("/{cityName}")
    public List<CityAverageTemperature> getTemperatureByCity(@PathVariable String cityName) {
        return service.getTemperaturesByCity(cityName);
    }
}

