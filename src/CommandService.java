import java.util.List;

public interface CommandService {

    String execute(List<String> tokens);

    boolean validate(List<String> tokens);
}
