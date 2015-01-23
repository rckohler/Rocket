package dni.damnit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Vector;

/**
 * Created by rck on 1/21/2015.
 */
public class ObjectOfMass {

    //private variables
        // constants
        private float negligibleMass = 1;
        private float gravitationalConstant;
        //drawing variables
        private Bitmap bitmap;
        private RectF bounds;
        private Paint paint = new Paint();

    //public variables
        //physics variables
        public float mass = negligibleMass;
        public float xPos, yPos;
        public float maxVelocity, xVelocity, yVelocity;
        public float maxAcceleration, xAcceleration, yAcceleration;
        public float radius;
        public float rotation;
        //flags
        public boolean collisionFlag;
        //vectors
        public Vector<ObjectOfMass>collidedWith = new Vector<ObjectOfMass>();

    public ObjectOfMass(float xPos, float yPos, float radius, float gravitationalConstant){
        this.xPos = xPos; // this objectOfMass's xPos = fed xPOs
        this.yPos = yPos;
        this.radius = radius;
        this.gravitationalConstant = gravitationalConstant;
    }
    public ObjectOfMass(float xPos, float yPos, float radius, float gravitationalConstant, Bitmap bitmap){
        this.xPos = xPos; // this objectOfMass's xPos = fed xPOs
        this.yPos = yPos;
        this.radius = radius;
        this.gravitationalConstant = gravitationalConstant;
        this.bitmap = bitmap;
    }
    private void move() {
        xVelocity += xAcceleration;
        yVelocity += yAcceleration;
        xPos += xVelocity;
        yPos += yVelocity;
    }

    public void setDestination(float xDestination, float yDestination){
        float dx, dy, distance;

        dx = xDestination-xPos;
        dy = yDestination-yPos;
        distance = (float)Math.pow(dx*dx+dy*dy,.5);

        xVelocity = maxVelocity*dx/distance;
        yVelocity = maxVelocity*dy/distance;
        xAcceleration = maxAcceleration*dx/distance;
        yAcceleration = maxAcceleration*dy/distance;
    }

    private void detectCollision(Vector<ObjectOfMass> others){
        collisionFlag = false;
        boolean ret = false;
        float dx, dy, distance;
        for (int i = 0; i < others.size(); i++) {
            ObjectOfMass other = others.elementAt(i);
            dx = other.xPos - xPos;
            dy = other.yPos - yPos;
            distance = (float) Math.pow(dx * dx + dy * dy, .5);
            if (distance < radius + other.radius) {
                collisionFlag = true;
                if (!collidedWith.contains(other))
                    collidedWith.add(other);
                }
            }
    }
    public boolean isClicked(float clickedX, float clickedY){
        boolean ret = false;
        if(clickedX<xPos+radius && clickedX > xPos-radius && clickedY < yPos+radius && clickedY > yPos-radius)
            ret = true;
        return ret;
    }
    public void setGravitationalConstant(float gravitationalConstant){ //if for some reason this changes... not likely to be used often
        this.gravitationalConstant = gravitationalConstant;
    }
    private void enactGravity(Vector<ObjectOfMass> others){
        float dx,dy,distance;
        float force, xForce, yForce;
        float acceleration, xAcceleration, yAcceleration;

        for (int i = 0; i < others.size(); i++) { // modifies others' acceleration not self
            ObjectOfMass currentObject = others.elementAt(i);
            if (currentObject != this){ // if the current object is not this object
                dx = xPos-currentObject.xPos;
                dy = yPos-currentObject.yPos;
                distance = (float) Math.hypot(dx,dy);

                force = gravitationalConstant*mass*currentObject.mass/(distance*distance);

                xForce = force*dx/distance;
                xAcceleration = xForce/mass;
                currentObject.xAcceleration += xAcceleration;

                yForce = force*dy/distance;
                yAcceleration = yForce/mass;
                currentObject.yAcceleration += yAcceleration;
            }
        }
    }

    private void drawSelf(Canvas canvas){
        paint.setColor(Color.BLUE);
        bounds.set(xPos-radius,yPos-radius,xPos+radius,yPos+radius);
        if (bitmap != null)
            canvas.drawBitmap(bitmap,null,bounds,null);
    }

    public void update(Canvas canvas, Vector<ObjectOfMass> others){
        enactGravity(others);
        move();
        drawSelf(canvas);
    }

}
