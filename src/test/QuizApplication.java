package test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;


public class QuizApplication extends JFrame {

    private JTabbedPane tabbedPane;
    private JRadioButton[][] options;
    private String[][] questions;
    private String[] correctAnswers;
    private JButton submitButton, nextButton, backButton;
    private JPanel navigationPanel;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private Timer timer;
    private int numQuestions = 10;
    private int timeLimit = 15 * 60; // 15 phút (15 phút * 60 giây)


    public QuizApplication() {
        initializeDataFromDatabase();

        // Khởi tạo khung chính
        setTitle("Quiz Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Hiển thị cửa sổ ở giữa màn hình

        // Màn hình chờ
        JPanel waitingPanel = new JPanel();
        waitingPanel.setLayout(new BorderLayout());
        waitingPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Thêm khoảng trắng
        JButton startButton = new JButton("Kiểm Tra");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new StartQuizAction());
        waitingPanel.add(startButton, BorderLayout.CENTER);
        add(waitingPanel);

        // Thiết lập giao diện câu đố
        setupQuizUI();

        // Hiển thị màn hình chờ
        setVisible(true);
    }

    private void initializeDataFromDatabase() {
        Connection connection = null;
        try {
            // Kết nối đến cơ sở dữ liệu MySQL trong XAMPP
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizapplication", "root", "");

            // Thực thi truy vấn SQL để lấy câu hỏi
            String query = "SELECT * FROM question";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            questions = new String[numQuestions][5];
            correctAnswers = new String[numQuestions];

            // Lấy dữ liệu từ kết quả truy vấn và gán vào mảng questions
            int i = 0;
            while (resultSet.next() && i < numQuestions) {
                questions[i] = new String[]{
                        " " + resultSet.getString("question"),
                        "A: " + resultSet.getString("optionA"),
                        "B: " + resultSet.getString("optionB"),
                        "C: " + resultSet.getString("optionC"),
                        "D: " + resultSet.getString("optionD")
                };
                correctAnswers[i] = resultSet.getString("answer");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupQuizUI() {
        tabbedPane = new JTabbedPane();
        ButtonGroup[] buttonGroups = new ButtonGroup[numQuestions];
        options = new JRadioButton[numQuestions][4];

        for (int i = 0; i < numQuestions; i++) {
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new GridLayout(5, 1));
            JLabel questionLabel = new JLabel(questions[i][0]);
            questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
            questionPanel.add(questionLabel);

            buttonGroups[i] = new ButtonGroup();
            for (int j = 0; j < 4; j++) {
                options[i][j] = new JRadioButton(questions[i][j + 1]);
                options[i][j].setFont(new Font("Arial", Font.PLAIN, 16));
                buttonGroups[i].add(options[i][j]);
                questionPanel.add(options[i][j]);
            }

            tabbedPane.add(String.valueOf(i + 1), questionPanel);
        }

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.addActionListener(new SubmitQuizAction());

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                if (currentIndex < numQuestions - 1) {
                    tabbedPane.setSelectedIndex(currentIndex + 1);
                }
            }
        });

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentIndex = tabbedPane.getSelectedIndex();
                if (currentIndex > 0) {
                    tabbedPane.setSelectedIndex(currentIndex - 1);
                }
            }
        });


        navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navigationPanel.add(backButton, BorderLayout.WEST);

        navigationPanel.add(submitButton, BorderLayout.AFTER_LAST_LINE);
        navigationPanel.add(nextButton, BorderLayout.EAST);
        // them setup timer

        navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(submitButton, BorderLayout.AFTER_LAST_LINE);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        timerLabel = new JLabel("Thời gian còn lại: 15:00", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        navigationPanel.add(timerLabel, BorderLayout.NORTH);


    }

    private class StartQuizAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            add(tabbedPane, BorderLayout.CENTER);
            add(navigationPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();
            startTimer(); // Bắt đầu đếm thời gian sau khi người dùng bắt đầu bài kiểm tra
        }



    }




    private class SubmitQuizAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.cancel();
            submitQuiz();
        }
    }
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = timeLimit;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (remainingTime > 0) {
                        remainingTime--;
                        int minutes = remainingTime / 60;
                        int seconds = remainingTime % 60;
                        timerLabel.setText(String.format("Thời gian còn lại: %02d:%02d", minutes, seconds));
                    } else {
                        timer.cancel();
                        submitQuiz();
                    }
                });
            }
        }, 1000, 1000);
    }
    private void submitQuiz() {
        int correctCount = 0;
        int incorrectCount = 0;
        int unansweredCount = 0;
        StringBuilder resultMessage = new StringBuilder();

        for (int i = 0; i < numQuestions; i++) {
            boolean answered = false;
            for (int j = 0; j < 4; j++) {
                if (options[i][j].isSelected()) {
                    answered = true;
                    String selectedAnswer = options[i][j].getText().substring(3);
                    if (selectedAnswer.equals(correctAnswers[i])) {
                        correctCount++;
                        resultMessage.append("Câu ").append(i + 1).append(": Đúng.\n");
                    } else {
                        incorrectCount++;
                        resultMessage.append("Câu ").append(i + 1).append(": Sai.\n");
                    }
                }
            }
            if (!answered) {
                unansweredCount++;
                resultMessage.append("Câu ").append(i + 1).append(": Chưa chọn đáp án\n");
            }
        }

        JOptionPane.showMessageDialog(null,
                "Đáp án đúng: " + correctCount + "/" + numQuestions +
                        "\nĐáp án sai: " + incorrectCount + "/" + numQuestions +
                        "\nCâu chưa chọn đáp án: " + unansweredCount + "/" + numQuestions);

        JProgressBar progressBar = new JProgressBar(0, numQuestions);
        progressBar.setValue(correctCount);
        progressBar.setStringPainted(true);
        JOptionPane.showMessageDialog(null, progressBar, "Kết quả", JOptionPane.INFORMATION_MESSAGE);

        JOptionPane.showMessageDialog(null, resultMessage.toString(), "Kết quả chi tiết", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    public static void main(String[] args) {
            SwingUtilities.invokeLater(QuizApplication::new);
        }
    }
