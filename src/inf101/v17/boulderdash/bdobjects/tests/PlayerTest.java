package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.bdobjects.*;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import org.junit.Test;

import static org.junit.Assert.*;

//Created by RakNoel, 16.03.2017.
public class PlayerTest {
    private BDMap map;

    @Test
    public void basicPlayerMovement(){
        IGrid<Character> grid = new MyGrid<>(9, 9, ' ');
        grid.set(1, 0, 'p');
        map = new BDMap(grid);

        assertTrue(map.get(1,0) instanceof BDPlayer);


    }
}
