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
                    //loadSelectedTodo();
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
                //filterTodos();
            }
        );
    }

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
    }

    private void addTodo(){

    }
    private void updateTodo(){

    }
    private void deleteTodo(){

    }
    private void refreshTodo(){
        loadTodos();
    }
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
    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo todo : todos){
            Object[] row = {todo.getId(), todo.getTitle(), todo.getDescription(), todo.isCompleted(), todo.getCreated_at(), todo.getUpdated_at()};
            tableModel.addRow(row);
        }
    }
}