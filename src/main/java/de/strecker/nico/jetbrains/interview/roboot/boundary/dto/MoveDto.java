package de.strecker.nico.jetbrains.interview.roboot.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.value.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveDto {

    @JsonProperty
    private Direction direction;

    @JsonProperty
    private Integer steps;
}
