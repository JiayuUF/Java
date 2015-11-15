import java.util.LinkedList;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom; //for test
import edu.princeton.cs.algs4.StdOut; //for test

public class PointSET {
    private TreeSet<Point2D> points;
    public PointSET() {
        // construct an empty set of points 
        points = new TreeSet<Point2D>();
    }
    public boolean isEmpty() {
        // is the set empty? 
        return points.isEmpty();
    }
    public int size() {
        // number of points in the set 
        return points.size();
    }
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.NullPointerException();
        points.add(p);
    }
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null)
            throw new java.lang.NullPointerException();
        return points.contains(p);
    }
    public void draw() {
        // draw all points to standard draw 
        for (Point2D p : points)
            p.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        if (rect == null)
            throw new java.lang.NullPointerException();
        LinkedList<Point2D> pointList = new LinkedList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) 
                pointList.add(p);
        }
        return pointList;
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null)
            throw new java.lang.NullPointerException();
        if (points == null || points.size() == 0) 
            return null;
        Point2D champion = null;
        double minD = Double.MAX_VALUE;
        for (Point2D newP : points) {
            double newD = newP.distanceTo(p);
            if (newD < minD) {
                minD = newD; 
                champion = newP;
            }
        }
        return champion;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        PointSET pSet = new PointSET();
        if (pSet.isEmpty()) 
            StdOut.println("isEmpty(): True");
        else
            StdOut.println("isEmpty(): False");
        int N = 5;
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            Point2D p = new Point2D(x, y);
            pSet.insert(p);
            StdOut.println("size(): "+pSet.size());
        }
        Point2D p = new Point2D(0.5, 0.5);
        pSet.insert(p);
        p = new Point2D(0.501, 0.501);
        pSet.insert(p);
        if (pSet.contains(new Point2D(0.5, 0.5))) 
            StdOut.println("pSet contains (0.5, 0.5)");
        Point2D tmpP = pSet.nearest(new Point2D(0.49, 0.49));
        StdOut.println("nearest to (0.49, 0.49: )"+tmpP);
        RectHV rect = new RectHV(0.3, 0.3, 0.7, 0.7);
        StdOut.println("points in range of rect: "+rect);
        for (Point2D pInRange : pSet.range(rect))
            StdOut.println(pInRange);
        //pSet.insert(null);
    }
}