package Lab1;

import java.io.*;
import java.util.Scanner;

public class palindrome {
    public static void main(String[] args) throws IOException {
        String[] str = new String[10];
        Scanner reader = new Scanner(System.in);
        for (int i=0;i<str.length-1;i++){
            System.out.printf("Введите слово. Чтобы остановить ввод - введите 'stop' ");
            str[i] = read();
            if (str[i].equals("stop")) {
                str[i] = null;
                break;
            }
        }
        for (int i = 0; i < str.length-1; i++) {
            if (str[i] == null) break;
            String s = str[i];
            String reverseS;
            reverseS = reverseString(s);
            if (isPolindrome(s, reverseS)){
                System.out.printf("Слово %s является полиндромом \n", s);
            }
        }
    }
    //Получение обратного слова
    public  static String reverseString(String word){
        String reverseWord = "";
        for (int i=0; i<word.length();i++){
            int x = word.length();
            reverseWord+=word.charAt((word.length()-1)-i);
        }
        return reverseWord;
    }
    //Проверка
    public static boolean isPolindrome(String word, String reverseWord){
        return word.equals(reverseWord);
    }
    //Осуществляет ввод с клавиатуры
    public static String read(){
        Scanner reader = new Scanner(System.in);
        return reader.nextLine();
    }
}
