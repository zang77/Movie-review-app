package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;


// Represent a user's personal collection of movies
public class MovieCollection implements Writable {
    private ArrayList<Movie> movieList;

    public MovieCollection() {
        movieList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECT: add a movie to the collection
    public void addMovie(Movie newMovie) {
        movieList.add(newMovie);
        EventLog.getInstance().logEvent(new Event("Movie added to collection."));
    }

    // REQUIRES: the movie to be removed is already in the collection
    // MODIFIES: this
    // EFFECT: remove a movie from the collection
    public void removeMovie(Movie removeMovie) {
        movieList.remove(removeMovie);
        EventLog.getInstance().logEvent(new Event("Movie deleted from collection."));
    }

    // REQUIRES: the collection is not empty
    // EFFECT: show the names of all the movies in the collection
    public ArrayList<String> viewAllNames() {
        ArrayList<String> movieNameList = new ArrayList<>();
        for (Movie m : movieList) {
            movieNameList.add(m.getName());
        }
        EventLog.getInstance().logEvent(new Event("Collection displayed"));
        return movieNameList;
    }

    // EFFECT: display the list of movies
    public ArrayList<Movie> viewAllMovies() {
        return movieList;
    }

    // EFFECT:  true if the collection is empty, false otherwise
    public boolean isEmpty() {
        return movieList.size() == 0;
    }

    // return the size of the collection
    public int getSize() {
        return movieList.size();
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("favorite", collectionToJson());
        return json;
    }

    // EFFECTS: returns things in this collection as a JSON array
    private JSONArray collectionToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Movie m : movieList) {
            jsonArray.put(m.toJson());
        }

        return jsonArray;
    }
}
