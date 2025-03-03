package com.videogametracker.games.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameListInfo {
    private Long id;
    private String name;
    private GameCover cover;
    private List<GameReleaseDate> release_dates;
    private List<GameGenre> genres;
    private String summary;
}
