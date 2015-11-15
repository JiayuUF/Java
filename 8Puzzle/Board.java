import java.util.ArrayList;
import java.util.List;
//import edu.princeton.cs.algs4.StdOut;   // used in main

public class Board {
    private int N;
    private final int[][] board;
    private int[][] copyBoard(int[][] src) {
        int[][] tar = new int[src.length][src.length];
        for (int i = 0; i < src.length; i++)
            for (int j = 0; j < src.length; j++) tar[i][j] = src[i][j];
        return tar;
    }
    
    public Board(int[][] blocks) {          
// construct a board from an N-by-N array of blocks
// (where blocks[i][j] = block in row i, column j)
        this.N = blocks.length;          //length
        this.board = copyBoard(blocks);
    }
    
    public int dimension() {                
// board dimension N
    return N;
    }
    
    public int hamming() {                  
// number of blocks out of place
        int ham = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != i*N+j+1) ham++;
            }
        }
        return ham;
    }
    
    public int manhattan() {                
// sum of Manhattan distances between blocks and goal
        int manh = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.board[i][j] == 0) continue;
                int trueI = (this.board[i][j]-1) / N;
                int trueJ = (this.board[i][j]-1) % N;
                manh += Math.abs(trueI - i) + Math.abs(trueJ - j);
            }
        }
        return manh;
    }
    
    public boolean isGoal() {               
// is this board the goal board?
        return (this.hamming() == 0);
    }
    
    public Board twin() {                   
// a board that is obtained by exchanging any pair of blocks
        int[][] newBoard = copyBoard(this.board);
        boolean flag = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N-1; j++) {
                if (newBoard[i][j] > 0 && newBoard[i][j+1] > 0) {
                    int tmp = newBoard[i][j];
                    newBoard[i][j] = newBoard[i][j+1];
                    newBoard[i][j+1] = tmp;
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        return new Board(newBoard);   //create a new Board type to return
        //Board twin = new Board(newBoard);
        //return twin;
    }
    
    public boolean equals(Object y) {       
// does this board equal y?
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    public Iterable<Board> neighbors() {    
// all neighboring boards
        List<Board> neighbors = new ArrayList<Board>();
        int[] mx = {1, -1, 0, 0};
        int[] my = {0, 0, 1, -1};
        boolean findZero = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.board[i][j] == 0) {
                    for (int k = 0; k < 4; k++) {
                        int ni = i + mx[k];
                        int nj = j + my[k];
                        if (ni >= 0 && nj >= 0 && ni < N && nj < N) {
                            int[][] newB = copyBoard(this.board);
                            newB[i][j] = this.board[ni][nj];
                            newB[ni][nj] = this.board[i][j];
                            Board newBoard = new Board(newB);
                            neighbors.add(newBoard);
                        }
                    }
                    findZero = true;
                    break;
                }
            }
            if (findZero) break;
        }
        return neighbors;
    }
    public String toString() {              
// string representation of this board 
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", this.board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
/*
    public static void main(String[] args) { 
// unit tests 
        int[][] input = new int[][]{{1, 2, 3, 4}, {5, 6, 0, 8}, {9, 10, 11, 12}, {13, 14, 15, 7}};
        Board testBoard = new Board(input);
        StdOut.println(testBoard.toString());
        Iterable<Board> result = testBoard.neighbors();
        for (Board b : result) {
            StdOut.println(b.toString());
        }
    }
    */
}