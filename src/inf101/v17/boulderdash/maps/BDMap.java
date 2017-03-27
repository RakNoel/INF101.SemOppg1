package inf101.v17.boulderdash.maps;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.*;
import inf101.v17.boulderdash.gui.SpriteReader;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;
import java.util.*;

/**
 * An implementation of a map
 *
 * @author larsjaffke
 */
public class BDMap {

    /**
     * A bunch of final integers that hold info about my sprites
     */
    private final int
            spriteWidth = 35,
            spriteHeight = 35,
            spriteBuffer = 2,
            totalSprites = 41;

    /**
     * Reads and stores sprites for the textures.
     */
    protected SpriteReader SpriteReader;
    protected ArrayList<Paint> sprites;

    /**
     * Integer that holds the point of the player for this map on finish
     */
    protected int playerPoints = 0;

    /**
     * Boolean to see if map is finished
     */
    protected boolean finished = false;

    /**
     * This maps background
     */
    protected String background;

    /**
     * Stores the data of the map
     */
    protected IGrid<IBDObject> grid;

    /**
     * Stores time
     */
    protected int seconds;

    /**
     * Stores position of each object with their hash
     */
    protected HashMap<IBDObject, Position> hashMap;

    /**
     * A separate reference to the player, since it is accessed quite
     * frequently.
     */
    protected BDPlayer player;

    /**
     * A separate reference to the AI-player if the Playerposition is null
     */
    protected BDPlayer AI;

    /**
     * Main constructor of this class.
     *
     * @param map A grid of characters, where each character represents a type
     *            of {@link IBDObject}: ' ' - {@link #BDEmpty}, '*' -
     *            {@link BDWall}, '#' - {@link #BDSand}, 'd' -
     *            {@link #BDDiamond}, 'b' - {@link BDBug}, 'r' -
     *            {@link #BDRock}, 'p' - {@link #BDPlayer}
     */
    public BDMap(IGrid<Character> map) {
        grid = new MyGrid<IBDObject>(map.getWidth(), map.getHeight(), null);
        hashMap = new HashMap<IBDObject, Position>();
        this.player = new BDPlayer(this);
        this.AI = new BDAIPlayer(this);

        this.sprites = new ArrayList<Paint>(totalSprites);
        InputStream reAsStr = getClass().getResourceAsStream("../../../../sprites/textures/textures.png");
        this.SpriteReader = new SpriteReader(reAsStr, spriteHeight, spriteWidth, spriteBuffer);


        for (int y = 0; y < Math.sqrt(totalSprites); y++)
            for (int x = 0; x < Math.sqrt(totalSprites); x++)
                sprites.add(SpriteReader.getSprite(x, y));

        fillGrid(map);
        this.seconds = 8 * Math.min(800, this.getHeight() * this.getWidth());
    }

    /**
     * methos to add a background texture for this map
     * @param background is a string of the filepath to the background
     */
    public void addBackground(String background) {
        if (background != null && !background.equals(""))
            this.background = background;
    }

    /**
     * True of the object can move in direction dir, false otherwise.
     *
     * @param obj
     * @param dir
     * @return
     */
    public boolean canGo(IBDObject obj, Direction dir) {
        return canGo(this.getPosition(obj), dir);
    }

    /**
     * True if an object can move to positio (x, y), false otherwise.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean canGo(int x, int y) {
        if (!isValidPosition(x, y)) {
            return false;
        }
        if (grid.get(x, y) instanceof BDWall) {
            return false;
        }
        return true;
    }

    /**
     * See {@link #canGo(int, int)}
     *
     * @param pos
     * @return
     */
    public boolean canGo(Position pos) {
        return canGo(pos.getX(), pos.getY());
    }

    /**
     * Returns seconds left
     */
    public int getTimeLeft() {
        return this.seconds / 8;
    }

    /**
     * Cf. {@link #canGo(IBDObject, Direction)}
     *
     * @param pos
     * @param dir
     * @return
     */
    public boolean canGo(Position pos, Direction dir) {
        switch (dir) {
            case NORTH:
                return canGo(pos.getX(), pos.getY() + 1);
            case SOUTH:
                return canGo(pos.getX(), pos.getY() - 1);
            case EAST:
                return canGo(pos.getX() + 1, pos.getY());
            case WEST:
                return canGo(pos.getX() - 1, pos.getY());
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method is used to initialize the map with a given map of charaters,
     * see also {@link #BDMap(IGrid, BDPlayer)}.
     *
     * @param inputmap
     */
    private void fillGrid(IGrid<Character> inputmap) {
        int width = inputmap.getWidth();
        int height = inputmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                IBDObject obj = makeObject(inputmap.get(x, y), x, y);
                grid.set(x, y, obj);
                hashMap.put(obj, new Position(x, y));
            }
        }
    }

    /**
     * Initialize an object according to the given character, see also
     * {@link #BDMap(IGrid, BDPlayer)}.
     *
     * @param c
     * @param x
     * @param y
     * @return
     */
    private IBDObject makeObject(Character c, int x, int y) {
        switch (c) {
            case 'p':
                return this.player;
            case '*':
                return new BDWall(this);
            case '#':
                return new BDSand(this);
            case ' ':
                return new BDEmpty(this);
            case 'd':
                return new BDDiamond(this);
            case 'r':
                return new BDRock(this);
            case 'q':
                return new BDDoor(this);
            case 'S':
                return new BDSlimeSpawn(this);
            case 's':
                return new BDSlimeDrop(this);
            case 'A':
                return this.AI;
            case 'b':
                try {
                    return new BDBug(this, new Position(x, y), 1, 10);
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                }
            default:
                System.err.println("Illegal character in map definition at (" + x + ", " + y + "): '" + c + "'");
                return new BDEmpty(this);
        }
        // alternatively, throw an exception
        // throw new IllegalArgumentException();
    }

    /**
     * get the object in (x, y)
     *
     * @param x
     * @param y
     * @return
     */
    public IBDObject get(int x, int y) {
        return grid.get(x, y);
    }

    /**
     * see {@link #get(int, int)}
     *
     * @param pos
     * @return
     */
    public IBDObject get(Position pos) {
        return grid.get(pos.getX(), pos.getY());
    }

    /**
     * Returns the height of this map.
     *
     * @return
     */
    public int getHeight() {
        return grid.getHeight();
    }

    /**
     * @return the point the player got
     */
    public int getPlayerPoints() {
        return this.playerPoints;
    }

    /**
     * Given a position pos, this method computes the nearest n empty fields.
     * This method is used when a bug dies and had to spread a certain number of
     * diamonds in neighboring fields.
     *
     * @param pos
     * @param n
     * @return
     */
    public List<Position> getNearestEmpty(Position pos, int n) {
        List<Position> empty = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (get(x, y) instanceof BDEmpty) {
                    empty.add(new Position(x, y));
                }
            }
        }
        if (empty.size() < n) {
            throw new IllegalArgumentException("There are less than " + n + " empty fields on this map");
        }
        Collections.sort(empty, new Comparator<Position>() {

            @Override
            public int compare(Position o1, Position o2) {
                return pos.distanceTo(o1) - pos.distanceTo(o2);
            }
        });
        List<Position> nearby = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            nearby.add(empty.get(i));
        }
        return nearby;
    }

    /**
     * Get the player in this map.
     *
     * @return
     */
    public BDPlayer getPlayer() {
        if (this.player.getPosition() == null)
            return this.AI;
        else
            return player;
    }

    /**
     * Get the position of an object in this map
     *
     * @param object
     * @return
     */
    public Position getPosition(IBDObject object) {
        return hashMap.get(object);
    }

    /**
     * get the width of this map.
     *
     * @return
     */
    public int getWidth() {
        return grid.getWidth();
    }

    /**
     * @return if the map is finished
     */
    public boolean getFinished() {
        return this.finished;
    }

    /**
     * Check whether (x, y) is a valid position in this map.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isValidPosition(int x, int y) {
        return x > -1 && x < grid.getWidth() && y > -1 && y < grid.getHeight();
    }

    /**
     * set tile (x, y) to element.
     *
     * @param x
     * @param y
     * @param element
     */
    public void set(int x, int y, IBDObject element) {
        if (!isValidPosition(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        grid.set(x, y, element);
        hashMap.put(element, new Position(x, y));
    }

    /**
     * The logical step method for this map!
     */
    public void step() {
        for (int x = 0; x < this.getWidth(); x++)
            for (int y = 0; y < this.getHeight(); y++)
                this.get(x, y).step();

        //Update seconds counter so that we change ingame timer
        if (this.seconds > 1)
            this.seconds--;
    }

    /**
     * Method to finish map when player walks on door
     *
     * @see BDPlayer
     * @see BDDoor
     *
     * also calculates player points
     */
    public void finish() {
        int monsterLeft = 0,
                diams = this.getPlayer().numberOfDiamonds(),
                timeLeft = this.seconds;

        for (int x = 0; x < this.getWidth(); x++)
            for (int y = 0; y < this.getHeight(); y++)
                if (this.get(x, y) instanceof BDBug)
                    monsterLeft++;

        int points = diams * 100;
        points += timeLeft;
        points -= monsterLeft * 100;

        if (points < 0)
            points = 0;

        this.playerPoints = points;
        this.finished = true;

    }

    /**
     * @return the Paintobject containing the background picture
     */
    public Paint getBackground() {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream(this.background);
            return new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0, 1.0, true);
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

    /**
     * Returns the sprite that is asked for
     * @param x coordinate x
     * @param y coordinate y
     * @return a Paint object of the sprite
     */
    public Paint getSprite(int x, int y) {
        return this.sprites.get((y * 7) + x);
    }

    /**
     * Alternative getsprite
     * @param x position x as if all sprites are read into ArrayList
     * @return a Paint object of the sprite
     */
    public Paint getSprite(int x) {
        return this.sprites.get(x);
    }
}
