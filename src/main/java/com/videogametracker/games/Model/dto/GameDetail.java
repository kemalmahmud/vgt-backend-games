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
public class GameDetail {
    private Long id;
    private String name;
    private String summary;
    private GameCover cover;
    private GameFranchise franchise;
    private List<GamePlatform> platforms;
    private List<GameGenre> genres;
    private List<GameInvolvedCompany> involved_companies;
    private List<GameScreenshot> screenshots;
    private List<GameReleaseDate> release_dates;
    private List<GameSimilar> similar_games;
    private Double rating;
    private Integer ratingCount;
    private Double vgtRating;
    private Double vgtRatingCount;
}
