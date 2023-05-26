import net.sf.robocode.repository.parsers.ClasspathFileParser;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.xml.sax.InputSource;

public class ClasspathFileParserTest {

    private ClasspathFileParser parser;
    private ClasspathFileParser.ClasspathHandler handler;

    @Before
    public void setUp() {
        parser = new ClasspathFileParser();
        handler = parser.classpathHandler;
    }


    /**
     * Tests the parsing of the .classpath file and retrieval of source paths.
     * It asserts that the parsed source paths match the expected values.
     *
     * @throws Exception if an error occurs during parsing or assertion
     */
    @Test
    public void testParseAndGetSourcePaths() throws Exception {
        // Sample XML content to simulate the .classpath file
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<classpath>\n" +
                "    <classpathentry kind=\"src\" path=\"src/main/java\"/>\n" +
                "    <classpathentry kind=\"src\" path=\"src/test/java\"/>\n" +
                "</classpath>";

        // Create an InputStream from the XML content
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());

        // Parse the XML using SAXParser
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(inputStream, handler);

        // Expected source paths
        String[] expectedSourcePaths = {"src/main/java", "src/test/java"};

        // Get the actual source paths from the handler
        String[] actualSourcePaths = handler.sourcePaths.toArray(new String[0]);

        // Assert the source paths match the expected values
        assertArrayEquals("Source paths should match", expectedSourcePaths, actualSourcePaths);
    }


    /**
     * Tests the parsing of the .classpath file and retrieval of the classpath.
     * It asserts that the parsed classpath matches the expected value.
     *
     * @throws Exception if an error occurs during parsing or assertion
     */
    @Test
    public void testParseAndGetClassPath() throws Exception {
        // Sample XML content
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<classpath>\n" +
                "    <classpathentry kind=\"output\" path=\"bin\"/>\n" +
                "</classpath>";

        // Create a temporary file
        File tempFile = createTempFile(xmlContent);

        // Create a parser instance
        ClasspathFileParser parser = new ClasspathFileParser();

        // Parse the temporary file URL
        parser.parse(tempFile.toURI().toURL());

        // Expected classpath
        String expectedClassPath = "bin";

        // Get the actual classpath from the parser
        String actualClassPath = parser.getClassPath();

        // Assert the classpath matches the expected value
        assertEquals(expectedClassPath, actualClassPath);

        // Delete the temporary file
        tempFile.delete();
    }

    // Helper method to create a temporary file with the given content
    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("classpath", ".xml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }



    /**
     * Tests the startElement method of the ClasspathHandler with a 'src' kind.
     * It asserts that the source path is correctly added to the handler's sourcePaths list.
     */
    @Test
    public void testClasspathHandlerStartElementWithSrcKind() {
        Attributes attributes = new MockAttributes("src", "src/main/java");

        try {
            handler.startElement("", "", "classpathentry", attributes);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Source path should match", "src/main/java", handler.sourcePaths.get(0));
    }

    /**
     * Tests the startElement method of the ClasspathHandler with an 'output' kind.
     * It asserts that the output path is correctly set in the handler's outputPath variable.
     */
    @Test
    public void testClasspathHandlerStartElementWithOutputKind() {
        Attributes attributes = new MockAttributes("output", "bin");

        try {
            handler.startElement("", "", "classpathentry", attributes);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Output path should match", "bin", handler.outputPath);
    }

    /**
     * Test case for the getSourcePaths() method.
     * Verifies that the correct source paths are returned from the parser.
     */
    @Test
    public void testGetSourcePaths() {
        // Create a sample ClasspathHandler
        ClasspathFileParser.ClasspathHandler handler = new ClasspathFileParser.ClasspathHandler();
        handler.sourcePaths.add("src/main/java");
        handler.sourcePaths.add("src/test/java");

        // Set the ClasspathHandler in the parser
        parser.setClasspathHandler(handler);

        // Get the source paths from the parser
        String[] sourcePaths = parser.getSourcePaths();

        // Assert that the source paths match the expected values
        assertArrayEquals("Source paths should match", new String[]{"src/main/java", "src/test/java"}, sourcePaths);
    }

    /**
     * Test case for the getClassPath() method.
     * Verifies that the correct class path is returned from the parser.
     */
    @Test
    public void testGetClassPath() {
        // Create a sample ClasspathHandler
        ClasspathFileParser.ClasspathHandler handler = new ClasspathFileParser.ClasspathHandler();
        handler.outputPath = "bin";

        // Set the ClasspathHandler in the parser
        parser.setClasspathHandler(handler);

        // Get the class path from the parser
        String classPath = parser.getClassPath();

        // Assert that the class path matches the expected value
        assertEquals("Class path should match", "bin", classPath);
    }


    /**
     * Test case for the getClasspathHandler() method.
     * Verifies that the correct ClasspathHandler instance is retrieved from the parser.
     */
    @Test
    public void testGetClasspathHandler() {
        // Create a sample ClasspathHandler
        ClasspathFileParser.ClasspathHandler handler = new ClasspathFileParser.ClasspathHandler();

        // Set the ClasspathHandler in the parser
        parser.setClasspathHandler(handler);

        // Get the ClasspathHandler from the parser
        ClasspathFileParser.ClasspathHandler retrievedHandler = parser.getClasspathHandler();

        // Assert that the retrieved handler is the same as the original handler
        assertSame("Retrieved handler should be the same instance", handler, retrievedHandler);
    }


    /**
     * Test case for the setClasspathHandler() method.
     * Verifies that the ClasspathHandler instance is correctly set in the parser.
     */
    @Test
    public void testSetClasspathHandler() {
        // Create a sample ClasspathHandler
        ClasspathFileParser.ClasspathHandler handler = new ClasspathFileParser.ClasspathHandler();

        // Set the ClasspathHandler in the parser
        parser.setClasspathHandler(handler);

        // Get the ClasspathHandler from the parser
        ClasspathFileParser.ClasspathHandler retrievedHandler = parser.getClasspathHandler();

        // Assert that the retrieved handler is the same as the original handler
        assertSame("Retrieved handler should be the same instance", handler, retrievedHandler);
    }

    private static class MockAttributes implements Attributes {

        private final String kind;
        private final String path;

        public MockAttributes(String kind, String path) {
            this.kind = kind;
            this.path = path;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public String getURI(int index) {
            return null;
        }

        @Override
        public String getLocalName(int index) {
            return null;
        }

        @Override
        public String getQName(int index) {
            return null;
        }

        @Override
        public String getType(int index) {
            return null;
        }

        @Override
        public String getValue(int index) {
            return null;
        }

        @Override
        public int getIndex(String uri, String localName) {
            return 0;
        }

        @Override
        public int getIndex(String qName) {
            return 0;
        }

        @Override
        public String getType(String uri, String localName) {
            return null;
        }

        @Override
        public String getType(String qName) {
            return null;
        }

        @Override
        public String getValue(String uri, String localName) {
            return null;
        }

        @Override
        public String getValue(String qName) {
            if ("kind".equals(qName)) {
                return kind;
            } else if ("path".equals(qName)) {
                return path;
            }
            return null;
        }
    }
}