package dni.damnit;

/**
 * Created by rck on 1/21/2015.
 */
public class ObjectOfMass {
    //private variables
    private float negligibleMass = 1;
    //public variables
    public float mass = negligibleMass;
    public float xPos, yPos;

    public float maxVelocity, xVelocity, yVelocity;
    public float maxAcceleration, xAcceleration, yAcceleration;
    public float radius;

    public ObjectOfMass(float xPos, float yPos, float radius){
        this.xPos = xPos; // this objectOfMass's xPos = fed xPOs
        this.yPos = yPos;
        this.radius = radius;
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

    public boolean detectCollision(ObjectOfMass other){
        boolean ret = false;
        float dx, dy, distance;
        dx = other.xPos - xPos;
        dy = other.yPos - yPos;
        distance = (float)Math.pow(dx*dx+dy*dy,.5);
        if (distance < radius + other.radius)
            ret = true;
        return ret;
    }
    public boolean isClicked(float clickedX, float clickedY){
        boolean ret = false;
        if(clickedX<xPos+radius && clickedX > xPos-radius && clickedY < yPos+radius && clickedY > yPos-radius)
            ret = true;

        return ret;
    }

}
