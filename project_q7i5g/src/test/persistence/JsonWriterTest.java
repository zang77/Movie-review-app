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
public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            MovieCollection mc = new MovieCollection();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCollection() {
        try {
            MovieCollection mc = new MovieCollection();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCollection.json");
            writer.open();
            writer.write(mc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCollection.json");
            mc = reader.read();
            assertEquals(0, mc.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCollection() {
        try {
            MovieCollection mc = new MovieCollection();
            mc.addMovie(new Movie("One", 1, "Bad movie"));
            mc.addMovie(new Movie("Big bang", 3, "Not bad"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCollection.json");
            writer.open();
            writer.write(mc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCollection.json");
            mc = reader.read();
            List<Movie> movies = mc.viewAllMovies();
            assertEquals(2, mc.getSize());
            checkMovie("One", 1, "Bad movie", movies.get(0));
            checkMovie("Big bang", 3, "Not bad", movies.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
