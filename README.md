Phone Book app + example automated tests
====================

Phone Book app
---------------

Run with:
> mvn compile exec:java -Dexec.mainClass="PhoneBook"

List, add, remove or search your contacts with this simple console application.

Type a command or 'exit' to quit:
* list - lists all saved contacts in alphabetical  order
* search - search for a contact by name
* add - add a new contact entry into the phone book
* remove - removes a contact from the phone book 
* reset - reset the phonebook to initial state stored in phone_book_example.csv
* exit - quit app

Phone Book app automated tests
---------------

Run with:

> mvn test

Each run of the test perform phonebook reset to the initial state stored in phone_book_example.csv
