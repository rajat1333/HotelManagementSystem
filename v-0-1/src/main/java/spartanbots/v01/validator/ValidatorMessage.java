package spartanbots.v01.validator;

public class ValidatorMessage {
    private boolean result;
    private String message;

    public ValidatorMessage(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
