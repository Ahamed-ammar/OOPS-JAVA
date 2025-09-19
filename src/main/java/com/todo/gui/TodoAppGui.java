package com.todo.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.todo.dao.TodoAppDao;
import java.util.List;
import com.todo.model.Todo;
import com.todo.util.db;
import java.sql.SQLException;

public class TodoAppGui extends JFrame {
    private TodoAppDao todoAppDAO;

    private JTable todoTable;
    private DefaultTableModel tableModel;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;

    public TodoAppGui() {
        this.todoAppDAO = new TodoAppDao();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadTodos(); // <-- Fixed typo: was 'LoadTodos()'
        loadSelectedTodo();
    }

    public void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        String[] columnNames = {"ID", "Title", "Description", "Completed", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
            (e) -> {
                if (!e.getValueIsAdjusting()) {
                    loadSelectedTodo();
                }
            }
        );

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        completedCheckBox = new JCheckBox("Completed");
        addButton = new JButton("Add Todo");
        updateButton = new JButton("Update Todo");
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todo");
        String[] filterOptions = {"All", "Completed", "Pending"};

        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.addActionListener(
            (e) -> {
                String opt = (String)filterComboBox.getSelectedItem();
                filterTodos();
            }
        );
    }
    private void filterTodo() {
        String option = (String) filterComboBox.getSelectedItem();
        if("All".equals(option)) {
            loadTodos();
        }
        else if("Completed".equals(option)) {
            //filterTodosByCompletion(true);
            
        }
        else if("Pending".equals(option)) {
            //filterTodosByCompletion(false);
        }
    }
    // private void setupEventListeners() {
    //     addButton.addActionListener(
    //         (e) ->{addTodo();});
    //     updateButton.addActionListener(
    //         (e) ->{updateTodo();});
    //     deleteButton.addActionListener(
    //         (e) ->{deleteTodo();});
    //     refreshButton.addActionListener(
    //         (e) ->{refreshTodo();});
    //     completedCheckBox.addActionListener(
    //         (e) ->{});
    // }
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Input panel for title, description, completed checkbox
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(completedCheckBox, gbc);

        // Button panel for Add, Update, Delete, Refresh
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Filter panel for filter label and combo box
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        // North panel to combine filter, input, and button panels
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(filterPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(todoTable), BorderLayout.CENTER);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a todo to edit or delete:"));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        addButton.addActionListener(
            (e) ->{addTodo();});
        updateButton.addActionListener(
            (e) ->{updateTodo();});
        deleteButton.addActionListener(
            (e) ->{deleteTodo();});
        refreshButton.addActionListener(
            (e) ->{refreshTodo();});
        completedCheckBox.addActionListener(
            (e) ->{});
    }

    private void addTodo(){
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();

        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }  
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setCompleted(completed);

        try {
            int result = todoAppDAO.createtodo(todo);
            if(result > 0){
                JOptionPane.showMessageDialog(this, "Todo added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                titleField.setText("");
                descriptionArea.setText("");
                completedCheckBox.setSelected(false);
                loadTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add todo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error adding todo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void updateTodo(){
        tableModel.getRowCount();
        int row = todoTable.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this, "Please select a todo to update", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String title = titleField.getText().trim();
        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Todo todo = todoAppDAO.getTodoById(id);
            if(todo != null){
                todo.setTitle(title);
                todo.setDescription(descriptionArea.getText().trim());
                todo.setCompleted(completedCheckBox.isSelected());
                boolean result = todoAppDAO.updateTodo(todo);

                if(result) {
                    JOptionPane.showMessageDialog(this, "Todo update successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTodos();
                }
                else {
                    JOptionPane.showMessageDialog(this, "Failed to update todo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error updating todo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();
    }
    private void deleteTodo(){
        int row = todoTable.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this, "Please select a todo to delete", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this todo?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION){
            return;
        }
        int id = (int) todoTable.getValueAt(row, 0);
        try {
            boolean result = todoAppDAO.deleteTodo(id);
            if(result){
                JOptionPane.showMessageDialog(this, "Todo deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                // titleField.setText("");
                // descriptionArea.setText("");
                // completedCheckBox.setSelected(false);
                loadTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete todo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error deleting todo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
        }
    }
    private void refreshTodo(){
        loadTodos();
    }
    private 
    private void loadTodos(){
        try {
            List<Todo> todos = todoAppDAO.getAllTodos();
            updateTable(todos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading todos: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void loadSelectedTodo(){
        int row = todoTable.getSelectedRow();
        if(row >= 0){
            String title = (String) tableModel.getValueAt(row, 1);
            String description = (String) tableModel.getValueAt(row, 2);
            boolean completed = (Boolean) tableModel.getValueAt(row, 3);

            titleField.setText(title);
            descriptionArea.setText(description);
            completedCheckBox.setSelected(completed);

            // Populate input fields
            // titleField.setText((String) tableModel.getValueAt(row, 1));
            // descriptionArea.setText((String) tableModel.getValueAt(row, 2));
            // completedCheckBox.setSelected((Boolean) tableModel.getValueAt(row, 3));
        }
    }
    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo todo : todos){
            Object[] row = {todo.getId(), todo.getTitle(), todo.getDescription(), todo.isCompleted(), todo.getCreated_at(), todo.getUpdated_at()};
            tableModel.addRow(row);
        }
    }
}