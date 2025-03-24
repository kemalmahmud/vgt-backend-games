package com.videogametracker.games.Controller;

import com.videogametracker.games.Model.dto.BaseResponse;
import com.videogametracker.games.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<BaseResponse> getGames(@RequestParam(defaultValue = "10") int limit,
                                                 @RequestParam(defaultValue = "0") int offset,
                                                 @RequestParam(defaultValue = "") String keyword) {
        return gameService.getGameList(limit, offset, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getGameDetail(@PathVariable Long id) {
        return gameService.getGameDetail(id);
    }
}
