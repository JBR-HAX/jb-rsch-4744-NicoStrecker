package de.strecker.nico.jetbrains.interview.roboot.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.LocationDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.MoveDto;
import de.strecker.nico.jetbrains.interview.roboot.entity.MoveToLocationEntity;
import de.strecker.nico.jetbrains.interview.roboot.entity.MoveToLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private final MoveToLocationRepository moveToLocationRepository;
    private final ObjectMapper objectMapper;

    public List<LocationDto> getLocationsForMoves(List<MoveDto> moves) {
        /* Number of moves + 1 for the initial move */
        List<LocationDto> locations = new ArrayList<>(moves.size()+1);
        LocationDto initialLocation = new LocationDto(0, 0);
        locations.add(initialLocation);

        for(MoveDto move : moves){
            LocationDto currentLocation = locations.getLast();
            LocationDto newLocation = new LocationDto(
                currentLocation.getX() + move.getDirection().getX() * move.getSteps(),
                currentLocation.getY() + move.getDirection().getY() * move.getSteps()
            );
            locations.add(newLocation);
        }

        saveToDatabase(moves, locations);
        return locations;
    }

    protected void saveToDatabase(List<MoveDto> input, List<LocationDto> output)  {
        log.info("Saving input and output to database");
        MoveToLocationEntity moveToLocation =  new MoveToLocationEntity();

        try{
            moveToLocation.setInput(objectMapper.writeValueAsString(input));
        }catch(JsonProcessingException e){
            throw new RuntimeException("Could not serialize input", e);
        }

        try {
            moveToLocation.setOutput(objectMapper.writeValueAsString(output));
        }catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize output", e);
        }

        moveToLocationRepository.save(moveToLocation);
    }
}
