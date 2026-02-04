# Pantry-Tracker

The Smart Pantry Manager is a Java-based Command Line Interface (CLI) application designed to streamline kitchen inventory management. By integrating with the Open Food Facts API, the system allows users to catalog grocery items via barcodes, track quantities, and maintain a persistent local database.

## Features

* **Barcode Integration:** Fetches real-time product data (names and brands) from the Open Food Facts global database.
* **Manual Data Entry:** Provides a fallback mechanism for items not found in the API or for products without a digital record.
* **Inventory Management:** Supports adding, searching, and removing products.
* **Quantity Tracking:** Allows users to manage stock levels by incrementing or decrementing item counts.
* **Persistent Storage:** Utilizes an SQLite database to ensure data is retained across application sessions.

## Technical Stack

* **Language:** Java 11 or higher
* **Database:** SQLite via JDBC
* **Data Parsing:** Jackson Databind (JSON)
* **Networking:** Java HttpClient (Standard in JDK 11+)
* **API:** Open Food Facts REST API

## Installation and Setup

### Prerequisites

To run this project, you must have the following JAR files in your classpath:
1. **SQLite JDBC Driver:** (e.g., sqlite-jdbc-3.x.x.jar)
2. **Jackson Databind:** (e.g., jackson-databind-2.x.x.jar)
3. **Jackson Core & Annotations:** Required dependencies for Databind.

### Running the Application

1. Clone the repository to your local machine.
2. Ensure the required dependencies are linked in your IDE or included in your build path.
3. Run the App.java file located in the src/main/java/com/pantry/ directory.

## Usage Instructions

Upon launching the application, use the following commands in the terminal:

* **[Barcode Number]:** Enter a 12 or 13-digit barcode to search for and add a product.
* **list:** Displays the entire contents of your pantry, including IDs, names, and quantities.
* **search:** Prompts for a product name to find specific entries in the database.
* **remove:** Prompts for a product ID to either decrease the quantity by one or delete the entry entirely.
* **exit:** Securely closes the database connection and terminates the program.

## Credits and Attribution

* **Data Source:** This application utilizes the Open Food Facts API. Product data is provided by the Open Food Facts community under the Open Database License (ODbL).
* **Development Assistance:** This project was developed as a collaborative effort between the developer and Google Gemini (AI) to demonstrate proficiency in API integration and database management.

## License

This project is licensed under the MIT License. See the LICENSE file for full details.
