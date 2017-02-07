package com.stkizema.test8telemarketing.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "idMovie", unique = true)
        })
public class MovieDb {
    @Id
    private Long idMovie;

    @NotNull
    @Property(nameInDb = "posterPath")
    private String posterPath;

    @NotNull
    @Property(nameInDb = "adult")
    private boolean adult;

    @NotNull
    @Property(nameInDb = "overview")
    private String overview;

    @NotNull
    @Property(nameInDb = "releaseDate")
    private String releaseDate;

    @NotNull
    @Property(nameInDb = "id")
    private Integer id;

    @NotNull
    @Property(nameInDb = "originalTitle")
    private String originalTitle;

    @NotNull
    @Property(nameInDb = "originalLanguage")
    private String originalLanguage;

    @NotNull
    @Property(nameInDb = "title")
    private String title;

    @NotNull
    @Property(nameInDb = "backdropPath")
    private String backdropPath;

    @NotNull
    @Property(nameInDb = "popularity")
    private Double popularity;

    @NotNull
    @Property(nameInDb = "voteCount")
    private Integer voteCount;

    @NotNull
    @Property(nameInDb = "video")
    private Boolean video;

    @NotNull
    @Property(nameInDb = "voteAverage")
    private Double voteAverage;

    public MovieDb(String posterPath, boolean adult, String overview, String releaseDate, Integer id,
                   String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                   Integer voteCount, Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    @Generated(hash = 967079244)
    public MovieDb(Long idMovie, @NotNull String posterPath, boolean adult, @NotNull String overview, @NotNull String releaseDate,
            @NotNull Integer id, @NotNull String originalTitle, @NotNull String originalLanguage, @NotNull String title,
            @NotNull String backdropPath, @NotNull Double popularity, @NotNull Integer voteCount, @NotNull Boolean video,
            @NotNull Double voteAverage) {
        this.idMovie = idMovie;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    @Generated(hash = 1190811592)
    public MovieDb() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Long getIdMovie() {
        return this.idMovie;
    }

    public void setIdMovie(Long idMovie) {
        this.idMovie = idMovie;
    }

    public boolean getAdult() {
        return this.adult;
    }
}
