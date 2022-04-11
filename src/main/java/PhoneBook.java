import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {

    private static final String CSV_STORAGE_PATH = "phone_book.csv";
    private static final String CSV_STORAGE_EXAMPLE_PATH = "phone_book_example.csv";

    public static void resetPhoneBook() throws URISyntaxException, IOException {
        FileUtils.copyFile(new File(PhoneBook.class.getClassLoader()
                .getResource(CSV_STORAGE_EXAMPLE_PATH)
                .toURI()), new File(PhoneBook.class.getClassLoader()
                .getResource(CSV_STORAGE_PATH)
                .toURI()));
    }

    private static void saveContacts(Map<String, List<String>> contacts) {
        try (PrintWriter writer = new PrintWriter(new File(PhoneBook.class.getClassLoader()
                .getResource(CSV_STORAGE_PATH)
                .toURI()))) {
            if (!contacts.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                    String line = String.format("%s,\"%s\"",
                            entry.getKey(), entry.getValue().toString().replaceAll("\\[|]", ""));
                    writer.println(line);
                }
            }

        } catch (IOException | URISyntaxException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private static void loadContacts(Map<String, List<String>> contacts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(PhoneBook.class.getClassLoader()
                .getResource(CSV_STORAGE_PATH)
                .toURI())))) {

            Pattern pattern = Pattern.compile("^([^,\"]{2,50}),\"([0-9+, ]+)\"$");

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String[] numbers = matcher.group(2).split(",\\s*");
                    contacts.put(matcher.group(1), Arrays.asList(numbers));
                }
            }

        } catch (IOException | URISyntaxException e) {
            System.err.println("Could not load contacts, phone book is empty!");
        }
    }

    private static void listCommands() {
        System.out.println("list - lists all saved contacts in alphabetical  order");
        System.out.println("search - finds a contact by name");
        System.out.println("add - saves a new contact entry into the phone book");
        System.out.println("remove - removes a contact from the phone book");
    }

    private static void listAllContacts(Map<String, List<String>> contacts) {
        if (!contacts.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                System.out.print(entry.getKey()+ ": ");
                for (String number : entry.getValue()) {
                    System.out.println(number);
                }
            }
        } else {
            System.out.println("No records found, the phone book is empty!");
        }

        System.out.println();
        System.out.println("Type a command or 'exit' to quit:");
    }

    private static void searchContact(Map<String, List<String>> contacts, Scanner input) {
        System.out.println("Enter the name you are looking for:");
        String name = input.nextLine().trim();

        if (contacts.containsKey(name)) {
            System.out.println(name);
            for (String number : contacts.get(name)) {
                System.out.println(number);
            }
        } else {
            System.out.println("Sorry, nothing found!");
        }

        System.out.println();
        System.out.println("Type a command or 'exit' to quit:");
    }

    private static void addContact(Map<String, List<String>> contacts, Scanner input) {
        System.out.println("You are about to add a new contact to the phone book.");
        String name;
        String number;

        while (true) {
            System.out.println("Enter contact name:");
            name = input.nextLine().trim();
            if (name.matches("^.{2,50}$")) {
                break;
            } else {
                System.out.println("Name must be in range 2 - 50 symbols.");
            }
        }

        while (true) {
            System.out.println("Enter contact number:");
            number = input.nextLine().trim();
            if (number.matches("^\\+?[0-9 ]{3,25}$")) {
                break;
            } else {
                System.out.println("Number may contain only '+', spaces and digits. Min length 3, max length 25.");
            }
        }

        if (contacts.containsKey(name)) {
            System.out.printf("'%s' already exists in the phone book!\n", name);

            if (contacts.get(name).contains(number)) {
                System.out.printf("Number %s already available for contact '%s'.\n", number, name);
            } else {
                contacts.get(name).add(number);
                saveContacts(contacts);
                System.out.printf("Successfully added number %s for contact '%s'.\n", number, name);
            }

        } else {
            List<String> numbers = new ArrayList<>();
            numbers.add(number);
            contacts.put(name, numbers);
            saveContacts(contacts);
            System.out.printf("Successfully added contact '%s' !\n", name);
        }

        System.out.println();
        System.out.println("Type a command or 'exit' to quit:");
    }

    private static void removeContact(Map<String, List<String>> contacts, Scanner input) {
        System.out.println("Enter name of the contact to be removed:");
        String name = input.nextLine().trim();

        if (contacts.containsKey(name)) {
            System.out.printf("Contact '%s' will be removed. Are you sure? [Y/N]:\n", name);
            String confirmation = input.nextLine().trim().toLowerCase();
            confirm:
            while (true) {
                switch (confirmation) {
                    case "y":
                        contacts.remove(name);
                        saveContacts(contacts);
                        System.out.println("Contact was removed successfully!");
                        break confirm;
                    case "n":
                        break confirm;
                    default:
                        System.out.println("Remove contact? [Y/N]:");
                        break;
                }
                confirmation = input.nextLine().trim().toLowerCase();
            }

        } else {
            System.out.println("Sorry, name not found!");
        }

        System.out.println();
        System.out.println("Type a command or 'exit' to quit:");
    }

    public static void main(String[] args) {

        System.out.println("PHONE BOOK");
        System.out.println("===========================");
        System.out.println("Type a command or 'exit' to quit:");
        try {
            resetPhoneBook();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        listCommands();
        System.out.print("> ");

        Map<String, List<String>> contacts = new TreeMap<>();
        loadContacts(contacts);

        Scanner input = new Scanner(System.in);
        String line = input.nextLine().trim();

        while (!line.equals("exit")) {

            switch (line) {
                case "list":
                    listAllContacts(contacts);
                    break;
                case "search":
                    searchContact(contacts, input);
                    break;
                case "add":
                    addContact(contacts, input);
                    break;
                case "remove":
                    removeContact(contacts, input);
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }


            System.out.print("\n> ");
            line = input.nextLine().trim();
        }

        System.out.println("'Phone Book' terminated.");
    }
}