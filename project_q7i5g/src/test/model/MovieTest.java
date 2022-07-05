package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private Movie testMovie;

    @BeforeEach
    void runBefore() {
        testMovie = new Movie("Tytanic", 5, "Touching love story!");
    }


    @Test
    void testConstructor() {
        assertEquals(testMovie.getName(), "Tytanic");
        assertEquals(testMovie.getRating(), 5);
        assertEquals(testMovie.getReview(), "Touching love story!");
    }

    @Test
    void testNewReview() {
        testMovie.newReview("Now I hate it.");
        assertEquals(testMovie.getReview(), "Now I hate it.");
    }

    @Test
    void testNewRating() {
        testMovie.newRating(1);
        assertEquals(testMovie.getRating(), 1);
    }


    @Test
    void testToString() {
        assertEquals("|              Tytanic|  |   5    |  | Touching love story!          |",
                testMovie.toString());
    }
}