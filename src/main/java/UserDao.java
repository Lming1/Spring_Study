import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private JdbcContext jdbcContext;
    private DataSource dataSource;
    //    public UserDao(ConnectionMaker dataSource) {
//        this.dataSource = dataSource;
//    }
//
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void setJdbcContext(JdbcContext jdbcContext){
        this.jdbcContext = jdbcContext;
    }



    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = dataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id = ?");
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;
        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }



    public void add(User user) throws SQLException, ClassNotFoundException {
        this.jdbcContext.workWithStatementStrategy(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
                    preparedStatement.setString(1, user.getId());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getPassword());
                    return preparedStatement;
                }
        );
    }




    public void deleteAll() throws SQLException {
        // 클라이언트 코드
        this.jdbcContext.workWithStatementStrategy(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from users");
                    return preparedStatement;
                }
        );
    }



    public int getCount() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select count(*) from users");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }catch (SQLException e){
            throw e;
        }
        finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e){

                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e){

                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e){

                }
            }
        }


    }
}
