# CIS2430*S20 (OOP) Assignment 1

Mini adventure game that loads a JSON file and allows the user to navigate through various settings and interact with items in them.

## Compiling and running

`mvn clean compile checkstyle:checkstyle test exec:java assembly:single`

When prompted for a filename, enter a relative filepath to the JSON file you wish to load for the adventure.
If the file is within the project directory, you only need to input the filename.
If the file is within another directory (outside of `project`), you must use:
  *`../filename.json` to navigate up one directory level, or
  *`/foldername/filename.json` to enter another folder.

## Game play

* move from room to room in the loaded adventure using the keyword `go` and the subjects: `N`, `S`, `E` or `W`.
* see a longer description of the room when you type `look`.
* see a longer description of an item in the room when you type `look` followed by the name of the item.
* enter `-1` at any point to quit.
* enter `help` at any point during the game to for commands/help.
