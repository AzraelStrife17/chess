package service;
import java.util.Collection;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public User registerUser(User user) throws ResponseException{

    }
}
