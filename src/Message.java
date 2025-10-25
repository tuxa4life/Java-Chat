import java.time.LocalDate;

public class Message {
    private final String author;
    private final String message;
    private final LocalDate date;

    public Message (String client, String message) {
        this.author = client;
        this.message = message;
        this.date = LocalDate.now();
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime () {
        return date.toString();
    }
}
