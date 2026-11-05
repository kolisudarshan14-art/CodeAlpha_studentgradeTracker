
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// Student class
class Student {
    String name;
    double marks;

    Student(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }

    String getGrade() {
        if (marks >= 90) return "A";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }
}

// Main GUI class
public class GradeTracker extends JFrame {

    ArrayList<Student> students = new ArrayList<>();

    JTextField nameField, marksField;
    JTable table;
    DefaultTableModel model;

    JLabel averageLabel, highestLabel, lowestLabel;

    public GradeTracker() {

        setTitle("Student Grade Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel(
            "STUDENT GRADE TRACKER", JLabel.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(30, 70, 140));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));

        add(heading, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());

        nameField = new JTextField(12);
        marksField = new JTextField(8);

        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All");
        JButton reportButton = new JButton("Summary Report");

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Marks (0-100):"));
        inputPanel.add(marksField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(reportButton);

        // Table
        String[] columns = {
            "No.", "Student Name", "Marks", "Grade"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        averageLabel = new JLabel("Average: 0", JLabel.CENTER);
        highestLabel = new JLabel("Highest: 0", JLabel.CENTER);
        lowestLabel = new JLabel("Lowest: 0", JLabel.CENTER);

        averageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        highestLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lowestLabel.setFont(new Font("Arial", Font.BOLD, 16));

        statsPanel.add(averageLabel);
        statsPanel.add(highestLabel);
        statsPanel.add(lowestLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        bottomPanel.add(statsPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add student action
        addButton.addActionListener(e -> addStudent());

        // Delete student action
        deleteButton.addActionListener(e -> deleteStudent());

        // Clear all action
        clearButton.addActionListener(e -> clearAll());

        // Summary report action
        reportButton.addActionListener(e -> showReport());

        setVisible(true);
    }

    // Add student
    void addStudent() {

        String name = nameField.getText().trim();
        String marksText = marksField.getText().trim();

        if (name.isEmpty() || marksText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter student name and marks!"
            );
            return;
        }

        try {
            double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(
                    this, "Marks must be between 0 and 100!"
                );
                return;
            }

            Student student = new Student(name, marks);
            students.add(student);

            model.addRow(new Object[]{
                students.size(),
                student.name,
                student.marks,
                student.getGrade()
            });

            nameField.setText("");
            marksField.setText("");

            updateStatistics();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid numeric marks!"
            );
        }
    }

    // Delete selected student
    void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a student to delete!"
            );
            return;
        }

        students.remove(selectedRow);
        model.removeRow(selectedRow);

        refreshTable();
        updateStatistics();
    }

    // Refresh table numbering
    void refreshTable() {

        model.setRowCount(0);

        for (int i = 0; i < students.size(); i++) {

            Student s = students.get(i);

            model.addRow(new Object[]{
                i + 1,
                s.name,
                s.marks,
                s.getGrade()
            });
        }
    }

    // Clear all records
    void clearAll() {

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No records to clear!"
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete all records?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            students.clear();
            model.setRowCount(0);
            updateStatistics();
        }
    }

    // Calculate average, highest and lowest
    void updateStatistics() {

        if (students.isEmpty()) {
            averageLabel.setText("Average: 0");
            highestLabel.setText("Highest: 0");
            lowestLabel.setText("Lowest: 0");
            return;
        }

        double total = 0;
        double highest = students.get(0).marks;
        double lowest = students.get(0).marks;

        for (Student s : students) {

            total += s.marks;

            if (s.marks > highest) {
                highest = s.marks;
            }

            if (s.marks < lowest) {
                lowest = s.marks;
            }
        }

        double average = total / students.size();

        averageLabel.setText(
            String.format("Average: %.2f", average)
        );

        highestLabel.setText(
            String.format("Highest: %.2f", highest)
        );

        lowestLabel.setText(
            String.format("Lowest: %.2f", lowest)
        );
    }

    // Display summary report
    void showReport() {

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No student records available!"
            );
            return;
        }

        double total = 0;
        double highest = students.get(0).marks;
        double lowest = students.get(0).marks;

        for (Student s : students) {
            total += s.marks;
            highest = Math.max(highest, s.marks);
            lowest = Math.min(lowest, s.marks);
        }

        double average = total / students.size();

        StringBuilder report = new StringBuilder();

        report.append("STUDENT SUMMARY REPORT\n");
        report.append("==============================\n");
        report.append("Total Students: ").append(students.size());
        report.append("\nAverage Marks: ")
              .append(String.format("%.2f", average));
        report.append("\nHighest Marks: ").append(highest);
        report.append("\nLowest Marks: ").append(lowest);
        report.append("\n\nSTUDENT DETAILS\n");
        report.append("------------------------------\n");

        for (Student s : students) {
            report.append(s.name)
                  .append(" | Marks: ").append(s.marks)
                  .append(" | Grade: ").append(s.getGrade())
                  .append("\n");
        }

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(textArea),
            "Summary Report",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GradeTracker();
        });
    }
}
