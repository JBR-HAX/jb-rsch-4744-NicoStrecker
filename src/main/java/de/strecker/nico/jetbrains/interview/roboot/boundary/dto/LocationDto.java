package de.strecker.nico.jetbrains.interview.roboot.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @JsonProperty
    private int x;

    @JsonProperty
    private int y;
}
