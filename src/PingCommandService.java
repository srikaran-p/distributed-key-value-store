import java.util.List;

public class PingCommandService implements CommandService {

    @Override
    public String execute(List<String> tokens) {
        return "PONG";
    }

    @Override
    public boolean validate(List<String> tokens) {
        return tokens.size() == 1;
    }
}
