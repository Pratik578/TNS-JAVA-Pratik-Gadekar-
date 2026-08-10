package java1;

import java.util.Scanner;

public class assignment_if {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter token color: ");

        String token = sc.next();

        if (token.equals("white")) {
            System.out.println("Tea");
        }
        else if (token.equals("yellow")) {
            System.out.println("Poha");
        }
        else if (token.equals("red")) {
            System.out.println("Rice Plate");
        }
        else {
            System.out.println("Vadapav");
        }

        sc.close();
    }
}