package dni.damnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.Vector;


public class PlanetHopView extends View {
    Random rand;
    Rocket rocket;

    Planet home, destination;
    Animation rocketAnimation;
    SingleAnimation explosionAnimation; // a comment
    MainActivity main;
    Paint paint;
    RectF background;
    Bitmap backgroundImage;
    Bitmap earthImage;
    Bitmap cometImage;
    Bitmap venusImage;
    Vector<Comet> comets = new Vector<Comet>();

    public PlanetHopView(Context context, Animation rocketAnimation, SingleAnimation explosionAnimation) {
        super(context);
        loadImages();
        rand = new Random();
        main = (MainActivity) context;
        this.rocketAnimation = rocketAnimation;
        this.explosionAnimation = explosionAnimation;
        //create planets before rocket

        background = new RectF(0, 0, main.screenWidth, main.screenHeight);
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        createPlanets();
        createRocket();
        //createComet();
        createCometLeft();
        //for(int i=0; i<20; i++) createCometLeft();
    }
        private void loadImages(){
            backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.space);
            venusImage = BitmapFactory.decodeResource(getResources(),R.drawable.venus);
            earthImage = BitmapFactory.decodeResource(getResources(),R.drawable.planet1);
            cometImage = BitmapFactory.decodeResource(getResources(),R.drawable.planet2);
        }

        public boolean onTouchEvent(MotionEvent event) {
            int eventaction = event.getAction();
            float third = main.screenWidth / 3;
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:            // finger touches the screen
                    if (destination.isClicked(event.getX(),event.getY()))
                        reset();
                    else {
                        if (event.getX() < third)// first third
                            rocket.rotateCounterClockwise();
                        if (event.getX() >= third && event.getX() < 2 * third)// first third
                            rocket.fireThrusters();
                        if (event.getX() > 2 * third)
                            rocket.rotateClockwise();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    rocket.stopThrusters();
                    break;
            }

            // tell the system that we handled the event and no further processing is required
            return true;
        }
        private void reset (){
            createRocket();

            createCometLeft();
        }

        public void loadBitmap(){


        }


        private void createPlanets() {
            home = new Planet(main.screenWidth * .2f, main.screenHeight * .23f, main.screenWidth * .06f, main, earthImage);
            destination = new Planet(main.screenWidth * .8f, main.screenHeight * .75f, main.screenWidth * .06f,main, cometImage);

        }

        private void createRocket() {
            rocket = new Rocket(home, main.screenWidth * 0.04f, rocketAnimation, explosionAnimation);


        }


    private void createCometLeft() {

        int y = rand.nextInt(500) + 1;
        float  velocityx=    rand.nextInt(5) + 1;
        float  velocityy=    rand.nextInt(5) + 1;
        Comet comet = new Comet(0, y,velocityx,velocityy, 7, main,venusImage);
        comets.add(comet);
    }

    private void drawComets(Canvas canvas){
            for (int i = 0; i< comets.size();i++){
                comets.elementAt(i).update(canvas,rocket);
                comets.elementAt(i).move();}

        }
        private void updateWorldPhysics(Canvas canvas) {


            home.update(canvas, rocket);
            destination.update(canvas, rocket);
            rocket.update(canvas);
            if(rand.nextInt(75)== 0)
                createCometLeft();
            drawComets(canvas);
        }






        //Throttling not working :( (Dan 1/3/15)
        //float targetFps = 30;
        //long timeLastUpdate = 0, timeNow = 0;
        protected void onDraw(Canvas canvas) {
            //timeNow = System.currentTimeMillis();
            //if(timeNow - timeLastUpdate > 1000/targetFps) {
            //	timeLastUpdate = System.currentTimeMillis();

            canvas.drawBitmap(backgroundImage,null,background,null);
            updateWorldPhysics(canvas);
            //}
            invalidate();
            paint=new Paint();
            drawComets(canvas);
            paint.setColor(Color.YELLOW);
            paint.setTextSize(30);
            canvas.drawText("SPD:"+rocket.calculateSpeed(),10,30,paint);
            try {
                Thread.sleep(30);    //Max fps = 100, throttled down to targetFps
            } catch (InterruptedException e) {
                System.err.println("PlanetHopView.onDraw error");
            }



        }

}
