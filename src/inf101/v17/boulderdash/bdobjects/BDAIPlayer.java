package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.input.KeyCode;

import java.util.*;

//Created by RakNoel, 23.03.2017.
public class BDAIPlayer extends BDPlayer implements IBDKillable {

    private ArrayList<Position> holder;
    private ArrayList<Position> used;
    private Queue<Position> testQueue;
    private HashMap<Position, Position> hashMap;
    private int wait = 2;

    public BDAIPlayer(BDMap owner) {
        super(owner);

        used = new ArrayList<>();
        hashMap = new HashMap<>();
        testQueue = new LinkedList<>();
        holder = new ArrayList<>();
    }

    private void findPath(IBDObject obj) {
        hashMap.clear();
        testQueue.clear();
        holder.clear();
        used.clear();

        BDMap bdmap = super.getMap();
        testQueue.add(this.getPosition());
        hashMap.put(this.getPosition(), null);

        while (!testQueue.isEmpty()) {
            Position pos = testQueue.remove();

            if (bdmap.get(pos).getClass().equals(obj.getClass())) {
                //DONE
                System.out.println("Found goal");
                while (hashMap.get(pos) != null) {
                    holder.add(pos);
                    pos = hashMap.get(pos);
                }
                System.out.println();
                System.out.println("-----------------------------------");
                Collections.reverse(holder);

//                for (Position p : holder)
//                    System.out.println(p);
                break;
            }

            int x = pos.getX();
            int y = pos.getY();

            ArrayList<Position> tests = new ArrayList<>();
            tests.add(new Position(x + 1, y));
            tests.add(new Position(x - 1, y));
            tests.add(new Position(x, y + 1));
            tests.add(new Position(x, y - 1));

            for (int i = 0; i < 4; i++) {
                try {
                    if (!used.contains(tests.get(i))) {
                        if (bdmap.get(tests.get(i)) instanceof BDEmpty
                                || bdmap.get(tests.get(i)) instanceof BDSand
                                || bdmap.get(tests.get(i)).getClass().equals(obj.getClass())) {

//                            System.out.println("Added pos " + tests.get(i) + " to queue");
                            testQueue.add(tests.get(i));
                            hashMap.put(tests.get(i), pos);
                            used.add(tests.get(i));
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
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

    @Override
    public void step() {
        if (--wait <= 0) {
            if (holder.size() > 0) {
                Position rPos = holder.get(0);
                int x = rPos.getX();
                int y = rPos.getY();
                int thisx = this.getX();

                //Check danger!
                if(this.owner.get(rPos.getX(), rPos.getY()+1) instanceof AbstractBDKillingObject
                        || this.owner.get(rPos) instanceof AbstractBDKillingObject){
                    wait = 4;
                    System.out.println("DANGER");
                }

                if (thisx < x) {
                    this.keyPressed(KeyCode.RIGHT);
//                    this.askedToGo = Direction.EAST;
                } else if (thisx > x) {
                    this.keyPressed(KeyCode.LEFT);
//                    this.askedToGo = Direction.WEST;
                } else if (this.getY() > y) {
                    this.keyPressed(KeyCode.DOWN);
//                    this.askedToGo = Direction.SOUTH;
                } else {
                    this.keyPressed(KeyCode.UP);
//                    this.askedToGo = Direction.NORTH;
                }
                this.holder.remove(0);
            } else {
                if (countDiams() > 0)
                    findPath(new BDDiamond(owner));
                else{
                    findPath(new BDDoor(owner));
                }
            }
        }
        super.step();
    }

}
