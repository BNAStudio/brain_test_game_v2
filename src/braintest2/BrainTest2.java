package braintest2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrainTest2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MathGame mathGame = new MathGame();
                mathGame.startGame();
            }
        });
    }

}

class MathGame implements ActionListener {
    private MathQuestion currentQuestion;
    private UI ui;
    private int score;

    public void startGame() {
        currentQuestion = generateQuestion();
        ui = new UI(this);
        score = 0;

        updateUI();
    }

    private MathQuestion generateQuestion() {
        int number1 = (int) (Math.random() * 10) + 1;
        int number2 = (int) (Math.random() * 10) + 1;
        int operator = (int) (Math.random() * 4); // 0: suma, 1: resta, 2: multiplicación, 3: división

        String operatorSymbol;
        int correctAnswer;

        switch (operator) {
            case 0:
                operatorSymbol = "+";
                correctAnswer = number1 + number2;
                break;
            case 1:
                operatorSymbol = "-";
                correctAnswer = number1 - number2;
                break;
            case 2:
                operatorSymbol = "*";
                correctAnswer = number1 * number2;
                break;
            case 3:
                operatorSymbol = "/";
                correctAnswer = number1 / number2;
                break;
            default:
                operatorSymbol = "+";
                correctAnswer = number1 + number2;
                break;
        }

        return new MathQuestion(number1, number2, operatorSymbol, correctAnswer);
    }

    private void updateUI() {
        ui.updateQuestionLabel(currentQuestion.getQuestion());
        ui.updateScoreLabel("Puntaje: " + score); // Actualizar el campo del puntaje
        ui.clearAnswerField();
        ui.requestFocusOnAnswerField();
    }

    private void evaluateAnswer() {
        int userAnswer = ui.getAnswer();
        int correctAnswer = currentQuestion.getCorrectAnswer();

        if (userAnswer == correctAnswer) {
            ui.showResultMessage("¡Respuesta correcta!", JOptionPane.INFORMATION_MESSAGE);
            score += 5;
        } else {
            ui.showResultMessage("Respuesta incorrecta. La respuesta correcta es " + correctAnswer, JOptionPane.ERROR_MESSAGE);
            endGame();
        }

        currentQuestion = generateQuestion();
        updateUI();
    }

    private void endGame() {
        ui.showResultMessage("Fin del juego. Puntuación: " + score, JOptionPane.INFORMATION_MESSAGE);
        ui.disableCheckButton();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ui.getCheckButton()) {
            evaluateAnswer();
        }
    }
}

class MathQuestion {
    private int number1;
    private int number2;
    private String operatorSymbol;
    private int correctAnswer;

    public MathQuestion(int number1, int number2, String operatorSymbol, int correctAnswer) {
        this.number1 = number1;
        this.number2 = number2;
        this.operatorSymbol = operatorSymbol;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return number1 + " " + operatorSymbol + " " + number2 + " = ";
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

class UI {
    private JFrame frame;
    private JLabel questionLabel;
    private JLabel scoreLabel; // Nuevo campo para mostrar el puntaje
    private JTextField answerField;
    private JButton checkButton;

public UI(ActionListener actionListener) {
        frame = new JFrame("Math Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1200, 720));

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(contentPanel, BorderLayout.CENTER);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setForeground(Color.BLUE);
        contentPanel.add(questionLabel);

        scoreLabel = new JLabel("Puntaje: 0"); // Etiqueta inicial para mostrar el puntaje
        contentPanel.add(scoreLabel);

        answerField = new JTextField(10);
        contentPanel.add(answerField);

        checkButton = new JButton("Comprobar");
        checkButton.addActionListener(actionListener);
        checkButton.setPreferredSize(new Dimension(120, 40));
        checkButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        checkButton.setForeground(Color.RED);
        contentPanel.add(checkButton);

        frame.getRootPane().setDefaultButton(checkButton);

        frame.pack();
        frame.setVisible(true);
    }

    public void updateQuestionLabel(String question) {
        questionLabel.setText(question);
    }

    public void updateScoreLabel(String score) {
        scoreLabel.setText(score);
    }

    public void clearAnswerField() {
        answerField.setText("");
    }

    public void requestFocusOnAnswerField() {
        answerField.requestFocus();
    }

    public int getAnswer() {
        try {
            return Integer.parseInt(answerField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public JButton getCheckButton() {
        return checkButton;
    }

    public void disableCheckButton() {
        checkButton.setEnabled(false);
    }

    public void showResultMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(frame, message, "Resultado", messageType);
    }
}
