package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.input.KeyCode;

import java.util.*;

//Created by RakNoel, 23.03.2017.
public class BDAIPlayer extends BDPlayer implements IBDKillable {

    private ArrayList<Position> gotoPositions;
    private ArrayList<Position> danger;
    private ArrayList<Position> usedNodes;
    private Queue<Position> processingQueue;
    private HashMap<Position, Position> pointerMap;
    private int wait = 2;
    private boolean notFoundPath;

    public BDAIPlayer(BDMap owner) {
        super(owner);

        usedNodes = new ArrayList<>();
        pointerMap = new HashMap<>();
        processingQueue = new LinkedList<>();
        gotoPositions = new ArrayList<>();
        danger = new ArrayList<>();
        notFoundPath = false;
    }

    /**
     * TLDR;
     * A heavy breadth first search algorithm that finds the first functional way to the nearest objective goal set,
     * and adds it to the this.gotoPositions arrayList as a row of coordinates from null to goal. This algorithm will also
     * avoid spending too long in dangerous tiles.
     * <p>
     * Explanation:
     * This will find ALL possible routes that comes out in the breadth of possible outcomes and follows all nodes in
     * a Queue. If we can go here, add to Queue, mark node as used, and make pointer in a hashMap; else discard.
     * Now finally we will find the goal, so we retrace from final position with hashMap into the
     * gotoPositions ArrayList and turn it backwards.
     *
     * @param obj The objective type you want to find a path to.
     */
    private void findPath(IBDObject obj) {

        //Clear all the arrays so we don't go in circles
        pointerMap.clear();
        processingQueue.clear();
        gotoPositions.clear();
        usedNodes.clear();

        //Get map, add nullPos to Queue and hashmap!
        BDMap bdmap = super.getMap();
        processingQueue.add(this.getPosition());
        pointerMap.put(this.getPosition(), null);

        while (!processingQueue.isEmpty()) {
            Position pos = processingQueue.remove();

            if (bdmap.get(pos).getClass().equals(obj.getClass())) {
                //DONE, now retrace path
                while (pointerMap.get(pos) != null) {
                    gotoPositions.add(pos);
                    pos = pointerMap.get(pos);
                }
                //then reverse array
                Collections.reverse(gotoPositions);
                break;
            }

            //Array with possible test positions
            ArrayList<Position> tests = new ArrayList<>();
            tests.add(pos.moveDirection(Direction.EAST));
            tests.add(pos.moveDirection(Direction.SOUTH));
            tests.add(pos.moveDirection(Direction.WEST));
            tests.add(pos.moveDirection(Direction.NORTH));

            for (int i = 0; i < tests.size(); i++) {
                try {
                    Position thisTest = tests.get(i);
                    if (!usedNodes.contains(thisTest)) {
                        //Add to que if possible route
                        if (bdmap.get(thisTest) instanceof BDEmpty
                                || bdmap.get(thisTest) instanceof BDSand
                                || bdmap.get(thisTest).getClass().equals(obj.getClass())) {
                            //Blocks up/down movement in dangerous tiles
                            if (!danger.contains(pos) || (danger.contains(pos) && i % 2 == 0) || notFoundPath) {
                                processingQueue.add(thisTest);
                                pointerMap.put(thisTest, pos);
                                usedNodes.add(thisTest);
                            } else {
                                //Blocked by danger
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }
            }
        }
        notFoundPath = (gotoPositions.size() == 0);
        if (notFoundPath)
            findPath(obj);
    }

    private int countDiams() {
        int dCount = 0;
        BDMap map = super.getMap();

        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++)
                if (map.get(x, y) instanceof BDDiamond)
                    dCount++;

        return dCount;
    }

    private void markDanger() {
        danger.clear();

        BDMap bdmap = super.getMap();

        for (int x = 0; x < bdmap.getWidth(); x++)
            for (int y = 0; y < bdmap.getHeight(); y++) {
                Position thisTest = new Position(x, y);

                try {
                    //Also add to dangerous-list IF fallingobj above
                    if (!danger.contains(thisTest) &&
                            bdmap.get(thisTest.moveDirection(Direction.NORTH))
                                    instanceof AbstractBDFallingObject) {
                        danger.add(thisTest);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }

                try {
                    //Else add to danger if Bug
                    if (!danger.contains(thisTest)
                            && bdmap.get(thisTest) instanceof BDBug) {
                        danger.add(thisTest);
                        danger.add(thisTest.moveDirection(Direction.NORTH));
                        danger.add(thisTest.moveDirection(Direction.WEST));
                        danger.add(thisTest.moveDirection(Direction.WEST).moveDirection(Direction.NORTH));
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }

                try {
                    //Else if slime, then add
                    if (!danger.contains(thisTest)
                            && bdmap.get(thisTest) instanceof BDSlimeSpawn) {
                        IBDObject obj = bdmap.get(x, y - 1);

                        try {
                            while (!(obj instanceof BDWall)) {
                                danger.add(new Position(x, y));
                                obj = bdmap.get(x, --y);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            //Ignore if out of map grab
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }
            }
    }

    @Override
    public void keyPressed(KeyCode key) {
        //NOPE! IGNORE
    }

    @Override
    public void step() {
        if (--wait <= 0) {

            //Find dangerous tiles
            if (danger.size() == 0)
                markDanger();

            //See if there are instructions
            if (gotoPositions.size() > 0) {
                Position rPos = gotoPositions.get(0);
                int x = rPos.getX();
                int y = rPos.getY();

                try {
                    Position nPos = gotoPositions.get(1);
                    if (danger.contains(rPos) && danger.contains(nPos)) {
                        if (owner.get(rPos.getX(), rPos.getY()) instanceof AbstractBDKillingObject
                                || owner.get(nPos.getX(), nPos.getY()) instanceof AbstractBDKillingObject) {
                            wait = 8;
                            return;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }
                try {
                    if (danger.contains(rPos)) {
                        if (owner.get(rPos.getX(), rPos.getY()) instanceof AbstractBDKillingObject)
                            wait = 4;
                    }
                    if (danger.contains(rPos.moveDirection(Direction.NORTH)))
                        if (owner.get(rPos.getX(), rPos.moveDirection(Direction.NORTH).getY())
                                instanceof AbstractBDKillingObject)
                            wait = 8;
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }


                if (this.getX() < x) {
                    this.askedToGo = Direction.EAST;
                } else if (this.getX() > x) {
                    this.askedToGo = Direction.WEST;
                } else if (this.getY() > y) {
                    this.askedToGo = Direction.SOUTH;
                } else {
                    this.askedToGo = Direction.NORTH;
                }

                this.gotoPositions.remove(0);

                //Now wait so we don't quantom leap!
                wait = 4;
            }

            //Else find new instructions
            else {
                if (countDiams() > 0) {
                    findPath(new BDDiamond(owner));
                } else {
                    findPath(new BDDoor(owner));
                }
            }
        }
        super.step();
    }

}
