package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.bdobjects.BDEmpty;
import inf101.v17.boulderdash.bdobjects.BDPlayer;
import inf101.v17.boulderdash.bdobjects.BDRock;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import javafx.scene.input.KeyCode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test to see if the player is able to move in the right/expected manner
 *
 * @author RakNoel
 * @version 1.0
 * @since 16.03.2017
 */

public class PlayerTest {
    private BDMap map;

    /**
     * This method tests if the player is able to move in all directions if the Keycode is sent
     */
    @Test
    public void basicPlayerMovement() {
        IGrid<Character> grid = new MyGrid<>(9, 9, ' ');
        grid.set(1, 0, 'p');
        map = new BDMap(grid);

        assertTrue(map.get(1, 0) instanceof BDPlayer);

        BDPlayer player = map.getPlayer();

        player.keyPressed(KeyCode.LEFT);
        map.step();
        assertTrue(map.get(0, 0) instanceof BDPlayer);
        assertTrue(map.get(1, 0) instanceof BDEmpty);

        player.keyPressed(KeyCode.UP);
        map.step();
        assertTrue(map.get(0, 1) instanceof BDPlayer);

        player.keyPressed(KeyCode.RIGHT);
        map.step();
        assertTrue(map.get(1, 1) instanceof BDPlayer);

        player.keyPressed(KeyCode.DOWN);
        map.step();
        assertTrue(map.get(1, 0) instanceof BDPlayer);
    }

    /**
     * Tests to see if the player is unable to leave the grid
     */
    @Test
    public void playerCantLeaveGrid() {
        IGrid<Character> grid = new MyGrid<>(9, 9, ' ');
        grid.set(1, 0, 'p');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        for (int i = 0; i < 100; i++) {
            player.keyPressed(KeyCode.RIGHT);
            map.step();
        }

        assertTrue(map.get(8, 0) instanceof BDPlayer);
    }

    /**
     * Test to check that the player is able to move trough and replaced BDSand with BDEmpty
     */
    @Test
    public void playerDestroysSand() {
        IGrid<Character> grid = new MyGrid<>(9, 9, '#');
        grid.set(1, 0, 'p');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        for (int i = 0; i < 3; i++) {
            player.keyPressed(KeyCode.RIGHT);
            map.step();
        }

        assertTrue(map.get(1, 0) instanceof BDEmpty);
        assertTrue(map.get(2, 0) instanceof BDEmpty);
        assertTrue(map.get(3, 0) instanceof BDEmpty);
        assertTrue(map.get(4, 0) instanceof BDPlayer);
    }

    /**
     * Test to see if the player moves the rock if possible
     */
    @Test
    public void playerMovesRock() {
        IGrid<Character> grid = new MyGrid<>(9, 9, ' ');
        grid.set(3, 3, 'r');
        grid.set(2, 3, 'p');
        grid.set(3, 2, '#');
        grid.set(2, 2, '#');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        player.keyPressed(KeyCode.RIGHT);

        for (int i = 0; i < 100; i++)
            map.step();

        assertFalse(map.get(3, 3) instanceof BDRock);
        assertTrue(map.get(3, 3) instanceof BDPlayer);

        assertTrue(map.get(4, 0) instanceof BDRock);
    }

    /**
     * Test to check if the player is unable to move trough a rock if it is blocked by other object, or unable to
     * walk trough walls
     */
    @Test
    public void playerCantMoveTrough() {
        playerCantMoveTrough('*');
        playerCantMoveTrough('r');
    }
    private void playerCantMoveTrough(char blockage) {
        IGrid<Character> grid = new MyGrid<>(9, 9, blockage);
        grid.set(3, 3, 'p');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        player.keyPressed(KeyCode.RIGHT);
        map.step();
        assertTrue(map.get(3, 3) instanceof BDPlayer);

        player.keyPressed(KeyCode.DOWN);
        map.step();
        assertTrue(map.get(3, 3) instanceof BDPlayer);

        player.keyPressed(KeyCode.UP);
        map.step();
        assertTrue(map.get(3, 3) instanceof BDPlayer);

        player.keyPressed(KeyCode.LEFT);
        map.step();
        assertTrue(map.get(3, 3) instanceof BDPlayer);
    }

    /**
     * Tests if the players diamond-counter rises and if the diamonds disappears if player walks on them.
     */
    @Test
    public void playerPicksUpDiamond() {
        IGrid<Character> grid = new MyGrid<>(9, 9, 'd');
        grid.set(3, 3, 'p');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        player.keyPressed(KeyCode.UP);
        map.step();
        player.keyPressed(KeyCode.UP);
        map.step();

        assertTrue(map.get(3, 5) instanceof BDPlayer);
        assertTrue(player.numberOfDiamonds() == 2);
    }


}
