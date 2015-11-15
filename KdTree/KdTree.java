import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut; //for test
import edu.princeton.cs.algs4.StdDraw; //for test

public class KdTree {
    private Node root;  //public for test
    private Node nearest;
    private int numNodes;
    private LinkedList<Point2D> pointList;
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONAL = false;
    /*private class Node {      //updated
        public Point2D point;
        public Node left, right;
        public RectHV rect;
        public boolean direction;
        public Node(Point2D p, boolean d, RectHV r) {
            this.point = p;
            this.left = null;
            this.right = null;
            this.rect = r;
            this.direction = d;
        }
    }*/
    private class Node {
        private Point2D point;
        private Node left, right;
        private RectHV rect;
        private boolean direction;
        public Node(Point2D p, boolean d, RectHV r) {
            this.point = p;
            this.left = null;
            this.right = null;
            this.rect = r;
            this.direction = d;
        }
        public void setPoint(Point2D p) { 
            this.point = p; 
        }
        public void setLeft(Node l) { 
            this.left = l; 
        }
        public void setRight(Node r) { 
            this.right = r; 
        }
        public void setRect(RectHV r) { 
            this.rect = r; 
        }
        public void setDirection(boolean d) { 
            this.direction = d; 
        }
        public Point2D getPoint() { 
            return this.point; 
        }
        public Node getLeft() { 
            return this.left; 
        }
        public Node getRight() { 
            return this.right; 
        }
        public RectHV getRect() { 
            return this.rect; 
        }
        public boolean getDirection() { 
            return this.direction; 
        }
    }
    public KdTree() {
        // construct an empty set of points 
        root = null;
        numNodes = 0;
    }
    public boolean isEmpty() {
        // is the set empty? 
        return (numNodes == 0);
    }
    public int size() {         
        // number of points in the set 
        return numNodes;
    }
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        Node newNode = null;
        RectHV newRect = null;
        if (numNodes == 0) {
            //if insert as root
            //RectHV newRect = null;
            newRect = new RectHV(0.0, 0.0, 1.0, 1.0);
            newNode = new Node(p, VERTICAL, newRect);
            root = newNode;
            numNodes++;
            return;
        }
        //if insert into existing tree
        Node currentNode = root;
        while (true) {
            if (p.equals(currentNode.getPoint()))
                //if the node is already inserted
                return;
            if (currentNode.getDirection() == VERTICAL) {
                if (p.x() < currentNode.getPoint().x()) {
                    //go left
                    if (currentNode.getLeft() == null) {
                        //RectHV newRect = null;
                        //StdOut.println(currentNode.rect);
                        newRect = new RectHV(currentNode.getRect().xmin(), currentNode.getRect().ymin(), 
                                             currentNode.getPoint().x(), currentNode.getRect().ymax());
                        newNode = new Node(p, HORIZONAL, newRect);
                        currentNode.setLeft(newNode);
                        numNodes++;
                        return;
                    }
                    else
                        currentNode = currentNode.getLeft();
                }
                else {
                    //go right
                    if (currentNode.getRight() == null) {
                        //RectHV newRect = null;
                        newRect = new RectHV(currentNode.getPoint().x(), currentNode.getRect().ymin(), 
                                             currentNode.getRect().xmax(), currentNode.getRect().ymax());
                        newNode = new Node(p, HORIZONAL, newRect);
                        currentNode.setRight(newNode);
                        numNodes++;
                        return;
                    }
                    else
                        currentNode = currentNode.getRight();
                }
            }
            else {
                //if currentNode is HORIZONAL
                if (p.y() < currentNode.getPoint().y()) {
                    //go down
                    if (currentNode.getLeft() == null) {
                        //RectHV newRect = null;
                        newRect = new RectHV(currentNode.getRect().xmin(), currentNode.getRect().ymin(), 
                                             currentNode.getRect().xmax(), currentNode.getPoint().y());
                        newNode = new Node(p, VERTICAL, newRect);
                        currentNode.setLeft(newNode);
                        numNodes++;
                        return;
                    }
                    else
                        currentNode = currentNode.getLeft();
                }
                else {
                    //go up
                    if (currentNode.getRight() == null) {
                        //RectHV newRect = null;
                        newRect = new RectHV(currentNode.getRect().xmin(), currentNode.getPoint().y(), 
                                             currentNode.getRect().xmax(), currentNode.getRect().ymax());
                        newNode = new Node(p, VERTICAL, newRect);
                        currentNode.setRight(newNode);
                        numNodes++;
                        return;
                    }
                    else
                        currentNode = currentNode.getRight();
                }
            }
        }
    }
    public boolean contains(Point2D p) {      
        // does the set contain point p? 
        Node currentNode = root;
        while (currentNode != null) {
            if (currentNode.getPoint().equals(p)) 
                return true;
            if (currentNode.getDirection() == VERTICAL) {
                if (p.x() < currentNode.getPoint().x()) 
                    currentNode = currentNode.getLeft();
                else
                    currentNode = currentNode.getRight();
            }
            else { //HORIZONAL
                if (p.y() < currentNode.getPoint().y()) 
                    currentNode = currentNode.getLeft();
                else
                    currentNode = currentNode.getRight();
            }
        }
        return false;
    }
    public void draw() {
        // draw all points to standard draw 
        // use range() to draw, need draw red/blue lines
        RectHV unit = new RectHV(0, 0, 1, 1);
        for (Point2D p : range(unit)) 
            p.draw();
    }
    private void searchInRange(Node node, RectHV rect) {
        if (node == null) 
            return;
        if (rect.contains(node.getPoint())) 
            pointList.add(node.getPoint());
        if (node.getLeft() != null) {
            if (rect.intersects(node.getLeft().getRect()))
                searchInRange(node.getLeft(), rect);
        }
        if (node.getRight() != null) {
            if (rect.intersects(node.getRight().getRect()))
                searchInRange(node.getRight(), rect);
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        pointList = new LinkedList<Point2D>();
        searchInRange(root, rect);
        return pointList;
    }
    private void searchNearest(Node current, Point2D goal) {
        boolean leftFirst; // true means left subtree first, false means right subtree first
        if (current == null)
            return;
        if (goal.distanceTo(current.getPoint()) < goal.distanceTo(nearest.getPoint()))
            nearest = current;
        if (current.getDirection() == VERTICAL) {
            // VERTICAL
            //StdOut.println("VERTICAL");
            if (goal.x() < current.getPoint().x())
                leftFirst = true;
            else
                leftFirst = false;
        }
        else {
            // HORIZONAL
            //StdOut.println("HORIZONAL");
            if (goal.y() < current.getPoint().y())
                leftFirst = true;
            else
                leftFirst = false;
        }
        //StdOut.println(goal.distanceTo(nearest.point));
        //StdOut.println(leftFirst);
        if (leftFirst) {
            // left subtree first
            if (current.getLeft() != null) {
                if (current.getLeft().getRect().distanceTo(goal) < goal.distanceTo(nearest.getPoint())) 
                    searchNearest(current.getLeft(), goal);
            }
            if (current.getRight() != null) {
                if (current.getRight().getRect().distanceTo(goal) < goal.distanceTo(nearest.getPoint())) 
                    searchNearest(current.getRight(), goal);
            }
        }
        else {
            // right subtree first 
            if (current.getRight() != null) {
                if (current.getRight().getRect().distanceTo(goal) < goal.distanceTo(nearest.getPoint())) 
                    searchNearest(current.getRight(), goal);
            }
            if (current.getLeft() != null) {
                if (current.getLeft().getRect().distanceTo(goal) < goal.distanceTo(nearest.getPoint())) 
                    searchNearest(current.getLeft(), goal);
            }
        }
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        nearest = root;
        searchNearest(root, p);
        if (nearest == null)
            return null;
        return nearest.getPoint();
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.4, 0.2));
        StdOut.println(tree.size());
        //StdOut.println(tree.root.point);
        tree.insert(new Point2D(0.2, 0.1));
        tree.insert(new Point2D(0.7, 0.3));
        tree.insert(new Point2D(0.3, 0.6));
        
        StdOut.println("Find existing point: " + tree.contains(new Point2D(0.7, 0.3)));
        StdOut.println("Find non-existant p: " + !tree.contains(new Point2D(0.2, 0.6)));
        StdOut.println("Count of nodes = 4 : " + tree.size());
        
        tree.insert(new Point2D(0.3, 0.6));
        
        StdOut.println("Cannot insert same : " + tree.size());
        
        StdDraw.setPenRadius(0.01);
        tree.draw();
        
        RectHV rect = new RectHV(0.3, 0.1, 0.9, 0.9);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.002);
        rect.draw();
        
        for (Point2D point : tree.range(rect)) {
            StdOut.println(point.toString());
        }
        
        StdOut.println();
        
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        Point2D comp = new Point2D(0.4, 0.5);
        comp.draw();
        
        StdOut.println(tree.nearest(comp).toString());
    }
}