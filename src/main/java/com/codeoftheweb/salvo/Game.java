package com.codeoftheweb.salvo;

import javax.persistence.Entity;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/api")

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Game() {}

    public Game(Date creationDate) {
            this.creationDate = creationDate;
    }

    public Date getCreationDate () {
            return creationDate;
        }

    public void setCreationDate (Date creationDate){
            this.creationDate = creationDate;
        }

    public Set<GamePlayer> getGamePlayers () {
            return gamePlayers;
        }

    public void addGamePlayers (GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }
}




