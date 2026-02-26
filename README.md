# Smart Pantry System

A Java-based inventory management application that tracks food items through barcode scanning, manual entry, and a web dashboard. The system integrates with the Open Food Facts API to automatically retrieve product details and stores all data in a local SQLite database.

Data Attribution

1. Attribution: Data is provided by Open Food Facts under the Open Database License (ODbL)
   This project utilizes the Open Food Facts API to automatically retrieve product names and brands based on barcode scans.

   * Data Source: All food product information is sourced from the Open Food Facts database.
   * License: The data is made available under the Open Database License (ODbL).
   * API Usage: This application identifies itself to the Open Food Facts servers using a custom User-Agent header as per their technical guidelines.
   * Product images (if displayed) are provided under the [Creative Commons Attribution ShareAlike](https://creativecommons.org/licenses/by-sa/3.0/)

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

1. Download "Run Program" folder
2. Run RunPantry.bat

The dashboard can be accessed at: http://localhost:7070

## Usage

* Use Webcam to enter in barcode (If browser asks for HTML5 canvas data you must allow it or the camera won't work)
* Manually enter barcode
* manually enter in product (Barcode is optional when using manual entry)
* Adjust quantity of items
* Deleter items

### API Endpoints

* GET /api/inventory: Returns a JSON array of all stored items.
* POST /api/add?barcode=[id]&qty=[n]: Adds an item via API lookup.
* POST /api/manual: Saves an item with manually provided details.
* POST /api/update?barcode=[id]&change=[n]: Adjusts the quantity of a specific item.
* DELETE /api/delete?barcode=[id]: Removes an item from the database.
