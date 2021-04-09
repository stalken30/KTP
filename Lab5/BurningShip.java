package Lab5;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zRe = 0;
        double zIm = 0;

        while (iteration < MAX_ITERATIONS &&
                zRe * zRe + zIm * zIm < 4)
        {
            double zReUpdated = zRe * zRe - zIm * zIm + x;
            double zImUpdated = 2 * Math.abs(zRe) * Math.abs(zIm) + y;
            zRe = zReUpdated;
            zIm = zImUpdated;
            iteration += 1;
        }

        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

    public String toString() {
        return "BurningShip";
    }
}
