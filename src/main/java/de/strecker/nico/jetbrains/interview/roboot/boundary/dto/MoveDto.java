package de.strecker.nico.jetbrains.interview.roboot.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.value.Direction;
import lombok.Data;

@Data
public class MoveDto {

    @JsonProperty
    private Direction direction;

    @JsonProperty
    private Integer steps;
}
