package adventure;

import java.util.ArrayList;

public class Room {
    /* you will need to add some private member variables */
    private adventure.Room connectedRoom; // Room object connected to current Room instance
    private Adventure myAdv = new Adventure();
    // will be returned
    private ArrayList<Item> roomItems; // array list of items in room
    private String roomName;
    private String shortDesc;
    private String longDesc;
    private long roomID;
    private boolean isStart;
    // entrances
    private ArrayList<Long> entranceIDs;
    private ArrayList<String> directions;

    /* required public methods */

    /**
     * lists all the items in the room (i.e. loot)
     * @return an ArrayList containing elements of type Item representing all items in the room
     */
    public ArrayList<Item> listItems(){
        return roomItems;
    }

    /**
     * returns name of room
     * @return a String value representing the room name
     */
    public String getName(){
        return roomName;
    }

    /**
     * returns long description of room
     * @return String value representing long description of room
     */
    public String getLongDescription(){
        return longDesc;
    }

    /**
     * using direction from user input to find connected room
     * @param givenDir the direction the user wants to go
     * @return an object of type Room which represents the
     * connected room (if there is an entrance in that direction)
     */
    public adventure.Room getConnectedRoom(String givenDir) {
        connectedRoom = null; // using this removes the need for a break statement

        // given direction from user input, determine whether entrance in that direction exists
        for (int i = 0; i < directions.size(); i++) {
            if (givenDir.equals(directions.get(i))) {
                // find the entrance element's "id" key value in one of the room elements
                for (int j = 0; j < myAdv.getTotalNumRooms(); j++) {
                    if (entranceIDs.get(i) == myAdv.listAllRooms().get(j).getRoomID()) {
                        connectedRoom = myAdv.listAllRooms().get(j); // set connected room
                    }
                }
            }
        }

        return connectedRoom;
    }

    /* you may wish to add some helper methods*/

    /**
     * print items in room
     */
    public void printItems() {
        try {
            for (int i = 0; i < roomItems.size(); i++) {
                System.out.printf("* %s\n", roomItems.get(i).getName());
            }
        } catch (Exception e) {
            System.out.println("There are no items in this room/area.");
        }
    }

    /**
     * get room short description
     * @return String value representing the short description of the room
     */
    public String getShortDescription() {
        return shortDesc;
    }

    /**
     * returns room id
     * @return a long value representing the room id
     */
    public long getRoomID() {
        return roomID;
    }

    /**
     * set room info (id, name, short and long description) from JSON file
     * @param id the room id
     * @param name the room name
     * @param shortDescription a short description of the room
     * @param longDescription a long description of the room
     */
    public void setRoomInfo(long id, String name, String shortDescription, String longDescription) {
        // exit game if name or descriptions were not given
        if (name == null || name.length() == 0 || shortDescription == null ||  shortDescription.length() == 0
                || longDescription == null || longDescription.length() == 0) {
            System.out.println("Error: Rooms in file are incorrectly formatted...");
            System.out.println("Exiting...");
            System.exit(-1);
        }

        roomID = id;
        roomName = name;
        shortDesc = shortDescription;
        longDesc = longDescription;
    }

    /**
     * add entrance in entrance arrays (ids and directions) for room from JSON file
     * @param id the id of the exit from the current room
     * @param direction the direction in which the exit is in the current room
     */
    public void setEntrance(long id, String direction) {
        // exit game if direction was not given
        if (direction == null || direction.length() == 0) {
            System.out.println("Error: Entrances in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }

        entranceIDs.add(id);
        directions.add(direction);
    }

    /**
     * create entrance arrays (ids and directions)
     */
    public void createEntranceArrays() {
        entranceIDs = new ArrayList<>();
        directions = new ArrayList<>();
    }

    /**
     * add room item in roomItems ArrayList
     * @param item item in room from JSON file
     */
    public void addRoomItem(Item item) {
        if (roomItems == null) {
            roomItems = new ArrayList<>();
        }
        roomItems.add(item);
    }

    /**
     * return room the user starts with in adventure
     * @return boolean variable representing whether room is starting room
     */
    public boolean getStart() {
        return isStart;
    }

    /**
     * set Room with "start" key as the start of the game
     * @param start used to represent whether room was marked as the starting room in the JSON file
     */
    public void setStart(boolean start) {
        isStart = start;
    }
}
