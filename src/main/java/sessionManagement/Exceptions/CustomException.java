package sessionManagement.Exceptions;

public class CustomException extends Exception {
    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomException(String name, String message) {
        super(message);
        this.name = name;
        this.message = message;
    }

    public CustomException(Exception e) {
        if (e instanceof CustomException) {
            this.name = ((CustomException) e).getName();
        } else {
            this.name = e.getClass().getSimpleName();
        }
        this.message = e.getMessage();
    }
}


