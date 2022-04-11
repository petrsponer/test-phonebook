import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;

public class PhoneBookExampleTest {

    private static final String LIST_EXIT_COMMANDS = "list\nexit\n";
    private static final String NO_COMMAND = "nocommand\nexit\n";

    @BeforeClass
    public static void prepTestData() throws URISyntaxException, IOException {
        PhoneBook.resetPhoneBook();
    }

    @Test
    public void commandListTest() {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(LIST_EXIT_COMMANDS.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(printStream);

        PhoneBook.main(new String[0]);

        System.setIn(stdin);
        System.setOut(stdout);

        String outputText = byteArrayOutputStream.toString();
        Assert.assertTrue("Expected record not found", outputText.contains("customer: 123456789"));
        Assert.assertTrue("Expected record not found", outputText.contains("help: 911"));
    }

    @Test
    public void noCommandTest() {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(NO_COMMAND.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(printStream);

        PhoneBook.main(new String[0]);

        System.setIn(stdin);
        System.setOut(stdout);

        String outputText = byteArrayOutputStream.toString();
        Assert.assertTrue("Expected record not found", outputText.contains("Invalid command!"));
        }

}