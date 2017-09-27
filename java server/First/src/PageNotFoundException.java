

public class PageNotFoundException extends Exception {

    public PageNotFoundException(String message){
        super(message);
    }

    public PageNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
