import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MiniMaxGame {
    char W = pieceTypes.WHITE.value;
    char B = pieceTypes.BLACK.value;
    char x = pieceTypes.EMPTY.value;
    private int positionsEvaluated = 0;
    private static int minimaxEstimate = 0;

    public static void main(String[] args) {
        try {
            char[] board = readFromFile(args[0]);
            String outputfile = args[1];
            int depth = Integer.parseInt(args[2]);

            MiniMaxGame game = new MiniMaxGame();
            char[] output = game.MaxMin(board, depth);

            writeToFile(outputfile, output);

            System.out.println("Input position:  " + new String(board));
            System.out.println("Output position: " + new String(readFromFile(outputfile)));
            System.out.println("Positions evaluated by static estimation: " + game.getPositionsEvaluated());
            System.out.println("MINIMAX estimate: " + minimaxEstimate);

        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a number between 1 and 24.");
        }
    }

    public ArrayList<String> generateMovesMidgameEndgame(char[] board) {
        int numW = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == W) {
                numW++;
            }
        }
        if (numW == 3) {
            return generateHopping(board);
        } else {
            return generateMove(board);
        }
    }

    public ArrayList<String> generateHopping(char[] board) {
        ArrayList<String> list = new ArrayList<String>();
        char[] copy;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == W) {
                for (int j = 0; j < board.length; j++) {
                    if (board[j] == x) {
                        copy = board.clone();
                        copy[i] = x;
                        copy[j] = W;
                        if (!closeMill(j, copy)) {
                            list.add(new String(copy));
                        }else{
                            generateRemove(copy, list);
                        }
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<String> generateMove(char[] board) {
        ArrayList<String> list = new ArrayList<>();
        char copy[];
		for(int i=0;i<board.length;i++) {
			if(board[i]=='W') {
				for(int j=0;j<board.length;j++) {
					if(board[j]==x) {
						copy = board.clone();
						copy[i]=x;
						copy[j]=W;

						if (!closeMill(j, copy)) {
                            list.add(new String(copy));
                        }else{
                            generateRemove(copy, list);
                        }
					}
				}
			}
		}
		return list;
    }

    public static int[] neighbors(int i) {
        switch(i) {
            case 0:
                return new int[]{1, 3, 8};
            case 1:
                return new int[]{0, 2, 4};
            case 2:
                return new int[]{1, 5, 13};
            case 3:
                return new int[]{0, 4, 6, 9};
            case 4:
                return new int[]{1, 3, 5};
            case 5:
                return new int[]{2, 4, 7, 12};
            case 6:
                return new int[]{3, 7, 10};
            case 7:
                return new int[]{5, 6, 11};
            case 8:
                return new int[]{0, 9, 20};
            case 9:
                return new int[]{3, 8, 10, 17};
            case 10:
                return new int[]{6, 9, 14};
            case 11:
                return new int[]{7, 12, 16};
            case 12:
                return new int[]{5, 11, 13, 19};
            case 13:
                return new int[]{2, 12, 22};
            case 14:
                return new int[]{10, 15, 17};
            case 15:
                return new int[]{14, 16, 18};
            case 16:
                return new int[]{11, 15, 19};
            case 17:
                return new int[]{9, 14, 18, 20};
            case 18:
                return new int[]{15, 17, 19, 21};
            case 19:
                return new int[]{12, 16, 18, 22};
            case 20:
                return new int[]{8, 17, 21};
            case 21:
                return new int[]{18, 20, 22};
            case 22:
                return new int[]{13, 19, 21};
            default:
                return new int[]{};
        }
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
        ArrayList<String> boardConfigs = generateMovesMidgameEndgame(tempB);
        ArrayList<String> swappedConfigs = new ArrayList<String>();
        for (String s : boardConfigs) {
            char[] temp = s.toCharArray();
            swappedConfigs.add(new String(swapPiecePositions(temp)));
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
        int numWhite = 0;
        int numBlack = 0;
        
        // Count the number of white and black pieces
        for (char piece : board) {
            if (piece == 'W') {
                numWhite++;
            } else if (piece == 'B') {
                numBlack++;
            }
        }
    
        if (numBlack <= 2) {
            return 10000;
        } else if (numWhite <= 2) {
            return -10000;
        } else {
            ArrayList<String> blackMoves = generateBlackMoves(board);
            int numBlackMoves = blackMoves.toArray().length;
    
            if (numBlackMoves == 0) {
                return 10000;
            } else {
                return (1000 * (numWhite - numBlack) - numBlackMoves);
            }
        }
    }
    
    public int getPositionsEvaluated() {
        return positionsEvaluated;
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
            ArrayList<String> positions = generateMovesMidgameEndgame(board);

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
