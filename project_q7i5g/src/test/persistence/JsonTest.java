package persistence;

import model.Movie;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkMovie(String name, int rating, String review, Movie movie) {
        assertEquals(name, movie.getName());
        assertEquals(rating, movie.getRating());
        assertEquals(review, movie.getReview());
    }
}
