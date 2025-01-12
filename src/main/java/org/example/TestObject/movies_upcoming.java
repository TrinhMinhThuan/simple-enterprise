package org.example.TestObject;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class movies_upcoming {
    private ObjectId _id;
    public ObjectId get_id() {
        return this._id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    private Integer tmdb_id;
    public Integer getTmdb_id() {
        return this.tmdb_id;
    }
    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
    }
    private Boolean adult;
    public Boolean getAdult() {
        return this.adult;
    }
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
    private String backdrop_path;
    public String getBackdrop_path() {
        return this.backdrop_path;
    }
    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
    private ArrayList genre_ids;
    public ArrayList getGenre_ids() {
        return this.genre_ids;
    }
    public void setGenre_ids(ArrayList genre_ids) {
        this.genre_ids = genre_ids;
    }
    private Integer id;
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    private String original_language;
    public String getOriginal_language() {
        return this.original_language;
    }
    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }
    private String original_title;
    public String getOriginal_title() {
        return this.original_title;
    }
    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }
    private String overview;
    public String getOverview() {
        return this.overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    private Double popularity;
    public Double getPopularity() {
        return this.popularity;
    }
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
    private String poster_path;
    public String getPoster_path() {
        return this.poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    private String release_date;
    public String getRelease_date() {
        return this.release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    private String title;
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    private Boolean video;
    public Boolean getVideo() {
        return this.video;
    }
    public void setVideo(Boolean video) {
        this.video = video;
    }
    private Double vote_average;
    public Double getVote_average() {
        return this.vote_average;
    }
    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
    private Integer vote_count;
    public Integer getVote_count() {
        return this.vote_count;
    }
    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }
    public movies_upcoming() {}
}
