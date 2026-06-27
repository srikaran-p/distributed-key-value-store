public class LogEntry {

    private Command command;
    private String key;
    private String value;

    public LogEntry(Command command, String key) {
        new LogEntry(command, key, null);
    }

    public LogEntry(Command command, String key, String value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
