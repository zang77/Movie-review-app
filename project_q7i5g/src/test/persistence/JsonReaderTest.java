package persistence;

import model.Movie;
import model.MovieCollection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MovieCollection mc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCollection.json");
        try {
            MovieCollection mc = reader.read();
            assertEquals(0, mc.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCollection() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCollection.json");
        try {
            MovieCollection mc = reader.read();
            List<Movie> movies = mc.viewAllMovies();
            assertEquals(2, movies.size());
            checkMovie("One", 1, "Bad movie", movies.get(0));
            checkMovie("Big bang", 3, "Not bad", movies.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}