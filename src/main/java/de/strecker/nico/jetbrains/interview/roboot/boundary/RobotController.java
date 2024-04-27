package de.strecker.nico.jetbrains.interview.roboot.boundary;

import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.LocationDto;
import de.strecker.nico.jetbrains.interview.roboot.boundary.dto.MoveDto;
import de.strecker.nico.jetbrains.interview.roboot.control.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    @PostMapping("/locations")
    public ResponseEntity<List<LocationDto>> locations(@RequestBody List<MoveDto> moves){
        return ResponseEntity.ok(robotService.getLocationsForMoves(moves));
    }

    @PostMapping("/moves")
    public ResponseEntity<List<MoveDto>> moves(@RequestBody List<LocationDto> locations){
        return ResponseEntity.ok(robotService.getMovesForLocations(locations));
    }

    @PostMapping("/moves-shortest-path")
    public ResponseEntity<List<MoveDto>> movesShortestPath(@RequestBody List<LocationDto> locations){
        return ResponseEntity.ok(robotService.movesShortestPath(locations));
    }
}
