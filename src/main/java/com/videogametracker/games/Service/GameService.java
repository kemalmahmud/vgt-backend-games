package com.videogametracker.games.Service;

import com.videogametracker.games.Model.dto.BaseResponse;
import com.videogametracker.games.Model.dto.GameDetail;
import com.videogametracker.games.Model.dto.GameList;
import com.videogametracker.games.Model.dto.GameListInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GameService {

    @Value("${igdb.client-id}")
    private String clientId;
    @Value("${igdb.client-secret}")
    private String clientSecret;
    @Value("${igdb.auth-url}")
    private String authUrl;
    @Value("${igdb.api-game-url}")
    private String apiGameUrl;

    @Autowired
    RestTemplate restTemplate;
    private String accessToken = null;

    private void fetchAccessToken() {
        try {
            String url = authUrl + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";

            ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                accessToken = (String) response.getBody().get("access_token");
            }
        }
        catch (Exception e) {
            log.error("error fetch access token");
        }
    }

    private void setHeader(HttpHeaders headers) {
        if (accessToken == null) {
            fetchAccessToken(); // Ambil token jika belum ada
        }

        // Header HTTP
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public ResponseEntity<BaseResponse> getGameList(int limit, int offset, String keyword) {
        var result = new BaseResponse();
        try {
            // Header HTTP
            HttpHeaders headers = new HttpHeaders();
            setHeader(headers);

            // Query IGDB API
            var query =  String.format("fields id,name,summary,cover.url,platforms.name,genres.name,release_dates.y; limit %d; offset %d;", limit, offset);
            if(keyword != null && !keyword.equals("")) {
                query = String.format(query + "search \"%s\";", keyword);
            }
            HttpEntity<String> request = new HttpEntity<>(query, headers);
            ResponseEntity<GameListInfo[]> response = restTemplate.exchange(apiGameUrl, HttpMethod.POST, request, GameListInfo[].class);
            var gameList = List.of(response.getBody());
            // ganti ke gambar besar
            gameList.forEach(g -> {
                if (g.getCover() != null) {
                    g.getCover().setUrl(g.getCover().getUrl().replace("t_thumb", "t_cover_big"));
                }
            });
            result.setStatus(HttpStatus.OK.value());
            result.setData(GameList.builder().games(gameList).build());
            result.setMessage("Success retrieving games");
            return ResponseEntity.ok(result);
        }
        catch(Exception e) {
            log.error("error in getGames, " + e.getMessage());
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setData(null);
            result.setMessage("Error retrieving games");
            return ResponseEntity.badRequest().body(result);
        }
    }

    public ResponseEntity<BaseResponse> getGameDetail(Long id) {
        BaseResponse result = new BaseResponse();
        try {
            // Header HTTP
            HttpHeaders headers = new HttpHeaders();
            setHeader(headers);

            // Query IGDB API
            String body = String.format("fields id,name,summary,cover.url,platforms.name,genres.name,franchise.name,involved_companies.company.name,screenshots.url,similar_games.name," +
                    "release_dates.y,rating,rating_count; where id = %d;", id);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<GameDetail[]> response = restTemplate.exchange(apiGameUrl, HttpMethod.POST, request, GameDetail[].class);

            var game = response.getBody()[0];

            result.setStatus(HttpStatus.OK.value());
            result.setData(game);
            result.setMessage("Success retrieving game detail");
            return ResponseEntity.ok(result);

        }
        catch(Exception e) {
            log.error("error in getGameDetail, " + e.getMessage());
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setData(null);
            result.setMessage("Error retrieving game detail");
            return ResponseEntity.badRequest().body(result);
        }
    }
}