package sudokuNorvig;

public class SudokuBacktracker {
    private char[][] board;

    public SudokuBacktracker(String s){
        board = new char[9][9];

        for (int i = 0; i < 9; i++){

            for (int j = 0; j < 9; j++){
                char c  = s.charAt(i*9 + j);
                board[i][j] = c;
            }
        }
    }

    public boolean solve(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == '.'){
                    for(char c = '1'; c <= '9'; c++){//trial. Try 1 through 9
                        if(isValid(i, j, c)){
                            board[i][j] = c; //Put c for this cell

                            if(solve())
                                return true; //If it's the solution return true
                            else
                                board[i][j] = '.'; //Otherwise go back
                        }
                    }

                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, char c){
        for(int i = 0; i < 9; i++) {
            if(board[i][col] != '.' && board[i][col] == c) return false; //check row
            if(board[row][i] != '.' && board[row][i] == c) return false; //check column
            if(board[3 * (row / 3) + i / 3][ 3 * (col / 3) + i % 3] != '.' &&
                    board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) return false; //check 3*3 block
        }
        return true;
    }


    public char[][] getBoard(){
        return board;
    }
}
