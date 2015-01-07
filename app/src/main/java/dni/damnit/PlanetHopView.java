package dni.damnit;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;


public class PlanetHopView extends View {
    Rocket rocket;
    Planet home, destination;
    Animation rocketAnimation;
    SingleAnimation explosionAnimation; // a comment
    MainActivity main;


        public PlanetHopView(Context context, Animation rocketAnimation, SingleAnimation explosionAnimation) {
            super(context);
            main = (MainActivity) context;
            this.rocketAnimation = rocketAnimation;
            this.explosionAnimation = explosionAnimation;
            createPlanets();    //create planets before rocket
            createRocket();
        }

        public boolean onTouchEvent(MotionEvent event) {
            int eventaction = event.getAction();
            float third = main.screenWidth / 3;
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:            // finger touches the screen
                    if (event.getX() < third)// first third
                        rocket.rotateCounterClockwise();
                    if (event.getX() >= third && event.getX() < 2 * third)// first third
                        rocket.fireThrusters();
                    if (event.getX() > 2 * third)
                        rocket.rotateClockwise();

                    break;
                case MotionEvent.ACTION_UP:
                    rocket.stopThrusters();
                    break;
            }

            // tell the system that we handled the event and no further processing is required
            return true;
        }

        private void createPlanets() {
            home = new Planet(main.screenWidth * .2f, main.screenHeight * .23f, main.screenWidth * .06f);
            destination = new Planet(main.screenWidth * .8f, main.screenHeight * .75f, main.screenWidth * .06f);
        }

        private void createRocket() {
            rocket = new Rocket(home, main.screenWidth * 0.04f, rocketAnimation, explosionAnimation);
        }

        private void updateWorldPhysics(Canvas canvas) {
            home.update(canvas, rocket);
            destination.update(canvas, rocket);
            rocket.update(canvas);
        }


        //Throttling not working :( (Dan 1/3/15)
        //float targetFps = 30;
        //long timeLastUpdate = 0, timeNow = 0;
        protected void onDraw(Canvas canvas) {
            //timeNow = System.currentTimeMillis();
            //if(timeNow - timeLastUpdate > 1000/targetFps) {
            //	timeLastUpdate = System.currentTimeMillis();

            updateWorldPhysics(canvas);
            invalidate();
            //}
            try {
                Thread.sleep(30);    //Max fps = 100, throttled down to targetFps
            } catch (InterruptedException e) {
                System.err.println("PlanetHopView.onDraw error");
            }
        }

}
