package ru.satird.boardgame.domain.dto;

import ru.satird.boardgame.domain.Boardgame;

public class BoardgameDto {
    private Long bggId;
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
    private Long favoriteCount;
    private Boolean isFavorite;

    public BoardgameDto(Boardgame boardgame, Long favoriteCount, Boolean isFavorite) {
        this.bggId = boardgame.getBggId();
        this.primaryName = boardgame.getPrimaryName();
        this.thumbnail = boardgame.getThumbnail();
        this.image = boardgame.getImage();
        this.description = boardgame.getDescription();
        this.yearpublished = boardgame.getYearPublished();
        this.minplayers = boardgame.getMinPlayers();
        this.maxplayers = boardgame.getMaxPlayers();
        this.playingtime = boardgame.getPlayingtime();
        this.usersrated = boardgame.getUsersrated();
        this.boardGameRank = boardgame.getBoardGameRank();
        this.favoriteCount = favoriteCount;
        this.isFavorite = isFavorite;
    }

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

    public Integer getYearpublished() {
        return yearpublished;
    }

    public Integer getMinplayers() {
        return minplayers;
    }

    public Integer getMaxplayers() {
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

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

}
