package com.liquid.liquidpedia.dto;

import jakarta.validation.constraints.*;

public class ReviewDto {

    @NotNull(message = "Rating harus diisi")
    @Min(1)
    @Max(5)
    private Float rating;

    private String comment;

    public ReviewDto() {}

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
