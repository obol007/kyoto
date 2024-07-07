package piotr.obolewicz.kyotu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import piotr.obolewicz.kyotu.serializers.CustomFloatSerializer;

@Entity
@Getter
@NoArgsConstructor
public class CityAverageTemperature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String cityName;
    @JsonProperty("year")
    private int recordYear;
    @JsonSerialize(using = CustomFloatSerializer.class)
    private float averageTemperature;

    public CityAverageTemperature(String cityName, int recordYear, float averageTemperature) {
        this.cityName = cityName;
        this.recordYear = recordYear;
        this.averageTemperature = averageTemperature;
    }
}
