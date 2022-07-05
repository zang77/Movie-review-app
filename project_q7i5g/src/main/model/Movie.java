package model;

import org.json.JSONObject;
import persistence.Writable;

// Represent a movie and its name, rating and review
public class Movie implements Writable {
    private String name;
    private int rating;
    private String review;

    // MODIFIES: this
    // REQUIRES: rating is on a scale of one to five
    // EFFECTS: name of the movie is set to movieName; rating of the movie is set to userRating;
    //          the review of the movie is set to comment
    public Movie(String movieName, int userRating, String comment) {
        this.name = movieName;
        this.rating = userRating;
        this.review = comment;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    // MODIFIES: this
    // EFFECTS: update the review of the movie
    public void newReview(String sentences) {
        this.review = sentences;
        EventLog.getInstance().logEvent(new Event("Review updated."));
    }

    // MODIFIES: this
    // EFFECTS: update the rating of the movie
    public void newRating(int stars) {
        this.rating = stars;
        EventLog.getInstance().logEvent(new Event("Rating updated."));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("rating", rating);
        json.put("review", review);
        return json;
    }

    @Override
    public String toString() {
        return String.format("| %20s|  |   %d    |  | %-30s|", name, rating, review);
    }
}
