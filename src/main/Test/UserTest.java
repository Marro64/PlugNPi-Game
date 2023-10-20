import Exceptions.CustomException;
import model.User;
import model.UserSignup;
import model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import dao.UserDao;
import static model.UserType.ADMIN;
import static model.UserType.PLAYER;
import static org.junit.jupiter.api.Assertions.*;
public class UserTest {

    private UserSignup user;
    @BeforeEach
    public void setUp() {
        this.user = new UserSignup("BenShapiro","testUser@jew.com", "password");
    }
    @Test
    public void completeTest() {
            int userID = UserDao.INSTANCE.addUser(user);
            User retrievedUser = new User();
            UserDao.INSTANCE.jsonToUser(UserDao.INSTANCE.getUser(userID),retrievedUser );
            assertEquals(user.getEmail(), retrievedUser.getEmail());
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            UserDao.INSTANCE.deleteUser(retrievedUser);
            assertThrows(CustomException.class, () -> {
                UserDao.INSTANCE.getUser(userID);
            }, "User with that id does not exist");

    }


}
