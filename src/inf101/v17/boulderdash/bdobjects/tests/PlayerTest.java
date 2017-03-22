package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.bdobjects.BDEmpty;
import inf101.v17.boulderdash.bdobjects.BDPlayer;
import inf101.v17.boulderdash.bdobjects.BDRock;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import javafx.scene.input.KeyCode;
import org.junit.Test;

import static org.junit.Assert.*;

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
        grid.set(3, 2, '*');
        grid.set(2, 2, '*');
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
    public void playerDoesNotEatRock() {
        IGrid<Character> grid = new MyGrid<>(9, 9, ' ');
        grid.set(3, 3, 'r');
        grid.set(2, 3, 'p');
        grid.set(3, 2, '*');
        grid.set(2, 2, 'r');
        grid.set(2, 1, '*');
        grid.set(4, 3, '*');
        map = new BDMap(grid);

        BDPlayer player = map.getPlayer();

        player.keyPressed(KeyCode.RIGHT);
        map.step();
        player.keyPressed(KeyCode.DOWN);
        map.step();
        player.keyPressed(KeyCode.UP);
        map.step();
        player.keyPressed(KeyCode.LEFT);
        map.step();

        for (int i = 0; i < 100; i++)
            map.step();

        assertTrue(map.get(3, 3) instanceof BDRock);
        assertFalse(map.get(3, 3) instanceof BDPlayer);

    }

    @Test
    public void playerCantMoveTrough() {
        playerCantMoveTrough('*');
        playerCantMoveTrough('r');

    }

    private void playerCantMoveTrough(char blockage) {
        //TODO
        fail();
    }

    @Test
    public void playerPicksUpDiamond() {
        //TODO
        fail();
    }

    @Test
    public void playerCantMoveWhenDead() {
        //TODO
        fail();
    }

    @Test
    public void playerWinOnDoor() {
        //TODO
        fail();
    }


}
