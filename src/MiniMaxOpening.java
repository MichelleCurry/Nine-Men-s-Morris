import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MiniMaxOpening {
    char W = pieceTypes.WHITE.value;
    char B = pieceTypes.BLACK.value;
    char x = pieceTypes.EMPTY.value;
    private int positionsEvaluated = 0;
    private int minimaxEstimate = 0;

    public static void main(String[] args) {
        try {
            char[] board = readFromFile(args[0]);
            String outputfile = args[1];
            int depth = Integer.parseInt(args[2]);
    
            MiniMaxOpening game = new MiniMaxOpening();
            char[] output = game.MaxMin(board, depth);
    
            writeToFile(outputfile, output);
    
            System.out.println("Input position: " + new String(board));
            System.out.println("Output position: " + new String(readFromFile(outputfile)));
            System.out.println("Positions evaluated by static estimation: " + game.getPositionsEvaluated());
            System.out.println("MINIMAX estimate: " + game.getMinimaxEstimate());
    
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a number between 1 and 24.");
        }
    }
    
    public ArrayList<String> generateAdd(char[] board) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == x) {
                char[] copy = board.clone();
                copy[i] = W;
                if (closeMill(i, copy)) {
                    list = generateRemove(copy, list);
                } else {
                    list.add(new String(copy));
                }
            }
        }
        return list;
    }

    public char[] swapPiecePositions(char[] board) {
        char[] tempB = board.clone();
        for (int i = 0; i < tempB.length; i++) {
            if (tempB[i] == W) {
                tempB[i] = B;
                continue;
            }
            if (tempB[i] == B) {
                tempB[i] = W;
            }
        }
        return tempB;
    }

    public ArrayList<String> generateBlackMoves(char[] tempB) {
        ArrayList<String> boardConfigs = generateAdd(tempB);
        ArrayList<String> swappedConfigs = new ArrayList<String>();
        for (String s : boardConfigs) {
            char[] tempB2 = s.toCharArray();
            swappedConfigs.add(new String(swapPiecePositions(tempB2)));
        }
        return swappedConfigs;
    }

    public ArrayList<String> generateRemove(char[] board, ArrayList<String> list) {
        int num = list.size();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == B) {
                if (!closeMill(i, board)) {
                    char[] copy = board.clone();
                    copy[i] = x;
                    list.add(new String(copy));
                }
                if (num == list.size()) {
                    for (int j = 0; j < board.length; j++) {
                        if (board[j] == B) {
                            char[] copy = board.clone();
                            copy[j] = x;
                            list.add(new String(copy));
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    public int staticEstimation(char[] board) {
        int numW = 0;
        int numB = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i] == W) {
                numW++;
            }
            if (board[i] == B) {
                numB++;
            }
        }
        return numW - numB;
    }

    public int getPositionsEvaluated() {
        return positionsEvaluated;
    }

    public int getMinimaxEstimate() {
        return minimaxEstimate;
    }

    public boolean closeMill(int pos, char[] b) {
        char C = b[pos];

        switch (pos) {
            case (0): // a0
                if ((b[1] == C && b[2] == C) || (b[8] == C && b[20] == C) || (b[3] == C && b[6] == C)) {
                    return true;
                }
                break;
            case (1): // d0
                if ((b[0] == C && b[2] == C)) {
                    return true;
                }
                break;
            case (2): // g0
                if ((b[0] == C && b[1] == C) || (b[5] == C && b[7] == C) || (b[13] == C && b[22] == C)) {
                    return true;
                }
                break;
            case (3): // b1
                if ((b[0] == C && b[6] == C) || (b[4] == C && b[5] == C) || (b[9] == C && b[17] == C)) {
                    return true;
                }
                break;
            case (4): // d1
                if ((b[3] == C && b[5] == C)) {
                    return true;
                }
                break;
            case (5): // f1
                if ((b[3] == C && b[4] == C) || (b[12] == C && b[19] == C) || (b[2] == C && b[7] == C)) {
                    return true;
                }
                break;
            case (6): // c2
                if ((b[0] == C && b[3] == C) || (b[10] == C && b[14] == C)) {
                    return true;
                }
                break;
            case (7): // e2
                if ((b[2] == C && b[5] == C) || (b[11] == C && b[16] == C)) {
                    return true;
                }
                break;
            case (8): // a3
                if ((b[0] == C && b[20] == C) || (b[9] == C && b[10] == C)) {
                    return true;
                }
                break;
            case (9): // b3
                if ((b[8] == C && b[10] == C) || (b[3] == C && b[17] == C)) {
                    return true;
                }
                break;
            case (10): // c3
                if ((b[8] == C && b[9] == C) || (b[6] == C && b[14] == C)) {
                    return true;
                }
                break;
            case (11): // e3
                if ((b[7] == C && b[16] == C) || (b[12] == C && b[13] == C)) {
                    return true;
                }
                break;
            case (12): // f3
                if ((b[11] == C && b[13] == C) || (b[5] == C && b[19] == C)) {
                    return true;
                }
                break;
            case (13): // g3
                if ((b[11] == C && b[12] == C) || (b[2] == C && b[22] == C)) {
                    return true;
                }
                break;
            case (14): // c4
                if ((b[6] == C && b[10] == C) || (b[15] == C && b[16] == C) || (b[17] == C && b[20] == C)) {
                    return true;
                }
                break;
            case (15): // d4
                if ((b[14] == C && b[16] == C) || (b[18] == C && b[21] == C)) {
                    return true;
                }
                break;
            case (16): // e4
                if ((b[14] == C && b[15] == C) || (b[7] == C && b[11] == C) || (b[19] == C && b[22] == C)) {
                    return true;
                }
                break;
            case (17): // b5
                if ((b[3] == C && b[9] == C) || (b[14] == C && b[20] == C) || (b[18] == C && b[19] == C)) {
                    return true;
                }
                break;
            case (18): // d5
                if ((b[17] == C && b[19] == C) || (b[15] == C && b[21] == C)) {
                    return true;
                }
                break;
            case (19): // f5
                if ((b[17] == C && b[18] == C) || (b[5] == C && b[12] == C) || (b[16] == C && b[22] == C)) {
                    return true;
                }
                break;
            case (20): // a6
                if ((b[0] == C && b[8] == C) || (b[14] == C && b[17] == C) || (b[21] == C && b[22] == C)) {
                    return true;
                }
                break;
            case (21): // d6
                if ((b[20] == C && b[22] == C) || (b[15] == C && b[18] == C)) {
                    return true;
                }
                break;
            case (22): // g6
                if ((b[20] == C && b[21] == C) || (b[2] == C && b[13] == C) || (b[16] == C && b[19] == C)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public char[] MaxMin(char[] board, int depth) {
        if (depth > 0) {
            depth--;
            ArrayList<String> positions = generateAdd(board);
            char[] minB;
            char[] maxB = new char[board.length];
            int eval = Integer.MIN_VALUE;

            for (String child : positions) {
                minB = MinMax(child.toCharArray(), depth);
                int estimation = staticEstimation(minB);
                if (eval < estimation) {
                    eval = estimation;
                    minimaxEstimate = eval;
                    maxB = child.toCharArray();
                }
            }
            return maxB;
        } else if (depth == 0) {
            positionsEvaluated++;
        }
        return board;
    }

    public char[] MinMax(char[] board, int depth) {
        if (depth > 0) {
            depth--;
            ArrayList<String> children = generateBlackMoves(board);
            char[] maxB;
            char[] minB = new char[board.length];
            int eval = Integer.MAX_VALUE;

            for (String child : children) {
                maxB = MaxMin(child.toCharArray(), depth);
                int estimation = staticEstimation(maxB);
                if (eval > estimation) {
                    eval = estimation;
                    minimaxEstimate = eval;
                    minB = child.toCharArray();
                }
            }
            return minB;
        } else if (depth == 0) {
            positionsEvaluated++;
        }
        return board;
    }
    
    public static char[] readFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        char[] Board = new char[23];

        String line = reader.readLine();

        if (line != null && line.length() == 23) {
            Board = line.toCharArray();
        } else {
            reader.close();
            throw new IOException("Invalid content in the file. "+line.length());
        }

        reader.close();
        return Board;
    }

    public static void writeToFile(String fileName, char[] array) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(array);
        writer.close();
    }

    public enum pieceTypes {
        WHITE('W'),
        BLACK('B'),
        EMPTY('x');
    
        public final char value;
    
        pieceTypes(char value) {
            this.value = value;
        }
    }
}

// if(maximizingPlayer){
// int maxEval = Integer.MIN_VALUE;
// char[] maxBoard = board.clone();
// ArrayList<String> boardConfigs = generateBlackMoves(board);
// for(String s : boardConfigs){
// char[] tempB = s.toCharArray();
// int eval = MiniMax(tempB, depth-1, false).length;
// if(eval > maxEval){
// maxEval = eval;
// maxBoard = tempB.clone();
// }
// }
// return maxBoard;
// }
// else{
// int minEval = Integer.MAX_VALUE;
// char[] minBoard = board.clone();
// ArrayList<String> boardConfigs = generateBlackMoves(board);
// for(String s : boardConfigs){
// char[] tempB = s.toCharArray();
// int eval = MiniMax(tempB, depth-1, true).length;
// if(eval < minEval){
// minEval = eval;
// minBoard = tempB.clone();
// }
// }
// return minBoard;
// }