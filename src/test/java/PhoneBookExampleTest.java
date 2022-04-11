import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class PhoneBookExampleTest {

    private static final String COMMANDS_STRING = "list\nexit\n";

    @Test
    public void mainTest(){
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(COMMANDS_STRING.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(printStream);

        PhoneBook.main(new String[0]);

        System.setIn(stdin);
        System.setOut(stdout);

        String outputText = byteArrayOutputStream.toString();
        String key = "output:";
        String output = outputText.substring(outputText.indexOf(key) + key.length()).trim();
        Assert.assertTrue("Expected record not found", output.contains("ab"));
    }
}