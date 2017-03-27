package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.bdobjects.*;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test-class to see if the slime behave in the right/expected manner.
 *
 * @author RakNoel
 * @version 1.0
 * @since 22.03.2017
 */
public class SlimeTest {

    private BDMap map;

    /**
     * Test if a BDSlimeDrop does fall
     */
    @Test
    public void dropFalls() {
        IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
        grid.set(1, 4, 's');
        grid.set(1, 0, '*');
        map = new BDMap(grid);

        for (int i = 0; i < 100; i++) {
            map.step();

            if (map.get(1, 4) instanceof BDEmpty) {
                assertTrue(map.get(1, 3) instanceof AbstractBDFallingObject);
                return;
            }
        }

        fail("Drop never moved");

    }

    /**
     * Tests if the drop does indeed kill
     */
    @Test
    public void dropKills() {
        dropKills('b');
        dropKills('p');
    }

    private void dropKills(char killable) {
        IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
        grid.set(1, 4, 's');
        grid.set(1, 1, killable);
        grid.set(1, 0, '*');
        grid.set(0, 1, '*');
        grid.set(2, 1, '*');
        map = new BDMap(grid);

        for (int i = 0; i < 100; i++)
            map.step();

        assertFalse(map.get(1, 1) instanceof IBDKillable);
    }

    /**
     * Test if the drop falls and disappears
     */
    @Test
    public void dropDissapears() {
        IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
        grid.set(1, 4, 's');
        grid.set(1, 0, '*');
        map = new BDMap(grid);

        for (int i = 0; i < 100; i++)
            map.step();

        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++)
                assertFalse(map.get(x, y) instanceof AbstractBDFallingObject);
    }

    /**
     * Tests if the drop tries to leave the grid if open to do so
     */
    @Test
    public void dropDoesNotLeaveGrid() {
        IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
        grid.set(1, 4, 's');
        map = new BDMap(grid);

        for (int i = 0; i < 100; i++)
            map.step();
    }

    /**
     * Tests if the spawner kills by spawning a drop that kills
     */
    @Test
    public void spawnerKills() {
        spawnerKills('p');
        spawnerKills('b');
    }

    private void spawnerKills(char Killable) {
        IGrid<Character> grid = new MyGrid<>(3, 6, ' ');
        grid.set(1, 4, '*');
        grid.set(1, 4, 'S');
        grid.set(1, 3, ' ');
        grid.set(1, 2, ' ');
        grid.set(1, 1, Killable);
        grid.set(1, 0, '*');
        grid.set(0, 1, '*');
        map = new BDMap(grid);

        //assertTrue(map.get(1,4) instanceof BDSlimeSpawn);

        for (int i = 0; i < 100; i++)
            map.step();

        assertFalse(map.get(1, 1) instanceof IBDKillable);
    }

    /**
     * Test if the the spawner is able to spawn a drop properly if space for it
     */
    @Test
    public void spawnerSpawnsDrop() {
        IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
        grid.set(1, 4, '*');
        grid.set(1, 3, 'S');
        map = new BDMap(grid);

        assertTrue(map.get(1, 3) instanceof BDSlimeSpawn);

        for (int i = 0; i < 100; i++) {
            map.step();

            if (map.get(1, 1) instanceof BDSlimeDrop)
                return;
        }

        fail("Never found a single slimedrop in 100 turns");

    }

    /**
     * Test if the drop needs a roof by changing tile above to BDEmpty tile above
     */
    @Test
    public void spawnerNeedsRoof() {
        IGrid<Character> grid = new MyGrid<>(8, 8, ' ');
        grid.set(4, 6, '*');
        grid.set(4, 5, 'S');
        grid.set(3, 2, '*');
        map = new BDMap(grid);

        for (int i = 0; i < 100; i++)
            map.step();

        //test if it is still here
        assertTrue(map.get(4, 5) instanceof BDSlimeSpawn);

        //Remove roof
        map.set(4, 6, new BDEmpty(map));

        //Test if it is removed
        for (int i = 0; i < 100; i++)
            map.step();

        assertFalse(map.get(4, 5) instanceof BDSlimeSpawn);
    }

    /**
     * Tests if spawner also kills what is right underneath if able to.
     */
    @Test
    public void spawnerKillsUnder() {
        spawnerKillsUnder('b');
        spawnerKillsUnder('p');
    }

    private void spawnerKillsUnder(char killable) {
        IGrid<Character> grid = new MyGrid<>(8, 8, ' ');
        grid.set(4, 3, 'S');
        grid.set(4, 2, killable);
        grid.set(3, 2, '*');
        map = new BDMap(grid);

        assertTrue(map.get(4, 3) instanceof BDSlimeSpawn);
        assertTrue(map.get(4, 2) instanceof IBDKillable);

        for (int i = 0; i < 200; i++)
            map.step();

        assertFalse(map.get(4, 2) instanceof IBDKillable);
    }
}
