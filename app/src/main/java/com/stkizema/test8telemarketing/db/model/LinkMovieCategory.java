package com.stkizema.test8telemarketing.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "idLink", unique = true)
        })
public class LinkMovieCategory {

    @Id
    private Long idLink;

    @NotNull
    @Property(nameInDb = "id_movie")
    private Integer idMovie;

    @NotNull
    @Property(nameInDb = "id_category")
    private Integer idCategory;

@Generated(hash = 1592601342)
public LinkMovieCategory(Long idLink, @NotNull Integer idMovie,
        @NotNull Integer idCategory) {
    this.idLink = idLink;
    this.idMovie = idMovie;
    this.idCategory = idCategory;
}

@Generated(hash = 654433947)
public LinkMovieCategory() {
}

public Long getIdLink() {
    return this.idLink;
}

public void setIdLink(Long idLink) {
    this.idLink = idLink;
}

public Integer getIdMovie() {
    return this.idMovie;
}

public void setIdMovie(Integer idMovie) {
    this.idMovie = idMovie;
}

public Integer getIdCategory() {
    return this.idCategory;
}

public void setIdCategory(Integer idCategory) {
    this.idCategory = idCategory;
}
}
