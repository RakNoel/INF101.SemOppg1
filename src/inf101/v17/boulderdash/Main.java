package inf101.v17.boulderdash;

import inf101.v17.boulderdash.gui.BoulderDashGUI;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.boulderdash.maps.MapReader;
import inf101.v17.datastructures.IGrid;

/**
 * Contains the main method to execute the program.
 *
 */
public class Main {

	public static void main(String[] args) throws Exception{
		// This is how you set up the program, change the file path accordingly.
		MapReader reader = new MapReader("resources/testLevels/testMap2.txt");
		IGrid<Character> rawGrid = reader.read();
		BDMap map = new BDMap(rawGrid);
		map.addBackground("../../../../sprites/background/back_cave.png");
		BoulderDashGUI.run(map);
	}

	//TODO: Add fancy rocks
	//TODO: Add music!
	//TODO: Add sound!
	//TODO: Add more levels
	//TODO: Add Level creator?
	//TODO: Add spikes

}
