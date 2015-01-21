package dni.damnit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Planet {
	Random rand = new Random();
	int massByG;
	public float rx;
	public float ry;
	public float radius;
    MainActivity main;
    Bitmap bitmap;
	RectF bounds;
	public float SAFE_ENTRY_SPEED = 10;		//TODO: Futz with this
	
	public Planet(float rx, float ry, float radius, MainActivity main,Bitmap bitmap) {
		this.rx = rx;
		this.ry = ry;
		this.radius = radius;
        this.main=main;
        int screenwidth = main.screenWidth;
		calculatemassByG();
        this.bitmap = bitmap;
        bounds = new RectF(rx-radius,ry-radius,rx+radius,ry+radius);


	}
	private void calculatemassByG() {
        int massByGContstant = 10;
        int radiusConstant = 3;
        massByG =(int)( massByGContstant*(radius*radius))/(radiusConstant*radiusConstant);


    }

    public boolean isClicked(float clickedX, float clickedY){
        boolean ret = false;
            if(clickedX<rx+radius && clickedX > rx-radius && clickedY < ry+radius && clickedY > ry-radius)
                ret = true;
        return ret;
    }


	private void enactGravity(Rocket rocket){
		float dx = rx-rocket.rx;
		float dy = ry-rocket.ry;
		float distance = (float) Math.hypot(dx,dy);
		
		float acceleration = massByG/(distance*distance);
		float accelerationX = acceleration*dx/distance;
		float accelerationY = acceleration*dy/distance;
		rocket.speedX+=accelerationX;
		rocket.speedY+=accelerationY;
	}
	private void detectCollision(Rocket rocket) {
		if( /*rocket.rx+rocket.radius>rx-radius &&
			rocket.rx-rocket.radius <rx+radius &&
			rocket.ry+rocket.radius>ry-radius &&
			rocket.ry-rocket.radius <ry+radius &&
			rocket.rocketState*/
			Math.hypot((rocket.rx - rx), (rocket.ry - ry)) < (radius)	&&
			rocket.rocketState == RocketState.Airborne)
		{
			if(Math.hypot(rocket.speedX, rocket.speedY) <= SAFE_ENTRY_SPEED) {
				rocket.rocketState = RocketState.Landed;
			} else {
				rocket.rocketState = RocketState.Crashed;
			}
		}
	}
	
	private void drawSelf(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
        bounds.set(rx-radius,ry-radius,rx+radius,ry+radius);
        canvas.drawBitmap(bitmap,null,bounds,null);
	}
	
	public void update(Canvas canvas, Rocket rocket){
		if (rocket.rocketState != RocketState.Home)
		{
			enactGravity(rocket);
			detectCollision(rocket);
		}
		drawSelf(canvas);
	}
}
