package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.bdobjects.BDAIPlayer;
import inf101.v17.boulderdash.bdobjects.BDDiamond;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.boulderdash.maps.MapReader;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A test class to test if the AI does complete the maps or not.
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.2017
 */
public class AIPlayerTest {

    private BDMap map;

    /**
     * Tests if the AI completes a very standard grid with few obstacles
     */
    @Test
    public void basicRouteSolve() {
        IGrid<Character> grid = new MyGrid<>(7, 6, ' ');
        grid.set(1, 4, 'A');
        grid.set(5, 1, 'q');

        map = new BDMap(grid);

        for (int i = 0; i < 45; i++)
            map.step();

        assertTrue(map.getFinished());
        assertFalse(map.get(1, 4) instanceof BDAIPlayer);
    }

    /**
     * Test if the AI does solve a more advanced route with more obstacles
     */
    @Test
    public void advancedRouteSolve() {
        IGrid<Character> grid = new MyGrid<>(7, 6, ' ');
        grid.set(1, 4, 'A');
        grid.set(2, 2, '*');
        grid.set(2, 1, '*');
        grid.set(3, 4, '*');
        grid.set(4, 3, '*');
        grid.set(5, 1, 'q');

        map = new BDMap(grid);

        for (int i = 0; i < 45; i++)
            map.step();

        assertTrue(map.getFinished());
        assertFalse(map.get(1, 4) instanceof BDAIPlayer);
    }

    /**
     * Test to see if the AI does pick up all diamonds before completing the map
     */
    @Test
    public void takesDiamsFirst() {
        IGrid<Character> grid = new MyGrid<>(7, 6, ' ');
        grid.set(1, 4, 'A');
        grid.set(2, 2, '*');
        grid.set(2, 1, '*');
        grid.set(3, 4, '*');
        grid.set(4, 3, '*');
        grid.set(5, 1, 'q');
        grid.set(1, 1, 'd');
        grid.set(4, 4, 'd');

        map = new BDMap(grid);

        for (int i = 0; i < 45; i++)
            map.step();

        assertTrue(map.getFinished());
        assertFalse(map.get(1, 4) instanceof BDAIPlayer);
        assertFalse(map.get(1, 1) instanceof BDDiamond);
        assertFalse(map.get(4, 4) instanceof BDDiamond);
    }

    /**
     * Test to see if the player completes all test-maps 100% of the time
     */
    @Test
    public void completesTestMaps() {
        for (int i = 0; i < 30; i++) {
            testMaps(1);
            testMaps(2);
        }
    }
    private void testMaps(int j) {
        MapReader reader = new MapReader("resources/testLevels/testMap" + j + ".txt");
        IGrid<Character> rawGrid = reader.read();

        map = new BDMap(rawGrid);
        assertFalse(map.getFinished());

        for (int i = 0; i < 3000; i++)
            map.step();

        assertTrue(map.getFinished());
    }
}
