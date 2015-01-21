package dni.damnit;

import android.graphics.Bitmap;

/**
 * Created by thoma_000 on 1/9/2015.
 */
public class Comet extends Planet{
    float velocityx;
    float velocityy;
    public Comet(float rx, float ry,float vx,float vy, float radius, MainActivity main,Bitmap bitmap) {
        super(rx, ry, radius, main,bitmap);
        velocityx=    rand.nextInt(5) + 1;
        velocityy=    rand.nextInt(5) + 1;
        boolean negative = false;
        if(rand.nextGaussian()<0) negative = true;
        if(negative)velocityy=velocityy*-1;




    }

    public void move(){
        rx += velocityx;
        ry += velocityy;



    }
}




