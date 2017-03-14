package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.BDBug;
import inf101.v17.boulderdash.bdobjects.IBDObject;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BugTest {

    private BDMap map;

    @Before
    public void setup() {
    }

    /**
     * Test to see if a un blocked monster does move
     */
    @Test
    public void bugMoves() {
        IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
        grid.set(2, 2, 'b');
        map = new BDMap(grid);

        // find the bug
        Position bugPos = new Position(2, 2);
        IBDObject bug = map.get(bugPos);
        assertTrue(bug instanceof BDBug);

        for (int i = 0; i < 100; i++) {
            map.step();
            if (map.get(bugPos) != bug) { // bug has moved
                // reported position should be different
                assertNotEquals(bugPos, map.getPosition(bug));
                // bug moved –  we're done
                return;
            }

        }

        fail("Bug should have moved in 100 steps!");
    }

    /**
     * Test to see if a blocked monster is unable to move
     */
    @Test
    public void bugLocked() {
        IGrid<Character> grid = new MyGrid<>(3, 3, '*');
        grid.set(1, 1, 'b');
        map = new BDMap(grid);

        // find the bug
        Position bugPos = new Position(1, 1);
        IBDObject bug = map.get(bugPos);
        assertTrue(bug instanceof BDBug);

        for (int i = 0; i < 100; i++) {
            map.step();
            if (map.get(bugPos) != bug) { // bug has moved
                // reported position should be different
                fail("Bug should NOT have moved!");
            }
        }

        return;
    }

    /**
     * Test to see if a bug moves as expected (WEST, NORTH, EAST, SOUTH)
     */
    @Test
    public void bugExpectedMove() {
        IGrid<Character> grid = new MyGrid<>(5, 5, ' ');
        grid.set(2, 2, 'b');
        map = new BDMap(grid);

        // find the bug
        Position bugPos = new Position(2, 2);
        IBDObject bug = map.get(bugPos);
        assertTrue(bug instanceof BDBug);

        Position expected = new Position(2, 2);
        Main:
        for (int p = 1; p <= 4; p++)
            for (int i = 0; i < 100; i++) {
                map.step();
                if (!map.getPosition(bug).equals(expected)) {
                    //Bug moved once, check if right move
                    System.out.println("Moved");
                    switch (p) {
                        case 1:
                            expected = new Position(1, 2);
                            break;
                        case 2:
                            expected = new Position(1, 1);
                            break;
                        case 3:
                            expected = new Position(2, 1);
                            break;
                        case 4:
                            expected = new Position(2, 2);
                            break;
                    }
                    if (!map.getPosition(bug).equals(expected))
                        fail("Monster not in correct spot, found in: " + map.getPosition(bug));
                    else
                        System.out.println("passed");
                        continue Main;
                }
            }


    }
}
