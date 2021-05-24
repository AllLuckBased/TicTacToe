import java.util.ArrayList;
import java.util.List;

public class GameState {

    private final Boolean[] boardStateBoolean;
    private boolean playersTurn;
    List<Integer> moveSequence;

    GameState(boolean playersTurn) {
        moveSequence = new ArrayList<>(9);
        boardStateBoolean = new Boolean[9];
        this.playersTurn = playersTurn;
    }

    private GameState copy() {
        GameState copiedState = new GameState(playersTurn);
        System.arraycopy(boardStateBoolean, 0, copiedState.boardStateBoolean, 0, 9);
        return copiedState;
    }

    void setValue(boolean playersTurn, int position) {
        if(this.playersTurn != playersTurn) throw new RuntimeException("Not correct player's turn!");
        boardStateBoolean[position] = playersTurn;
        this.playersTurn = !playersTurn;
        moveSequence.add(position);
    }

    int getLastMovePosition() {
        return moveSequence.get(moveSequence.size()-1);
    }

    boolean isPlayersTurn() {
        return playersTurn;
    }

    int[] winningCombination;
    int[][] winningCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 4, 8}, {2, 4, 6}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
    boolean isGameOver() {
        for(int[] combination : winningCombinations) {
            Boolean a = boardStateBoolean[combination[0]], b = boardStateBoolean[combination[1]], c = boardStateBoolean[combination[2]];
            if(a == null || b == null || c == null) continue;
            if(a.equals(b) && b.equals(c)) return true;
        }
        for(int i = 0; i < 9; i++) {
            if(boardStateBoolean[i] == null) break;
            else if(i == 8) return true;
        }
        return false;
    }
    int evaluateResult() {
        for(int[] combination : winningCombinations) {
            Boolean a = boardStateBoolean[combination[0]], b = boardStateBoolean[combination[1]], c = boardStateBoolean[combination[2]];
            if(a == null || b == null || c == null) continue;
            if(a.equals(b) && b.equals(c)) {
                winningCombination = combination;
                return a ? -1 : 1;
            }
        }
        for(int i = 0; i < 9; i++) {
            if(boardStateBoolean[i] == null) break;
            else if(i == 8) return 0;
        }
        throw new RuntimeException("Cannot evaluate position if game is ongoing!");
    }

    List<GameState> generateChildren() {
        List<GameState> childStates = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if(boardStateBoolean[i] == null) {
                GameState childState = copy();
                childState.setValue(playersTurn, i);
                childStates.add(childState);
            }
        }
        return childStates;
    }

    GameState bestMoveChildState;
    int minimax(boolean maximizingPlayersTurn, boolean initialCall) {
        if(isGameOver()) return evaluateResult();

        if(maximizingPlayersTurn) {
            int maxEval = -1;
            for(GameState childState : generateChildren()) {
                int eval = childState.minimax(false, false);
                if(initialCall && eval > maxEval) bestMoveChildState = childState;
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = 1;
            for(GameState childState : generateChildren()) {
                int eval = childState.minimax(true, false);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }
}