import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;
import edu.princeton.cs.algs4.*; // used for test
    
public class Solver {
    private class Node implements Comparable<Node> {
        private Board board;
        private Node previous;
        private int moveNum;
        public Node(Board board, Node previous) {
            this.board = board;
            this.previous = previous;
            if (previous == null)  // the beginning
                this.moveNum = 0;
            else 
                this.moveNum = previous.moveNum + 1;
        }
        @Override
        public int compareTo(Node that) {
            return (this.board.manhattan() - that.board.manhattan()) + (this.moveNum - that.moveNum);
        }
    }
    private Node insertNeighbor(MinPQ<Node> queue) {
        if (queue.isEmpty()) return null;
        Node current = queue.delMin();
        //StdOut.println(current.board);
        if (current.board.isGoal()) return current;
        //StdOut.println("test1");
        for (Board b : current.board.neighbors()) {
            if ((current.previous == null) || !b.equals(current.previous.board))
                // if current.previous == null, there is no current.previous.board
                queue.insert(new Node(b, current));
            //StdOut.println("neighbors:");
            //StdOut.println(b);
        }
        return null;
    }
    private Node endNode = null;  //used for trace back
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        MinPQ<Node> originQ = new MinPQ<Node>();
        MinPQ<Node> twinQ = new MinPQ<Node>();
        originQ.insert(new Node(initial, null));
        twinQ.insert(new Node(initial.twin(), null));
        
        boolean originNotSolved = true;
        boolean twinNotSolved = true;
        while (originNotSolved && twinNotSolved) { //when neigher one has reached the goal, continue 
            endNode = insertNeighbor(originQ);
            originNotSolved = (endNode == null);
            twinNotSolved = (insertNeighbor(twinQ) == null);
        }
    }
    
    public boolean isSolvable() {
        // is the initial board solvable?
        return endNode != null;
    }
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable()) 
            return endNode.moveNum;
        else return -1;
    }
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable()) {
            Stack<Board> tmp = new Stack<Board>();
            for (Node tail = endNode; tail != null; tail = tail.previous) {
                tmp.push(tail.board);
            }
            Stack<Board> result = new Stack<Board>();
            //Board tmpB = null;
            while (!tmp.isEmpty()) {
                result.push(tmp.pop());
            }
            return result;
        }
        else 
            return null;
    }
    
    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In("./puzzle12.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}