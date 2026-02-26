# Smart Pantry System

A Java-based inventory management application that tracks food items through barcode scanning, manual entry, and a web dashboard. The system integrates with the Open Food Facts API to automatically retrieve product details and stores all data in a local SQLite database.

Data Attribution

1. Attribution: Data is provided by Open Food Facts under the Open Database License (ODbL)
   This project utilizes the Open Food Facts API to automatically retrieve product names and brands based on barcode scans.

   Data Source: All food product information is sourced from the Open Food Facts database.
   License: The data is made available under the Open Database License (ODbL).
   API Usage: This application identifies itself to the Open Food Facts servers using a custom User-Agent header as per their technical guidelines.
   Product images (if displayed) are provided under the [Creative Commons Attribution ShareAlike](https://creativecommons.org/licenses/by-sa/3.0/)

## Features

* Barcode Integration: Automatically fetches product names and brands from the Open Food Facts API.
* Dual Interface: Includes a Command Line Interface (CLI) for rapid scanning and a Web interface for visual management.
* Persistent Storage: Uses a local SQLite database (pantry.db) to ensure data remains available between sessions.
* Automated Logic: Updates quantities on barcode conflict and automatically removes items when their quantity reaches zero.

## Technical Stack

* Language: Java 11 or higher
* Database: SQLite (via JDBC)
* Web Framework: Javalin
* JSON Processing: Jackson Databind
* External API: Open Food Facts API

## Prerequisites

* Java Development Kit (JDK) 11 or higher.
* Maven or another build tool to manage the following dependencies:
* javalin
* jackson-databind
* sqlite-jdbc
* slf4j-simple



## Installation and Setup

1. Clone the repository:
```bash
git clone https://github.com/your-username/smart-pantry-system.git
cd smart-pantry-system
```
2. run

The dashboard can be accessed at: http://localhost:7070

## Usage

### CLI Commands

* list: Display all items currently in the pantry.
* search: Find specific products by name.
* remove: Reduce the quantity of an item or delete it by ID.
* exit: Safely close the database and exit the program.
* [barcode]: Entering a numeric barcode triggers an API lookup.

### API Endpoints

* GET /api/inventory: Returns a JSON array of all stored items.
* POST /api/add?barcode=[id]&qty=[n]: Adds an item via API lookup.
* POST /api/manual: Saves an item with manually provided details.
* POST /api/update?barcode=[id]&change=[n]: Adjusts the quantity of a specific item.
* DELETE /api/delete?barcode=[id]: Removes an item from the database.
