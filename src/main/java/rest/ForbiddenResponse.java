package rest;

/**
 * Created by gantz on 27.02.16.
 */
public class ForbiddenResponse {
    private int status;
    private String message;

    public ForbiddenResponse() {
        status = 403;
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
