package piotr.obolewicz.kyotu.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import piotr.obolewicz.kyotu.model.CityAverageTemperature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private static final String CSV_FILE_PATH = "src/main/resources/example_file.csv";

    private final TemperatureService service;

    @Override
    public void run(String... args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String currentCity = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            List<CityAverageTemperature> cityAverageTemperatures = new ArrayList<>();
            Map<String, TemperatureEntry> temperatureEntries = new HashMap<>();
            String[] line;

            while ((line = csvReader.readNext()) != null) {
                String cityName = line[0];
                LocalDateTime timestamp = LocalDateTime.parse(line[1], formatter);
                int year = timestamp.getYear();
                float temperature = Float.parseFloat(line[2]);

                if (currentCity == null) {
                    currentCity = cityName;
                }

                if (!cityName.equals(currentCity)) {
                    insertAverageTemperaturesToDatabase(cityAverageTemperatures, temperatureEntries);
                    currentCity = cityName;
                }

                addTemperature(cityName, year, temperatureEntries, temperature);
            }

            insertAverageTemperaturesToDatabase(cityAverageTemperatures, temperatureEntries);
            log.info("Data has been inserted successfully.");

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private static void addTemperature(String cityName, int year, Map<String, TemperatureEntry> temperatureEntries, float temperature) {
        String key = cityName + "_" + year;
        TemperatureEntry temperatureEntry = temperatureEntries.computeIfAbsent(key, k -> new TemperatureEntry(cityName, year));
        temperatureEntry.addTemperature(temperature);
    }

    private void insertAverageTemperaturesToDatabase(List<CityAverageTemperature> cityAverageTemperatures, Map<String, TemperatureEntry> temperatureEntries) {
        cityAverageTemperatures.addAll(calculateAverages(temperatureEntries));
        service.saveAll(cityAverageTemperatures);
        cityAverageTemperatures.clear();
        temperatureEntries.clear();
    }

    private List<CityAverageTemperature> calculateAverages(Map<String, TemperatureEntry> temperatureEntry) {
        List<CityAverageTemperature> result = new ArrayList<>();
        for (TemperatureEntry entry : temperatureEntry.values()) {
            result.add(new CityAverageTemperature(entry.getCityName(), entry.getYear(), entry.getAverageTemperature()));
        }
        return result;
    }
}

class TemperatureEntry {
    @Getter
    private String cityName;
    @Getter
    private int year;
    private float tempSum;
    private int count;

    public TemperatureEntry(String cityName, int year) {
        this.cityName = cityName;
        this.year = year;
        this.tempSum = 0;
        this.count = 0;
    }

    public void addTemperature(float temperature) {
        this.tempSum += temperature;
        this.count++;
    }

    public float getAverageTemperature() {
        return count == 0 ? 0 : tempSum / count;
    }
}
