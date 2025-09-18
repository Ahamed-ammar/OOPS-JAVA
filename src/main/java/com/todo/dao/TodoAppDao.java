package com.todo.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import javax.xml.crypto.Data;
import com.todo.model.Todo;
import com.todo.util.db;
//import com.todo.util.db;

public class TodoAppDao {

    private static final String SELECT_ALL_TODOS = "select * from todos";// ORDER BY created_at DESC";
    private static final String INSERT_TODO = "INSERT INTO todo (title, description, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    //Create a New Todo
    public int createtodo(Todo todo) throws SQLException {
        try (
            Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS);
        ) {
            System.out.println("Connecting to database...");
            stmt.setString(1, todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(todo.getCreated_at()));
            stmt.setObject(5, todo.getUpdated_at());
            
            int rowAffected = stmt.executeUpdate();
                if(rowAffected == 0){
                throw new SQLException("Creating todo failed, no rows affected.");
                }
            }
        return 0;
    }


    private Todo getTodoRow(ResultSet res) throws SQLException {

        int id = res.getInt("id");
        String title = res.getString("title");
        String description = res.getString("description");
        boolean completed = res.getBoolean("completed");
        LocalDateTime createdAt = res.getTimestamp("created_at") != null ? res.getTimestamp("created_at").toLocalDateTime() : null;
        LocalDateTime updatedAt = res.getTimestamp("updated_at") != null ? res.getTimestamp("updated_at").toLocalDateTime() : null;
        return new Todo(id, title, description, createdAt, completed, updatedAt);
        //return new Todo();
    }
    public List<Todo> getAllTodos() throws SQLException{
        List<Todo> todos = new ArrayList<>();
        try(Connection conn = db.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TODOS);
            //PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo Order By created_at DESC");
            ResultSet rs = stmt.executeQuery();
            )
        {
            System.out.println("Query executed successfully!");
            while(rs.next()){
                Todo todo = getTodoRow(rs);
                System.out.println(todo); // Print each Todo object for debugging
                todos.add(todo);
                //todos.add(getTodoRow(rs));
                // Todo todo = new Todo();
                // todo.setId(rs.getInt("id"));
                // todo.setTitle(rs.getString("title"));
                // todo.setDescription(rs.getString("description"));
                // todo.setCompleted(rs.getBoolean("completed"));
                // LocalDateTime createdAt = rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null;
                // todo.setCreated_at(createdAt);

                // LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null;
                // todo.setUpdated_at(updatedAt);
                
            }
            //return todos;
        } 
        catch(SQLException e){
            e.printStackTrace();
        } 
        return todos;
    }

    public static String getSelectAllTodos() {
        return SELECT_ALL_TODOS;
    }

    public static String getInsertTodo() {
        return INSERT_TODO;
    }
}