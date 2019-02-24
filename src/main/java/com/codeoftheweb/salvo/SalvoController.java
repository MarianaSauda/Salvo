package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.lang.Object;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShipRepository shipRepository;

    //@RequestMapping("/games")
    public List<Map<String, Object>> getGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGamesDTO(game))
                .collect(Collectors.toList());
    }


    private Map<String, Object> makeGamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate", game.getCreationDate());
        dto.put("gamePlayers", getGamePlayerList(game.getGamePlayers()));
        dto.put("scores", getScoreList(game.getScores()));
        return dto;
    }

    private List<Map<String, Object>> getGamePlayerList(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(gamesPlayer -> gamePlayersDTO(gamesPlayer))
                .collect(Collectors.toList());
    }


    private Map<String, Object> gamePlayersDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gpid", gamePlayer.getId()); //id
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        return dto;
    }

    public List<Map<String, Object>> getPlayerList(List<Player> players) {
        return players
                .stream()
                .map(player -> PlayersDTO(player))
                .collect(Collectors.toList());
    }

    private Map<String, Object> PlayersDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        return dto;
    }

    public List<Map<String, Object>> makeShipList(Set<Ship> ships) {
        return ships
                .stream()
                .map(ship -> shipDTO(ship))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getShipList(Set<Ship> ships) {
        return ships
                .stream()
                .map(ship -> shipDTO(ship))
                .collect(Collectors.toList());
    }

    //ships data transfer object
    private Map<String, Object> shipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("shipType", ship.getShipType());
        dto.put("player", ship.getGamePlayer().getPlayer().getId());
        dto.put("locations", ship.getLocation());
        return dto;
    }



    @RequestMapping("/leaderBoard")
    private List<Map<String, Object>> makeLeaderboard() {
        return playerRepository
           .findAll()
           .stream()
           .map(player -> makeScoreDTO(player))
           .collect(Collectors.toList());
    }

    private Map<String, Object> makeScoreDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put ("userName", player.getUserName());
        dto.put("total", player.getScores(player));
        dto.put("won", player.getWins(player.getScores()));
        dto.put("lost", player.getLoses(player.getScores()));
        dto.put("tied", player.getDraws (player.getScores()));
        return dto;
    }
    public List<Map<String, Object>> getScoreList(Set<Score> scores) {
        return scores
                .stream()
                .map(score -> scoreDTO(score))
                .collect(Collectors.toList());
    }

    //scores data transfer object
    private Map<String, Object> scoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("playerID", score.getPlayer().getId());
        //dto.put("player", score.getPlayer().getId());
        dto.put("score", score.getScore());
        dto.put("finishDate", score.getFinishDate());
        return dto;
    }


    public List<Map<String, Object>> getScores(List<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(gamePlayer -> MakeScoreDTO(gamePlayer))
                .collect(Collectors.toList());
    }

    public Map<String, Object> MakeScoreDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("playerID", gamePlayer.getPlayer().getId());
        if (gamePlayer.getScore() != null)
            dto.put("score", gamePlayer.getScore().getScore());

        return dto;
    }


    @RequestMapping("/game_view/{gpid}")
    public ResponseEntity<Object> cheat(@PathVariable long gpid, Authentication authentication) {
        Player player = getLoggedPlayer(authentication);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);
        if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("error", "Forbidden"), HttpStatus.FORBIDDEN);
        }

        if (player.getId() != gamePlayer.getPlayer().getId()) {
            return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("creationDate", gamePlayer.getGame().getCreationDate().getTime());
        dto.put("gamePlayers", getGamePlayerList(gamePlayer.getGame().getGamePlayers()));
        dto.put("ships", getShipList(gamePlayer.getShips()));
        dto.put("salvoes", getSalvoList(gamePlayer.getGame()));
        //return dto;
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    Player getLoggedPlayer(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private Map<String, Object> salvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("salvoLocations", salvo.getSalvoLocations());
        return dto;
    }

    private List<Map<String, Object>> getSalvoList(Game game) {
        List<Map<String, Object>> myList = new ArrayList<>();
        game.getGamePlayers().forEach(gamePlayer -> myList.addAll(makeSalvoList(gamePlayer.getSalvoes())));
        return myList;
    }

    private List<Map<String, Object>> makeSalvoList(List<Salvo> salvoes) {
        return salvoes
                .stream()
                .map(salvo -> salvoDTO(salvo))
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public Map<String, Object> makeLoggedPlayer(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            dto.put("player", "Guest");
        else
            dto.put("player", loggedPlayerDTO(playerRepository.findByUserName(authentication.getName())));
        dto.put("games", getGames());
        return dto;
    }

    public Map<String, Object> loggedPlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("name", player.getUserName());
        return dto;
    }

    // metodo para crear un jugador
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createPlayer(@RequestParam String userName, @RequestParam String password) {
        if (userName.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }
        Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    // metodo para crear un juego
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            response.put("error", "unauthorized");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            Game game = gameRepository.save(new Game(new Date()));
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, game.getCreationDate()));
            response.put("gpid", gamePlayer.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }


    // metodo para unirse a un juego
    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@PathVariable Long nn, Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            response.put("error", "unauthorized");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else {
            Game game = gameRepository.getOne(nn);
            if (game != null) {
                if (game.getGamePlayers().size() < 2) {
                    Player player = playerRepository.findByUserName(authentication.getName());
                    GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, game.getCreationDate()));
                    response.put("gpid", gamePlayer.getId());
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                } else {
                    response.put("error", "game is full");
                    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
                }
            } else {
                response.put("error", "no such game");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        }

    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }




    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships) {

        if (isGuest(authentication)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Player player = getCurrentPlayerByName(authentication.getName());

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if (gamePlayer == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (player.getId() != gamePlayer.getPlayer().getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (gamePlayer.getShips().size()>= 5) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        for (Ship ship: ships){
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        }

        return new ResponseEntity<>(makeMap("OK", "Ships placed"), HttpStatus.OK);
    }

    private Player getCurrentPlayerByName(String name) {
        Player currentPlayer = playerRepository.findByUserName(name);

        return currentPlayer;
    }
    


}





