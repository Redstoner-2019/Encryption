package me.redstoner2019.test1;

import java.util.Random;

public class PrimeGenerator {
    public static int generateRandomPrime(int lowerBound, int upperBound) {
        Random random = new Random();
        int randomNum;

        // Loop until a prime number is found within the range
        do {
            randomNum = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        } while (!isPrime(randomNum));

        return randomNum;
    }

    // Helper method to check if a number is prime
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

