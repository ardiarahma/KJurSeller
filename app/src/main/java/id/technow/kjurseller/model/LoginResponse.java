package id.technow.kjurseller.model;

/**
 * Created by Afyad Kafa on 1/14/2019.
 */

public class LoginResponse {
    private String status;
    private String message;
    private User user;

    public LoginResponse(String status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
