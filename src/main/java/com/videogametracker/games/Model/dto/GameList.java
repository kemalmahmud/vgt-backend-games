package com.videogametracker.games.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameList {
    private List<GameListInfo> games;
    private Integer currentPage;
    private Integer totalGames;
    private Integer totalPages;
}
