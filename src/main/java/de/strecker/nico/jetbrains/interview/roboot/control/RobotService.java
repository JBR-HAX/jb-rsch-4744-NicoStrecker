package de.strecker.nico.jetbrains.interview.roboot.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.LocationDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.MoveDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.value.Direction;
import de.strecker.nico.jetbrains.interview.roboot.entity.LocationToMoveEntity;
import de.strecker.nico.jetbrains.interview.roboot.entity.LocationToMoveRepository;
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
    private final LocationToMoveRepository locationToMoveRepository;

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

        this.saveMoveToLocationToDatabase(moves, locations);
        return locations;
    }

    protected void saveMoveToLocationToDatabase(List<MoveDto> input, List<LocationDto> output)  {
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

    public List<MoveDto> getMovesForLocations(List<LocationDto> locations) {
        List<MoveDto> moves = new ArrayList<>(locations.size()-1);
        LocationDto currentLocation = locations.getFirst();
        
        for(int i = 1; i < locations.size(); i++){
            /* TODO: Check that only one changes */
            LocationDto nextLocation = locations.get(i);
            int dx = nextLocation.getX() - currentLocation.getX();
            int dy = nextLocation.getY() - currentLocation.getY();

            Direction direction;
            int steps;

            if(dx > 0) {
                direction = Direction.EAST;
                steps = dx;
            } else if(dx < 0) {
                direction = Direction.WEST;
                steps = -dx;
            } else if(dy > 0) {
                direction = Direction.NORTH;
                steps = dy;
            } else {
                direction = Direction.SOUTH;
                steps = -dy;
            }

            moves.add(new MoveDto(direction, steps));
            currentLocation = nextLocation;
        }
        
        saveLocationToMoveToDatabase(locations,moves);
        return moves;
    
    }

    protected void saveLocationToMoveToDatabase(List<LocationDto> input, List<MoveDto> output){
        log.info("Saving input and output to database");
        LocationToMoveEntity locationToMoveEntity = new LocationToMoveEntity();
        
        try {
            locationToMoveEntity.setInput(objectMapper.writeValueAsString(input));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize input", e);
        }
        
        try {
            locationToMoveEntity.setOutput(objectMapper.writeValueAsString(output));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize output", e);
        }
        
        locationToMoveRepository.save(locationToMoveEntity);


    }

    /**
     * Calculates the shortest path through all given locations using the Nearest Neighbor algorithm.
     * Starts at the first location and repeatedly visits the nearest unvisited location until all locations have been visited.
     *
     * @param locations the list of locations to visit
     * @return the list of moves representing the shortest path
     */
    public List<MoveDto> movesShortestPath(List<LocationDto> locations) {
        List<MoveDto> shortestPath = new ArrayList<>();
        List<LocationDto> remainingLocations = new ArrayList<>(locations);
        LocationDto currentLocation = remainingLocations.remove(0); // start at the first location

        while (!remainingLocations.isEmpty()) {
            LocationDto nearestLocation = findNearestLocation(currentLocation, remainingLocations);
            remainingLocations.remove(nearestLocation);
            int dx = nearestLocation.getX() - currentLocation.getX();
            int dy = nearestLocation.getY() - currentLocation.getY();

            Direction direction;
            int steps;

            if(dx > 0) {
                direction = Direction.EAST;
                steps = dx;
            } else if(dx < 0) {
                direction = Direction.WEST;
                steps = -dx;
            } else if(dy > 0) {
                direction = Direction.NORTH;
                steps = dy;
            } else {
                direction = Direction.SOUTH;
                steps = -dy;
            }

            shortestPath.add(new MoveDto(direction, steps));
            currentLocation = nearestLocation;
        }

        return shortestPath;
    }

    /**
     * Finds the nearest location to a given location from a list of locations.
     * The distance is calculated using the Manhattan distance.
     *
     * @param currentLocation the location to find the nearest location to
     * @param locations the list of locations to search
     * @return the nearest location
     */
    private LocationDto findNearestLocation(LocationDto currentLocation, List<LocationDto> locations) {
        LocationDto nearestLocation = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (LocationDto location : locations) {
            int distance = Math.abs(currentLocation.getX() - location.getX()) + Math.abs(currentLocation.getY() - location.getY());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestLocation = location;
            }
        }

        return nearestLocation;
    }
}
