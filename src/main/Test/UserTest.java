import jakarta.ws.rs.core.Response;
import resources.UserResource;
import sessionManagement.Exceptions.CustomException;
import model.User;
import model.UserSignup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.UserDao;

import static org.junit.jupiter.api.Assertions.*;
public class UserTest {

    private UserSignup user;
    private User retrievedUser;
    private int userID;
    @BeforeEach
    public void setUp() {
        this.user = new UserSignup("BenShapiro","bing@live.nl", "N0tMyR34lP4$$word");
        userID = UserDao.INSTANCE.addUser(user);
        retrievedUser = new User();
        UserDao.INSTANCE.jsonToUser(UserDao.INSTANCE.getUser(userID),retrievedUser );
    }

    /**
     * Test for creating a user and deleting it.
     * BenShapiro should not be in the DB!
     */
    @Test
    public void registrationTest() {
            assertEquals(user.getEmail(), retrievedUser.getEmail());
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            UserDao.INSTANCE.deleteUser(retrievedUser);
            assertNull(UserDao.INSTANCE.getUser(userID));
    }

}
