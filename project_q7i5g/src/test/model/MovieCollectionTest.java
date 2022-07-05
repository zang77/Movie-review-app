package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MovieCollectionTest {
    private MovieCollection testCollection;
    Movie movie1 = new Movie("Tytanic", 5, "Great");
    Movie movie2 = new Movie("COCO", 4, "Great but not perfect");

    @BeforeEach
    void runBefore(){
        testCollection = new MovieCollection();
    }

    @Test
    void testAddMovie() {
        assertTrue(testCollection.isEmpty());
        testCollection.addMovie(movie1);
        assertEquals(testCollection.getSize(), 1);
        assertFalse(testCollection.isEmpty());
    }

    @Test
    void testRemoveMovie() {
        testCollection.addMovie(movie1);
        testCollection.addMovie(movie2);
        testCollection.removeMovie(movie1);
        assertEquals(testCollection.getSize(), 1);
    }

    @Test
    void testViewAll() {
        testCollection.addMovie(movie1);
        testCollection.addMovie(movie2);
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Tytanic", "COCO"));
        assertEquals(testCollection.viewAllNames(), names);
    }

    @Test
    void testViewAllMovies() {
        assertTrue(testCollection.isEmpty());
        testCollection.addMovie(movie1);
        ArrayList<Movie> newList = new ArrayList<>();
        newList.add(movie1);
        assertEquals(testCollection.viewAllMovies(), newList);
    }
}



