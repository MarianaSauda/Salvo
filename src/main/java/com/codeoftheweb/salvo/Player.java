package com.codeoftheweb.salvo;

import javax.persistence.Entity;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/api")

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    List<Score> scores = new ArrayList<>();

    public Player () {}

    public Player (String userName, String password) {
        this.userName = userName;
        this.setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GamePlayer> getGamePlayers () {
        return gamePlayers;
    }

    public void addGamePlayers (GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Score getScore (Game game) {
        return game
                .getScores()
                .stream()
                .filter(score -> score.getPlayer().equals(this))
                .findFirst()
                .orElse(null);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getScores (Player player){
        return  getWins(player.getScores()) * 1
                + getDraws(player.getScores()) * 0.5
                + getLoses(player.getScores()) * 0;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public double getWins (List<Score>scores){
        return scores
                .stream()
                .filter(score -> score.getScore() == 1)
                .count();
    }

    public double getDraws (List<Score>scores){
        return scores
                .stream()
                .filter(score -> score.getScore() == 0.5)
                .count();
    }

    public double getLoses (List<Score>scores){
        return scores
                .stream()
                .filter(score -> score.getScore() == 0)
                .count();
    }

    public String toString() {
        return userName;
    }

}

