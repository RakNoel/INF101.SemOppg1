package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.BDBug;
import inf101.v17.boulderdash.bdobjects.BDPlayer;
import inf101.v17.boulderdash.bdobjects.IBDObject;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test-class to test the BDBug objects behave the way they are supposed to on map-updates
 *
 * @author Anya
 * @version 1.0
 * @since 27.03.2017
 */
public class BugTest {

    private BDMap map;

    /**
     * Test to see if a unblocked monster does move at all
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
        IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
        grid.set(1, 1, 'b');
        map = new BDMap(grid);

        // find the bug
        Position bugPos = new Position(1, 1);
        IBDObject bug = map.get(bugPos);
        assertTrue(bug instanceof BDBug);

        Position expected = new Position(1, 1);
        Main:
        for (int p = 1; p <= 4; p++)
            for (int i = 0; i < 100; i++) {
                map.step();
                if (!map.getPosition(bug).equals(expected)) {
                    //Bug moved once, check if right move
                    System.out.println("Moved");
                    switch (p) {
                        case 1:
                            expected = new Position(0, 1);
                            break;
                        case 2:
                            expected = new Position(0, 2);
                            break;
                        case 3:
                            expected = new Position(1, 2);
                            break;
                        case 4:
                            expected = new Position(1, 1);
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

    /**
     * Test to test if bug kills player if walked over
     */
    @Test
    public void playerKilledByBug() {
        IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
        grid.set(1, 1, 'b'); //Bug
        grid.set(0, 2, 'p'); //Player
        map = new BDMap(grid);

        //Test if both player and bug is created and in grid at right pos
        Position bugPos = new Position(1, 1);
        Position plyPos = new Position(0, 2);
        IBDObject bug = map.get(bugPos);
        IBDObject ply = map.get(plyPos);
        assertTrue(bug instanceof BDBug);
        assertTrue(ply instanceof BDPlayer);

        for (int i = 0; i < 200; i++) {
            map.step();
            if (!(map.get(plyPos) instanceof BDPlayer && !(map.get(plyPos) instanceof BDBug))) {
                // player is dead
                return;
            }
        }

        fail("Player has not been killed within 200 steps");
    }

    /**
     * Test to see if bug is unable to kill the player if blocked by wall
     */
    @Test
    public void playerNotKilledWall() {
        IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
        grid.set(1, 1, 'b'); //Bug
        grid.set(0, 2, 'p'); //Player
        grid.set(0, 1, '*'); //Wall
        map = new BDMap(grid);

        //Test if both player and bug is created and in grid at right pos
        Position bugPos = new Position(1, 1);
        Position plyPos = new Position(0, 2);
        IBDObject bug = map.get(bugPos);
        IBDObject ply = map.get(plyPos);
        assertTrue(bug instanceof BDBug);
        assertTrue(ply instanceof BDPlayer);

        for (int i = 0; i < 200; i++) {
            map.step();
            assertTrue(map.getPlayer().isAlive());
        }
    }
}
