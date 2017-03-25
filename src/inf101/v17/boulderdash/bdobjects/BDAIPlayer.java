package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.input.KeyCode;

import java.util.*;

//Created by RakNoel, 23.03.2017.
public class BDAIPlayer extends BDPlayer implements IBDKillable {

    private ArrayList<Position> holder;
    private ArrayList<Position> danger;
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
        danger = new ArrayList<>();
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
            System.out.println("Queue: " + testQueue.size());
            Position pos = testQueue.remove();

            if (bdmap.get(pos).getClass().equals(obj.getClass())) {
                //DONE, now retrace path
                while (hashMap.get(pos) != null) {
                    holder.add(pos);
                    pos = hashMap.get(pos);
                }
                //then reverse
                Collections.reverse(holder);
                break;
            }

            //Array with possible test positions
            ArrayList<Position> tests = new ArrayList<>();
            tests.add(pos.moveDirection(Direction.EAST));
            tests.add(pos.moveDirection(Direction.SOUTH));
            tests.add(pos.moveDirection(Direction.WEST));
            tests.add(pos.moveDirection(Direction.NORTH));

            for (Position i : tests)
                System.out.println(i.toString());

            for (int i = 0; i < tests.size(); i++) {
                try {
                    Position thisTest = tests.get(i);
                    if (!used.contains(thisTest)) {
                        //Add to que if possible route
                        if (bdmap.get(thisTest) instanceof BDEmpty
                                || bdmap.get(thisTest) instanceof BDSand
                                || bdmap.get(thisTest).getClass().equals(obj.getClass())) {
                            //Blocks up/down movement in dangerous tiles
                            if (!danger.contains(pos) || (danger.contains(pos) && i%2 == 0)) {
                                testQueue.add(thisTest);
                                hashMap.put(thisTest, pos);
                                used.add(thisTest);
                            }else{
                                //BLocked by danger
                                System.out.println("Blocked by danger");
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }
            }
        }
        System.out.println("Queue empty and no path found");
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

                try{
                //Also add to dangerous-list IF fallingobj above
                if (!danger.contains(thisTest) &&
                        bdmap.get(thisTest.moveDirection(Direction.NORTH))
                                instanceof AbstractBDFallingObject) {
                    this.danger.add(thisTest);
                }}catch(IndexOutOfBoundsException e){
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
                }catch(IndexOutOfBoundsException e){
                    //Ignore
                }

                try {
                    //Else if slime, then add
                    if (!danger.contains(thisTest)
                            && bdmap.get(thisTest) instanceof BDSlimeSpawn) {
                        danger.add(thisTest);
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
                }catch(IndexOutOfBoundsException e){
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
            if(danger.size() == 0)
               markDanger();

            //See if there are instructions
            if (holder.size() > 0) {
                Position rPos = holder.get(0);
                int x = rPos.getX();
                int y = rPos.getY();

                //TODO: DangerTile Behaviour
                try {
                    Position nPos = holder.get(1);
                    if (danger.contains(rPos) && danger.contains(nPos)) {
                        if (owner.get(rPos.getX(), rPos.getY()) instanceof AbstractBDKillingObject
                                || owner.get(nPos.getX(), nPos.getY()) instanceof AbstractBDKillingObject) {
                            wait = 4;
                            return;
                        }
                    }
                }catch(IndexOutOfBoundsException e){
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

                this.holder.remove(0);

                //Now wait so we don't quantom leap!
                wait = 4;
            }

            //Else find new instructions
            else {
                if (countDiams() > 0) {
                    findPath(new BDDiamond(owner));
                }else {
                    findPath(new BDDoor(owner));
                }
            }
        }
        super.step();
    }

}
