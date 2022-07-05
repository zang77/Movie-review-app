package persistence;

import model.Movie;
import model.MovieCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads movie collection from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MovieCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMovieCollection(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private MovieCollection parseMovieCollection(JSONObject jsonObject) {
        MovieCollection mc = new MovieCollection();
        addMovies(mc, jsonObject);
        return mc;
    }

    // MODIFIES: mc
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addMovies(MovieCollection mc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("favorite");
        for (Object json : jsonArray) {
            JSONObject nextMovie = (JSONObject) json;
            addMovie(mc, nextMovie);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addMovie(MovieCollection mc, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int rating = jsonObject.getInt("rating");
        String review = jsonObject.getString("review");
        Movie movie = new Movie(name, rating, review);
        mc.addMovie(movie);
    }
}
