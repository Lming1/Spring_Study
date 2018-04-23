import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
//@DirtiesContext
// ApplicationContext 상태 변경
public class UserDaoTest {


//    @Autowired
    private UserDao userDao;

    @Before
    public void setup() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = applicationContext.getBean("userDao", UserDao.class);
//        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/jeju", "jeju", "jejupw", true);
//        userDao.setDataSource(dataSource);
    }

    @Test(expected=EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        userDao.get("defaultid");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        // 전부 삭제
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        // 사용자 추가
        User user = new User();
        user.setId("admin");
        user.setName("testuser");
        user.setPassword("1234");
        userDao.add(user);
        assertThat(userDao.getCount(), is(1));
        User addedUser = userDao.get(user.getId());
        assertThat(addedUser.getId(), is(user.getId()));
        assertThat(addedUser.getName(), is(user.getName()));
        assertThat(addedUser.getPassword(), is(user.getPassword()));
    }
}
