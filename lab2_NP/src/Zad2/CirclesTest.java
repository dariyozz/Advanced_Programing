package Zad2;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();
}

class MovablePoint implements Movable {
    private int x, y;
    private int xSpeed, ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0) {
            throw new ObjectCanNotBeMovedException("Point (" + x + "," + (y - ySpeed) + ") is out of bounds");
        }
        y -= ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.getyMax()) {
            throw new ObjectCanNotBeMovedException("Point (" + x + "," + (y + ySpeed) + ") is out of bounds");
        }
        y += ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.getxMax()) {
            throw new ObjectCanNotBeMovedException("Point (" + (x + xSpeed) + "," + y + ") is out of bounds");
        }
        x += xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0) {
            throw new ObjectCanNotBeMovedException("Point (" + (x - xSpeed) + "," + y + ") is out of bounds");
        }
        x -= xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }
}

class MovableCircle implements Movable {
    int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ") and radius " + radius;
    }
}

class MovablesCollection {
    private List<Movable> movables;
    private static int xMax;
    private static int yMax;

    public MovablesCollection(int xMax, int yMax) {
        MovablesCollection.xMax = xMax;
        MovablesCollection.yMax = yMax;
        movables = new ArrayList<>();
    }

    public static int getxMax() {
        return xMax;
    }

    public static int getyMax() {
        return yMax;
    }

    public static void setxMax(int xMax) {
        MovablesCollection.xMax = xMax;
    }

    public static void setyMax(int yMax) {
        MovablesCollection.yMax = yMax;
    }

    public void addMovableObject(Movable m) {
        try {
            if (m instanceof MovableCircle) {
                MovableCircle circle = (MovableCircle) m;
                if (circle.getCurrentXPosition() - circle.radius < 0 ||
                        circle.getCurrentXPosition() + circle.radius > xMax ||
                        circle.getCurrentYPosition() - circle.radius < 0 ||
                        circle.getCurrentYPosition() + circle.radius > yMax) {
                    throw new MovableObjectNotFittableException("Movable circle with center (" + circle.getCurrentXPosition() + "," + circle.getCurrentYPosition() + ") and radius " + circle.radius + " can not be fitted into the collection");
                }
            } else if (m instanceof MovablePoint) {
                MovablePoint point = (MovablePoint) m;
                if (point.getCurrentXPosition() < 0 || point.getCurrentXPosition() > xMax ||
                        point.getCurrentYPosition() < 0 || point.getCurrentYPosition() > yMax) {
                    throw new MovableObjectNotFittableException("Point (" + point.getCurrentXPosition() + "," + point.getCurrentYPosition() + ")" +
                            " is out of bounds");
                }
            }
            movables.add(m);
        } catch (MovableObjectNotFittableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movables) {
            try {
                if ((type == TYPE.POINT && m instanceof MovablePoint) || (type == TYPE.CIRCLE && m instanceof MovableCircle)) {
                    switch (direction) {
                        case UP:
                            m.moveUp();
                            break;
                        case DOWN:
                            m.moveDown();
                            break;
                        case LEFT:
                            m.moveLeft();
                            break;
                        case RIGHT:
                            m.moveRight();
                            break;
                    }
                }
            } catch (ObjectCanNotBeMovedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size ").append(movables.size()).append(":\n");
        for (Movable m : movables) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString().trim();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }

        }
        System.out.println(collection.toString()+"\n");


        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString()+"\n");

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString()+"\n");

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString()+"\n");


        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
