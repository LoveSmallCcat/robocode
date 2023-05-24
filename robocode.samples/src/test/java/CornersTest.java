import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Corners;

public class CornersTest {
    private static Corners corners;

    @Before
    public void setUp() {
        corners = new Corners();
    }
    @Test
    public void runTest() {


    }

    @Test
    public void testGoCorner() {
        // Call the goCorner() method


        // Assert that the robot is positioned at the desired corner
        // Note: You need to implement the assertions based on the behavior of the goCorner() method
        // For example:
        Assert.assertEquals(0, corners.getX());
        Assert.assertEquals(0, corners.getY());

        // Assert that the robot is facing the corner
        // For example:


        // Assert any other relevant conditions
    }

    @Test
    public void testSmartFire() {
        // Call the smartFire() method with different distances
        corners.smartFire(100);
        corners.smartFire(150);
        corners.smartFire(250);

        // Assert that the firepower is set correctly based on the distance
        // Note: You need to implement the assertions based on the behavior of the smartFire() method
        // For example:


        // Assert any other relevant conditions
    }

}
