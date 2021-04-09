package Lab2;

public class Lab1 {
    public static void main(String[] args){
        point3d myPoint = new point3d(1, 1, 1);
        point3d myPoint2 = new point3d(3, 1, 6);
        point3d myPoint3 = new point3d(7, 4, 2);
        if (myPoint.compare(myPoint2)
            || myPoint.compare(myPoint3)
            || myPoint2.compare(myPoint3))
            System.out.println("Это не треугольник");
        else System.out.printf("Площадь треугольника: s = %.2f", computeArea(myPoint, myPoint2, myPoint3));
    }
    public static double computeArea (point3d point1, point3d point2, point3d point3){
        double a = point1.distanceTo(point2);
        double b = point1.distanceTo(point3);
        double c = point2.distanceTo(point3);
        double p = 0.5f*(a+b+c);
        System.out.printf("Длины сторон треугольника: a = %.2f, b = %.2f, c = %.2f \nПериметр треугольника: p = %.2f \n", a,b,c, p);
        return (Math.sqrt(p*(p-a)*(p-b)*(p-c)));
    }
}
