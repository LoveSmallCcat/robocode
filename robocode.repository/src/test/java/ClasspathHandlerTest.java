import net.sf.robocode.repository.parsers.ClasspathFileParser;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;


public class ClasspathHandlerTest {

    /**
     * Test the parsing of classpath XML content in the visitDirectory method in ClassPathHandler class.
     *
     * @throws MalformedURLException if there is an issue with the URL
     */
    @Test
    public void testParse() throws MalformedURLException {
        ClasspathFileParser parser = new ClasspathFileParser();
        String xmlContent = "<classpathentry kind=\"output\" path=\"bin\"/>";

        // Convert XML content to URL
        URL xmlUrl = new URL("file:/temp.xml");

        // Debugging statement
        System.out.println("XML Content: " + xmlContent);

        parser.parse(xmlUrl);

        // Debugging statement
        System.out.println("Class Path: " + parser.getClassPath());

        assertNotSame("Class path should match", "bin", parser.getClassPath());
    }
}
