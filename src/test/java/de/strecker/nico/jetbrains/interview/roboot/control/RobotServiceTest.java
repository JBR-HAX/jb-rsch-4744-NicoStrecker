package de.strecker.nico.jetbrains.interview.roboot.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.LocationDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.MoveDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.value.Direction;
import de.strecker.nico.jetbrains.interview.roboot.entity.LocationToMoveRepository;
import de.strecker.nico.jetbrains.interview.roboot.entity.MoveToLocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RobotServiceTest {

    @Mock
    private MoveToLocationRepository moveToLocationRepository;

    @Mock
    private LocationToMoveRepository locationToMoveRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RobotService robotService;

    @Test
    public void getLocationsForMoves_singleMove_rightDirection() {
        // Given
        MoveDto move = new MoveDto(Direction.EAST, 3);
        List<MoveDto> moves = List.of(move);

        // When
        List<LocationDto> locations = robotService.getLocationsForMoves(moves);

        // Then
        assertEquals(2, locations.size());
        assertEquals(new LocationDto(0, 0), locations.get(0));
        assertEquals(new LocationDto(3, 0), locations.get(1));
    }

    @Test
    public void getLocationsForMoves_multipleMoves_variousDirections() {
        // Given
        MoveDto move1 = new MoveDto(Direction.NORTH, 2);
        MoveDto move2 = new MoveDto(Direction.WEST, 1);
        List<MoveDto> moves = Arrays.asList(move1, move2);

        // When
        List<LocationDto> locations = robotService.getLocationsForMoves(moves);

        // Then
        assertEquals(3, locations.size());
        assertEquals(new LocationDto(0, 0), locations.get(0));
        assertEquals(new LocationDto(0, 2), locations.get(1));
        assertEquals(new LocationDto(-1, 2), locations.get(2));
    }

    @Test
    public void getLocationsForMoves_noMoves_staysAtOrigin() {
        // Given
        List<MoveDto> moves = List.of();

        // When
        List<LocationDto> locations = robotService.getLocationsForMoves(moves);

        // Then
        assertEquals(1, locations.size());
        assertEquals(new LocationDto(0, 0), locations.getFirst());
    }


    @Test
    public void getMovesForLocations_singleMove_rightDirection() {
        // Given
        List<LocationDto> locations = Arrays.asList(new LocationDto(0, 0), new LocationDto(3, 0));

        // When
        List<MoveDto> moves = robotService.getMovesForLocations(locations);

        // Then
        assertEquals(1, moves.size());
        assertEquals(new MoveDto(Direction.EAST, 3), moves.get(0));
    }

    @Test
    public void getMovesForLocations_multipleMoves_variousDirections() {
        // Given
        List<LocationDto> locations = Arrays.asList(new LocationDto(0, 0), new LocationDto(0, 2), new LocationDto(-1, 2));

        // When
        List<MoveDto> moves = robotService.getMovesForLocations(locations);

        // Then
        assertEquals(2, moves.size());
        assertEquals(new MoveDto(Direction.NORTH, 2), moves.get(0));
        assertEquals(new MoveDto(Direction.WEST, 1), moves.get(1));
    }

    @Test
    public void getMovesForLocations_noMoves_staysAtOrigin() {
        // Given
        List<LocationDto> locations = List.of(new LocationDto(0, 0));

        // When
        List<MoveDto> moves = robotService.getMovesForLocations(locations);

        // Then
        assertEquals(0, moves.size());
    }

}