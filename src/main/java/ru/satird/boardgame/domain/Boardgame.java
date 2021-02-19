package ru.satird.boardgame.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bgg")
public class Boardgame {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bggId;
    private String alternate;
    private String primaryName;
    private String thumbnail;
    private String image;
    private String description;
    private Integer yearpublished;
    private Integer minplayers;
    private Integer maxplayers;
    private Integer playingtime;
    private Double usersrated;
    private Integer boardGameRank;
    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = {@JoinColumn(name = "boardgame_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> favorites = new HashSet<>();
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User user;

    public Long getBggId() {
        return bggId;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Integer getYearPublished() {
        return yearpublished;
    }

    public Integer getMinPlayers() {
        return minplayers;
    }

    public Integer getMaxPlayers() {
        return maxplayers;
    }

    public Integer getPlayingtime() {
        return playingtime;
    }

    public Double getUsersrated() {
        return usersrated;
    }

    public Integer getBoardGameRank() {
        return boardGameRank;
    }

    public Set<User> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<User> favorites) {
        this.favorites = favorites;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getAlternate() {
        return alternate;
    }
}
