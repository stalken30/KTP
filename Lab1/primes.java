package Lab1;

public class primes {
    public static void main(String[] args) {
        for (int n=2; n<101; n++){
            if (!isPrime(n)){
                System.out.printf("Число %d является простым \n", n);
            }
        }


    }
    public static boolean isPrime(int n){
        for (int i = 2; i<n; i++){
            if (n%i==0) return true;
        }
        return false;
    }
}
