package adventure;

import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class Game {

    /* this is the class that runs the game.
    You may need some member variables */
    private static Adventure myAdventure;

    /**
     * @param args String array of user input from terminal
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); // create input buffer
        String userInput;
        String filename; // JSON file path
        boolean invalidInput;

        // instantiating objects
        adventure.Game theGame = new adventure.Game(); // instantiate object of type Game to avoid using static methods
        Room currentRoom = new Room();
        Room temp = new Room(); // used in method calls
        JSONObject jsonObj;

        theGame.startMessage();
        help();

        // 2. Ask the user if they want to load a json file.
        System.out.println("Would you like to load an adventure description from file? (Y/N)");
        do {
            System.out.print("> ");
            userInput = scanner.next(); // read in next String token from input stream
            scanner.nextLine(); // consume the rest of the line
            invalidInput = false;
            // print error message for incorrect input
            if (!userInput.equals("Y") && !userInput.equals("N") && !userInput.equals("-1")) {
                invalidInput = true;
                System.out.println("I don't understand...");
            }
            // if user wants to quit, exit program
            if (userInput.equals("-1")) {
                System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
                System.exit(0);
            }
        } while (invalidInput);

        // 3. Parse the file the user specified to create the adventure, or load your default adventure
        if (userInput.equals("Y")) { // load user's adventure
            System.out.println("Please enter a relative file path to your adventure.");
            do {
                System.out.print("> ");
                filename = scanner.nextLine().trim(); // filename cannot have a newline
                // if user wants to quit, exit program
                if (filename.equals("-1")) {
                    System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
                    System.exit(0);
                }
                // try loading file
                jsonObj = theGame.loadAdventureJson(filename);
                if (jsonObj != null) { // else shows an error message
                    myAdventure = theGame.generateAdventure(jsonObj);
                }
            } while (jsonObj == null); // while error in parsing or locating file
        } else { // Generate (default) adventure
            System.out.println("Generating default adventure...");
            filename = "default_adventure.json";
            jsonObj = theGame.loadAdventureJson(filename);
            myAdventure = theGame.generateAdventure(jsonObj);
        }

        // begin adventure
        System.out.println("\n[BEGINNING ADVENTURE]----------------------------------------------------");
        // find starting location
        for (int i = 0; i < myAdventure.getTotalNumRooms(); i++) {
            if (myAdventure.listAllRooms().get(i).getStart()) {
                currentRoom = myAdventure.listAllRooms().get(i); // set current room to start room
                myAdventure.setCurrentRoom(currentRoom);
                System.out.println(myAdventure.getCurrentRoomDescription() + "."); // display description of start room
            }
        }
        System.out.println("Items present:");
        currentRoom.printItems();

        // beginning game loop
        do {
            // obtain user input
            System.out.print("> ");
            userInput = scanner.nextLine();
            // if user wants to quit, exit program
            if (userInput.equals("-1")) {
                System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
                System.exit(0);
            }
            String[] input = userInput.split("\\s+"); // split input by spaces
            // use game instance method to parse user input to learn what the user wishes to do
            invalidInput = theGame.parseInput(input, currentRoom);
            if (!invalidInput) {
                temp = currentRoom;
                currentRoom = theGame.play(input, temp);
                myAdventure.setCurrentRoom(currentRoom);
            }
        } while (invalidInput || !userInput.equals("-1"));

        System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
        scanner.close();
    }

    /* you must have these instance methods and may need more */

    /**
     * loads the adventure from the user input
     * @param filename relative filepath for adventure file
     * @return JSONObject which file was parsed into
     */
    public JSONObject loadAdventureJson(String filename) {
        JSONParser parser = new JSONParser(); // JSON 'parser' object to parse file

        try (FileReader reader = new FileReader(filename)) {
            return (JSONObject) parser.parse(reader); // parse file into JSON object
        } catch (Exception e) { // catch all exceptions if try does not work
            System.out.println("Error: File could not be loaded.");
            return null;
        }
    }

    /**
     * returns an Adventure object using information from parsed JSON file
     * @param obj JSONObject that has file parsed into it
     * @return an Adventure instance to use for the rest of the game
     */
    public Adventure generateAdventure(JSONObject obj) {
        Adventure generatedAdv = new Adventure();
        // parse the contents of the file and create the adventure
        JSONObject myObj = (JSONObject) obj.get("adventure");
        // retrieving arrays
        JSONArray itemList = (JSONArray) myObj.get("item");
        JSONArray roomList = (JSONArray) myObj.get("room");
        // parsing arrays
        itemList.forEach(item -> parseItemObject((JSONObject) item, generatedAdv)); // iterating over item array
        roomList.forEach(room -> parseRoomObject((JSONObject) room, generatedAdv)); // iterating over room array

        return generatedAdv;
    }

    /**** MY ADDITIONAL METHODS ****/

    /**
     * parsing user input to see what they want
     * @param input user input parsed into array of Strings
     * @param currentRoom a Room object representing the current room the user is in
     * @return a boolean value representing whether the user input was invalid
     */
    public boolean parseInput(String[] input, Room currentRoom) {
        boolean invalidInput = false;

        if (input.length <= 2) { // user input must always be less than two strings
            if (!input[0].equals("go") && !input[0].equals("look") && !input[0].equals("help")) { // three possible keys
                invalidInput = true;
                System.out.println("I don't understand...");
            } else if (input[0].equals("go")) {
                try { // error-checking for unusual user input
                    if (!input[1].equals("N") && !input[1].equals("E") && !input[1].equals("S") && !input[1].equals("W")) {
                        invalidInput = true;
                        System.out.println("I don't understand...");
                    } else {
                        // check whether connected room exists
                        if (currentRoom.getConnectedRoom(input[1]) == null) {
                            invalidInput = true;
                            System.out.println("There is nothing in that direction.");
                        }
                    }
                } catch (Exception e) {
                    invalidInput = true;
                    System.out.println("I don't understand...");
                }
            } else if (input[0].equals("look")) {
                if (input.length == 2) {
                    try { // error-checking if no items are in current room
                        int counter = 0;
                        for (int i = 0; i < currentRoom.listItems().size(); i++) {
                            // check whether the user input the name of a (valid) item in the room
                            if (!input[1].equals(currentRoom.listItems().get(i).getName())) {
                                invalidInput = true;
                            } else {
                                invalidInput = false;
                                break; // NOTE: refactor code to remove break statement!
                            }
                        }
                        if (invalidInput) {
                            System.out.println("Error: There is no such item in this room/area.");
                        }
                    } catch (Exception e) {
                        invalidInput = true;
                        System.out.println("There are no items in this room/area.");
                    }
                }
            } else { // if (input[0].equals("help"))
                invalidInput = true;
                help();
            }
        } else {
            invalidInput = true;
            System.out.println("Error: User input must be less than two words.");
        }

        return invalidInput;
    }

    /**
     * returns current user location in game
     * @param input user input parsed into array of Strings
     * @param theRoom temp variable to be used in this method to represent the current room
     * @return an object of type Room to update the current room if the user input leads to a room from file
     * */
    public Room play(String[] input, Room theRoom) {
        String itemName; // a valid item name
        Room currentRoom = theRoom;

        // user input must always be less than or equal to two strings
        if (input.length <= 2) {
            // checking whether user wants to 'go' somewhere or 'look'
            if (input[0].equals("go")) {
                if (currentRoom.getConnectedRoom(input[1]) != null) {
                    currentRoom = currentRoom.getConnectedRoom(input[1]); // set current room to connected room
                    myAdventure.setCurrentRoom(currentRoom);
                    System.out.println("\nYou are now in " + currentRoom.getName() + "."); // display room name
                    System.out.println("Items present:");
                    currentRoom.printItems(); // list items in room
                }
            } else if (input[0].equals("look")) {
                if (input.length == 1) { // want longer description of room
                    System.out.println(myAdventure.getCurrentRoom().getLongDescription() + ".");
                } else { // want description of an item
                    itemName = input[1];
                    for (int i = 0; i < theRoom.listItems().size(); i++) {
                        if (itemName.equals(theRoom.listItems().get(i).getName())) {
                            System.out.println("There is " + theRoom.listItems().get(i).getLongDescription() + ".");
                        }
                    }
                }
            }
        }

        return currentRoom;
    }

    /**
     * parses item array from JSON file using one item from itemList at a time
     * @param item JSON object representing one item from items list in JSON file
     * @param generatedAdv Adventure object representing the generated adventure using the file given to load game
     */
    public static void parseItemObject(JSONObject item, Adventure generatedAdv) {
        JSONObject itemObj = item; // creating pointer pointing to mem address of item
        Item nextItem = new Item();

        // get keys within item array element
        try { // error-checking for incorrectly formatted JSON file
            long id = (Long) itemObj.get("id");
            String name = (String) itemObj.get("name");
            String desc = (String) itemObj.get("desc");
            nextItem.setItem(id, name, desc); // storing information in item object
        } catch (Exception e) {
            System.out.println("Error: Items in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }

        generatedAdv.addItem(nextItem); // add item to adventure
    }

    /**
     * parses room array from JSON file using one room from roomList at a time
     * @param room JSON object representing one room from rooms list in JSON file
     * @param generatedAdv Adventure object representing the generated adventure using the file given to load game
     */
    public static void parseRoomObject(JSONObject room, Adventure generatedAdv) {
        JSONObject roomObj = room;
        Room nextRoom = new Room();
        ArrayList<Item> allItems = generatedAdv.listAllItems();
        boolean isStart = false;

        // mark first room in adventure as start room
        String start = (String) roomObj.get("start");
        if (start != null) {
            isStart = true;
        }
        nextRoom.setStart(isStart);

        try { // get keys within room array element, error-checking for incorrectly formatted JSON file
            long roomID = (Long) roomObj.get("id");
            String name = (String) roomObj.get("name");
            String shortDesc = (String) roomObj.get("short_description");
            String longDesc = (String) roomObj.get("long_description");
            nextRoom.setRoomInfo(roomID, name, shortDesc, longDesc); // storing information in Room object
        } catch (Exception e) {
            System.out.println("Error: Rooms in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }

        // parsing entrances
        JSONArray entranceList = (JSONArray) roomObj.get("entrance"); // get keys within entrance array
        nextRoom.createEntranceArrays(); // allocate memory for entrance ids and directions arrays
        for (int i = 0; i < entranceList.size(); i++) {
            JSONObject entrance = (JSONObject) entranceList.get(i);
            try { // get keys within entrance array element, error-checking for incorrectly formatted JSON file
                long entranceID = (Long) entrance.get("id");
                String dir = (String) entrance.get("dir");
                nextRoom.setEntrance(entranceID, dir); // creating new entrance for room
            } catch (Exception e) {
                System.out.println("Error: Entrances in file are incorrectly formatted.");
                System.out.println("Exiting...");
                System.exit(-1);
            }
        }

        // parsing loot
        JSONArray lootList = (JSONArray) roomObj.get("loot"); // get keys within loot array
        if (lootList != null) { // error-checking for no loot
            for (int i = 0; i < lootList.size(); i++) {
                JSONObject loot = (JSONObject) lootList.get(i);
                long lootID = (Long) loot.get("id");
                for (int j = 0; j < allItems.size(); j++) {
                    if (lootID == allItems.get(j).getItemID()) {
                        nextRoom.addRoomItem(allItems.get(j)); // store room's loot in roomItems ArrayList of Room class
                    }
                }
            }
        }

        generatedAdv.addRoom(nextRoom); // add room to adventure
    }

    /**
     * prints start message once user selects file to read from
     */
    public void startMessage() {
        System.out.println("[WELCOME!]---------------------------------------------------------------------");
        System.out.println("This is a prototype game modeled after the 1977 game Colossal Caves by Will Crowther.");
        System.out.println("This version of the game will load an adventure description from file and allow you to \n"
                + "interact with the rooms and items in that adventure.");
    }

    /**
     * prints helpful information to the user if he/she wants it
     */
    public static void help() {
        System.out.println("\n[SOME TIPS]--------------------------------------------------------------");
        System.out.println("As a player of this game, you may:\n"
                + " -- move from room to room in the adventure using the keyword 'go' and the subjects: N, S, E or W.\n"
                + " -- see a longer description of the room when you type 'look'.\n"
                + " -- see a longer description of an item in the room when you type 'look' followed by item name.\n"
                + " -- enter '-1' at any point to quit.\n"
                + " -- enter 'help' during game play to refer to these tips again.\n");
    }
}
