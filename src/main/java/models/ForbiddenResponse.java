package models;

/**
 * Created by gantz on 27.02.16.
 */
public class ForbiddenResponse {
    public static final int STATUS = 403;
    private int status;
    private String message;

    public ForbiddenResponse() {
        status = STATUS;
        message = "Чужой юзер.";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
