import java.util.Collections;
import java.util.List;

public class CommandParser {

    public static List<String> parse(String inputLine) {
        if (inputLine == null) {
            return Collections.emptyList();
        }

        String[] tokensArray = inputLine.split("\\s+");
        return List.of(tokensArray);
    }
}
