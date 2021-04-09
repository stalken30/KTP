package Lab2;

public class point3d {
    private double xCoord;
    private double yCoord;
    private double zCoord;
    public point3d (double x, double y, double z){
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }
    public point3d (){
        this (0,0,0);
    }
    public void setX (double val){
        xCoord = val;
    }
    public void setY (double val){
        yCoord = val;
    }
    public void setZ (double val){
        zCoord = val;
    }
    public double getX () {
        return xCoord;
    }
    public double getY () {
        return yCoord;
    }
    public double getZ () {
        return zCoord;
    }
    public boolean compare (point3d otherPoint){
//        return((first.xCoord == second.xCoord)
//            && (first.yCoord == second.yCoord)
//            && (first.zCoord == second.zCoord));
        return((this.xCoord == otherPoint.xCoord)
            && (this.yCoord == otherPoint.yCoord)
            && (this.zCoord == otherPoint.zCoord));
    }
    public double distanceTo (point3d otherPoint){
        double xDistance = 0;
        double yDistance = 0;
        double zDistance = 0;
        xDistance = this.xCoord - otherPoint.xCoord;
        yDistance = this.yCoord - otherPoint.yCoord;
        zDistance = this.zCoord - otherPoint.zCoord;
        return (Math.sqrt(xDistance*xDistance + yDistance*yDistance + zDistance*zDistance));
    }
}
