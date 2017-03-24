package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import inf101.v17.boulderdash.bdobjects.*;
import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

public class FallingTest {

	private BDMap map;

	@Before
	public void setup() {
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'r');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
	}

	@Test
	public void fallingTestCheckFall() {
		checkFall(new Position(0, 4));
	}
	
	@Test
	public void fallingKills() {
		fallingKills('d');
		fallingKills('r');
	}
	public void fallingKills(char test) {
		// diamond two tiles above kills player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, test);
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);


		checkFall(new Position(0, 4));
		checkFall(new Position(0, 3));
		checkFall(new Position(0, 2));
		assertFalse(map.getPlayer().isAlive());
	}

	@Test
	public void restingDoesntKill(){
		restingDoesntKill('d');
		restingDoesntKill('r');
	}
	private void restingDoesntKill(char test) {
		// diamond on top of player doesn't kill player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 3, test);
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);

		for (int i = 0; i < 100; i++)
			map.step();

		assertTrue(map.getPlayer().isAlive());
	}

	@Test
	public void fallingTest() {
		fallingTest('d');
		fallingTest('r');
	}
	public void fallingTest(char test) {
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, test);
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		IBDObject obj = map.get(0, 4);
		assertFalse(obj instanceof BDEmpty);

		// four steps later, we've fallen down one step
		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 3));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 2));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 1));

		// wall reached, no more falling
		for (int i = 0; i < 10; i++)
			map.step();
		assertEquals(obj, map.get(0, 1));
	}

	protected Position checkFall(Position pos) {
		IBDObject obj = map.get(pos);
		if (obj instanceof AbstractBDFallingObject) {
			Position next = pos.moveDirection(Direction.SOUTH);
			if (map.canGo(next)) {
				IBDObject target = map.get(next);
				if (target.isEmpty() || target.isKillable()) {
				} else {
					next = pos;
				}
			} else {
				next = pos;
			}

			//map.step(); System.out.println(map.getPosition(object));
			map.step();
			map.step();
			map.step();
			map.step();
			assertEquals(obj, map.get(next));
			return next;
		} else
			return pos;
	}

	@Test
	public void pushTest(){
		IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
		grid.set(0, 4, 'r');
		grid.set(2, 4, 'r');
		grid.set(0, 3, '#');
		grid.set(2, 3, '#');

		grid.set(1, 1, 'p');
		grid.set(1, 0, '#');
		map = new BDMap(grid);

		assertTrue(map.get(0,4) instanceof BDRock);
		assertTrue(map.get(2,4) instanceof BDRock);
		assertTrue(map.get(1,1) instanceof BDPlayer);

		BDRock bdRock2 = (BDRock) map.get(0,4);
		BDRock bdRock1 = (BDRock) map.get(2,4);

		bdRock1.push(Direction.WEST);
		for (int i = 0; i < 16; i++)
			map.step();
		assertFalse(map.getPlayer().isAlive());
		assertTrue(map.get(1,1) instanceof BDRock);

		bdRock2.push(Direction.EAST);
		for (int i = 0; i < 16; i++)
			map.step();

		assertFalse(map.get(0,4) instanceof BDRock);
	}

	@Test
	public void pushWall(){
		IGrid<Character> grid = new MyGrid<>(3, 5, ' ');
		grid.set(0, 4, 'r');
		grid.set(2, 4, 'r');
		grid.set(0, 3, '#');
		grid.set(2, 3, '#');

		grid.set(1, 1, 'p');
		grid.set(1, 0, '#');
		map = new BDMap(grid);

		assertTrue(map.get(0,4) instanceof BDRock);
		assertTrue(map.get(2,4) instanceof BDRock);
		assertTrue(map.get(1,1) instanceof BDPlayer);

		BDRock bdRock1 = (BDRock) map.get(2,4);

		assertFalse(bdRock1.push(Direction.EAST));
		for (int i = 0; i < 16; i++)
			map.step();
		assertTrue(map.getPlayer().isAlive());
		assertFalse(map.get(1,1) instanceof BDRock);
	}

	@Test
	public void fallDoesntBreakGround(){
		fallDoesntBreakGround('r');
		fallDoesntBreakGround('d');
	}
	public void fallDoesntBreakGround(char test){
		IGrid<Character> grid = new MyGrid<>(1, 5, ' ');
		grid.set(0, 4, test);
		map = new BDMap(grid);

		assertFalse(map.get(0,4) instanceof BDEmpty);

		for (int i = 0; i < 100; i++)
			map.step();

		assertFalse(map.get(0,0) instanceof BDEmpty);
	}

	@Test
	public void fancyFall(){
		fancyFall('d');
		fancyFall('r');
	}
	private void fancyFall(char test){
		IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
		grid.set(1, 1, test);
		grid.set(1, 0, '*');
		map = new BDMap(grid);

		for (int i = 0; i < 100; i++)
			map.step();

		assertFalse(map.get(0,1) instanceof AbstractBDFallingObject);
		assertTrue(map.get(0,0) instanceof AbstractBDFallingObject
				|| map.get(2,0) instanceof AbstractBDFallingObject);

	}
}
