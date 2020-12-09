package adventure;

public class Item {
    /* you will need to add some private member variables */
    private Room myRoom = new Room();
    private Adventure myAdv = new Adventure();
    private String itemName;
    private String desc;
    private long itemID;

    /* required public methods */

    /**
     * returns name of item
     * @return a String representing the name of the item
     */
    public String getName() {
        return itemName;
    }

    /**
     * returns long description of item
     * @return a String representing the description of the item
     */
    public String getLongDescription() {
        return desc;
    }

    /**
     * returns a reference to the room that contains the item
     * @return an object of type Room representing the room that contains the item
     */
    public Room getContainingRoom() {
        for (int i = 0; i < myAdv.listAllRooms().size(); i++) { // loop through all rooms
            for (int j = 0; j < myAdv.listAllRooms().get(i).listItems().size(); j++) { // loop through each room's items
                if (myAdv.listAllRooms().get(i).listItems().get(j).getItemID() == itemID) {
                    myRoom = myAdv.listAllRooms().get(i);
                }
            }
        }

        return myRoom;
    }

    /* you may wish to add some helper methods */

    /**
     * set keys (id, name, and long description) of item in item array from JSON file
     * @param id the id of the item
     * @param name the name of the item
     * @param description the description of the item
     */
    public void setItem(long id, String name, String description) {
        if (name == null || name.length() == 0 || description == null || description.length() == 0) {
            System.out.println("Error: Items in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }

        itemID = id;
        itemName = name;
        desc = description;
    }

    /**
     * return item id
     * @return a long value representing the id of the item
     */
    public long getItemID() {
        return itemID; // get item id for specified item in roomItems ArrayList
    }
}
