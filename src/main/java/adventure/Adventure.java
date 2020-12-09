package adventure;

import java.util.ArrayList;

public class Adventure {
    /* you will need to add some private member variables */
    // static -> stays in same memory location, shared among all instances of a class
    private static ArrayList<Room> allRooms = new ArrayList<>();
    private static ArrayList<Item> allItems = new ArrayList<>();
    private Room myRoom; // keeping track of the current room

    /* ======== Required public methods ========== */
    /*
     * note, you don't have to USE all of these methods but you do have to provide
     * them. We will be using them to test your code
     */

    /**
     * lists all rooms in adventure
     * @return ArrayList of all rooms in adventure
     */
    public ArrayList<Room> listAllRooms() {
        return allRooms;
    }

    /**
     * lists all items in adventure
     * @return ArrayList of all items in adventure
     */
    public ArrayList<Item> listAllItems() {
        return allItems;
    }

    /**
     * returns current room description
     * @return String value representing short description of current room
     */
    public String getCurrentRoomDescription() {
        return myRoom.getShortDescription();
    }

    /* you may wish to add additional methods */

    /**
     * returns total number of rooms
     * @return an int value representing the total number of rooms in the adventure
     * */
    public int getTotalNumRooms() {
        return allRooms.size();
    }

    /** returns the current room and will be used to keep track of it
     * @return a Room object representing the current room the user is in
     */
    public Room getCurrentRoom() {
        return myRoom;
    }

    /**
     * add room to allRooms ArrayList
     * @param room room to be added to allRooms ArrayList
     */
    public void addRoom(Room room) {
        allRooms.add(room);
    }

    /**
     * add item to allItems ArrayList
     * @param item item to be added to allItems ArrayList
     */
    public void addItem(Item item) {
        allItems.add(item);
    }

    /**
     * set current room the user is in
     * @param currentRoom the current room the user is in
     */
    public void setCurrentRoom(Room currentRoom) {
        myRoom = currentRoom;
    }
}
