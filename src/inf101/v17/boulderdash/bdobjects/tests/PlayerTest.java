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

//Created by RakNoel, 16.03.2017.
public class PlayerTest {
    private BDMap map;

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
