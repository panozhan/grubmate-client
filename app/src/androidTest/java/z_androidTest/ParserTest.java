package z_androidTest;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.util.ArrayList;

import objects.Notification;
import objects.Parser;
import objects.Post;

import static org.junit.Assert.assertEquals;

/**
 * Created by Alex Pan on 10/31/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ParserTest {

    @Before
    public void init() {

    }

    /**
     * Test that removing a group should make it disappear from list of groups
     */
    Parser parser = new Parser();
    @Test
    public void parseStrings_isCorrect() throws Exception {
        String jsonString = "[\"1\",\"2\",\"3\",\"4\"]";
        InputStream stream = new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
        ArrayList<String> list = parser.parseStringArrayJson(stream);
        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "2");
        assertEquals(list.get(2), "3");
        assertEquals(list.get(3), "4");
    }

    @Test
    public void parsePost_isCorrect() throws Exception {
        String jsonString = "{\"_id\":\"3\",\"title\":\"asdf\",\"tag\":\"sdfew\",\"location\":\"abc\",\"price\":\"$10\"}";
        InputStream stream = new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
        Post post = parser.parsePost(stream);
        assertEquals(post.get_id(), "3");
        assertEquals(post.getTag(), "sdfew");
        assertEquals(post.getTitle(), "asdf");
        assertEquals(post.getLocation(), "abc");
        assertEquals(post.getPrice(), "$10");

    }

    @Test
    public void parseNotification_isCorrect() throws Exception {
        String jsonString = "{\"type\":\"status\",\"address\":\"asdf\",\"title\":\"sdfew\",\"postid\":\"abc\",\"status\":\"$10\"}";
        InputStream stream = new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
        Notification notification = parser.parseNotification(stream);
        assertEquals(notification.getType(), "status");
        assertEquals(notification.getTitle(), "sdfew");
        assertEquals(notification.getAddress(), "asdf");
        assertEquals(notification.getPostId(), "abc");
        assertEquals(notification.getStatus(), "$10");

    }

}
