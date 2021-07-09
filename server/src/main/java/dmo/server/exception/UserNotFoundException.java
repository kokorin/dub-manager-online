package dmo.server.exception;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
