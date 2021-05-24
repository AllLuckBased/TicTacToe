import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TicTacToe {

    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JLabel textField = new JLabel();
    JPanel button_panel = new JPanel();
    JButton[] buttons = new JButton[9];

    GameState gameState;
    String playerSymbol, computerSymbol;

    public TicTacToe() {
        frame.setVisible(true);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(53, 53, 53));

        frame.setLayout(new BorderLayout());

        title_panel.setLayout(new BorderLayout());

        textField.setOpaque(true);
        textField.setText("Tic-Tac-Toe");
        textField.setForeground(Color.green);
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setBackground(new Color(25, 25, 25));
        textField.setFont(new Font("Ink Free", Font.BOLD, 75));

        title_panel.add(textField);

        button_panel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();

            button.setName("" + i);
            button.addActionListener(e -> {
                if (gameState.isPlayersTurn() && button.getText().equals("")) {
                    gameState.setValue(true, Integer.parseInt(button.getName()));
                    button.setForeground(new Color(255, 0, 0));
                    button.setText(playerSymbol);

                    if(isGameOngoing()) {
                        textField.setText(computerSymbol + " turn");
                        makeEngineMove();
                    }
                }
            });
            button.setFont(new Font("MV Boli", Font.BOLD, 120));

            buttons[i] = button;
            button_panel.add(button);
        }

        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);

        Random random = new Random();
        playerSymbol = random.nextInt(2) == 0 ? "X" : "O";
        gameState = random.nextInt(2) == 0 ? new GameState(true) : new GameState(false);

        computerSymbol = playerSymbol.equals("X") ? "O" : "X";
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        textField.setText(gameState.isPlayersTurn() ? playerSymbol + " turn" : computerSymbol + " turn");
        if(!gameState.isPlayersTurn()) makeEngineMove();
    }

    public boolean isGameOngoing() {
        if(gameState.isGameOver()) {
            int result = gameState.evaluateResult();
            switch (result) {
                case -1 -> playerWins(gameState.winningCombination);
                case 0 -> draw();
                case 1 -> computerWins(gameState.winningCombination);
                default -> throw new RuntimeException("Undefined result: " + result);
            }
            return false;
        }
        return true;
    }

    public void playerWins(int[] combination) {
        buttons[combination[0]].setBackground(Color.green);
        buttons[combination[1]].setBackground(Color.green);
        buttons[combination[2]].setBackground(Color.green);

        for(int i= 0; i<9;i++){
            buttons[i].setEnabled(false);
        }
        textField.setText("Congrats!!  You Win!");
    }
    public void computerWins(int[] combination) {

        buttons[combination[0]].setBackground(Color.red);
        buttons[combination[1]].setBackground(Color.red);
        buttons[combination[2]].setBackground(Color.red);

        for(int i= 0; i<9;i++){
            buttons[i].setEnabled(false);
        }
        textField.setText("Tough Luck!!  You Lose!");
    }

    public void draw() {
        for(int i= 0; i<9;i++){
            buttons[i].setBackground(Color.lightGray);
            buttons[i].setEnabled(false);
        }
        textField.setText("Meh!!  Its a draw!");
    }

    public void makeEngineMove() {
        gameState.minimax(true, true);
        int computerMovePosition = gameState.bestMoveChildState.getLastMovePosition();
        gameState.setValue(false, computerMovePosition);
        buttons[computerMovePosition].setForeground(new Color(255, 0, 0));
        buttons[computerMovePosition].setText(computerSymbol);
        if(isGameOngoing()) textField.setText(playerSymbol + " turn");
    }
}