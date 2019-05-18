
package slots;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Slots {

    public static void main(String[] args) throws FileNotFoundException 
    {
       Scanner sc = new Scanner(System.in);
       
       // Values of 2D array containing the sizes of all of the reels
       final int arrX = 78;
       final int arrY = 5;
       
       // User's credits
       long money = 10000;
       long bet = 0;
       
       
       boolean hasWon = false; // Active when there are 3 or more occurences in a line
       boolean isDone = false; // Game loop, terminates when wrong input is used or when user wishes to leave
       boolean correctInput = true; // Used to regulate user input
       
       String[][] Reels = new String[3][5]; // Reels visible to the user
       String[][] allReels = new String[arrX][arrY]; // All reels taken from file
       
       
       // Arrays used to check if there are three ocurences in a line in a Counting Sort - like manner
       String[] checkArr = {"J ", "Q ", "K ", "A ", "S1 ", "S2 ", "S3 ", "S4 ", "S5 "};
       String[] winningLine = new String[5];  
       int[] resultArr = {0,0,0,0,0,0,0,0,0};     
       
    
       // Reads file and inputs it in 2D array
       readFile(allReels, arrX, arrY); 
       
       //Game loop
       while (!isDone) { 
        
        System.out.println("Would you like to play? Please enter how much you want to bet. Enter 0 to withdraw.");
        System.out.println("Current credits: " + money);
        System.out.println();
        
        try {
        bet = sc.nextInt();
        }
        
        // Break game loop if input is incorrect
        catch (Exception e) {
            correctInput = false;
            isDone = true;
            System.out.println("Securitiy is kicking you out for that.");
            break;                 
        }
        
        System.out.println();
        
        //Terminates game loop
        if (bet == 0) 
            isDone = true;
        
        //Checks if user atempts to bet more money than he has
        if (bet > money) { 
            correctInput = false;
            System.out.println("You can't bet what you don't have!");
        }
        
        else {
            correctInput = true;
        }
        
        // If the game has not been terminated and user input is correct run one iteration of the game
        if (!isDone && correctInput) {  
             
        // take the wager from the user's total money
        money = money - bet; 
        
        // Takes 5 random values from the reels and removes NA values if they are found
        spin(allReels,Reels);        
        removeNAs(Reels);

        // Checks the three random generated rows and checks if there are three ocurences in them
        hasWon = checkHorizontalWin(Reels, winningLine , checkArr, resultArr, hasWon); 
             
        // Checks to see if there are three ocurences in the diagonal line, called only if there are no occurences in the three rows
        if (hasWon == false) {
        hasWon = checkDiagonalWin(Reels, winningLine , checkArr, resultArr, hasWon);
        }
        
        // Give user his winnings if he has won
        if (hasWon) { 
            money = money + (bet * 2);
        }
        
        //Print the reels to the user
        printReels(Reels); 
        System.out.println();
        hasWon = false;
      }       
   }            
}
    
    
    
 public static boolean checkHorizontalWin(String[][] Reels, String [] winningLine, String checkArr[], int resultArr[], boolean hasWon) {
     // Loops three times for each row
     for (int k = 0; k < 3; k++) {
         
         //Stores the 5 symbols of each row in the winningLine array
         for (int i = 0; i < 5; i++) {
             winningLine[i] = Reels[k][i];
         }
         
         //Iincrements the reasult array for each corresponding item found in the checkArray
         incrementResulArr(checkArr, winningLine, resultArr);
         
         //Checks to see if the result array has a value which is greater than 2, returns true if it does
         hasWon = checkWin(resultArr,hasWon);
         
         // sets all the values of the result array to 0
         reIntializeResultArr(resultArr);
         
         //if one of the rows returns true break out of the loop,
         if (hasWon) 
             break;
     }     
     return hasWon;
 }
 
 public static boolean checkDiagonalWin(String[][] Reels, String [] winningLine, String checkArr[], int resultArr[], boolean hasWon) {
     
         // Manually assign the values to the diagonal line
         winningLine[0] = Reels[0][0];
         winningLine[1] = Reels[1][1];
         winningLine[2] = Reels[2][2];
         winningLine[3] = Reels[1][3];
         winningLine[4] = Reels[0][4];
             
         //Iincrements the reasult array for each corresponding item found in the checkArray
         incrementResulArr(checkArr, winningLine, resultArr);
         
         //Checks to see if the result array has a value which is greater than 2, returns true if it does
         hasWon = checkWin(resultArr,hasWon);
         
         // sets all the values of the result array to 0
         reIntializeResultArr(resultArr);
         
         return hasWon;
 }
 
 public static void printReels(String[][] Reels) {
     
     // Print the reels visible to the user
     for (int i = 0; i < 3; i++) {
           for ( int j = 0; j < 5; j++) {
               System.out.print(Reels[i][j]);
           }
           System.out.println();
       }
 }
 
 public static void removeNAs(String Reels[][]) {
     
     // Replace NAs with S2 as S2 is the next symbol to appear after the reel loops once
     for (int i = 0; i < 3; i++) {
           for ( int j = 0; j < 5; j++) {
               if (Reels[i][j].equals("NA "))
                   Reels[i][j] = "S2 ";
           }
         }
 }
 
 public static void spin(String allReels[][], String Reels[][]) {
     
     // Declare Random instance 
     int randomPosition;
     Random randomPositionGenerator = new Random();
     
     // Initialize random instance for each element in a row
     for (int i = 0; i < 5; i++) {         
           randomPosition = randomPositionGenerator.nextInt(78);

           // Usual case, fills the Reels array with the random values and the incremented versions of those random numbers
           if (randomPosition <= 75) { 
               Reels[0][i] = allReels[randomPosition][i];
               Reels[1][i] = allReels[randomPosition+1][i];
               Reels[2][i] = allReels[randomPosition+2][i];
           }
           // Two special cases which occur when the random numer is equal to a number close to the end which can cause an array out of bonds exception, manually asing it to the first values of the reel.
           else if (randomPosition == 76){ 
               Reels[0][i] = allReels[randomPosition][i];
               Reels[1][i] = allReels[randomPosition+1][i];
               Reels[2][i] = allReels[0][i];
           }
           else if (randomPosition == 77){ 
               Reels[0][i] = allReels[randomPosition][i];
               Reels[1][i] = allReels[0][i];
               Reels[2][i] = allReels[1][i];
           }       
       }
 }
 
 public static void readFile(String [][] allReels,int arrX,int arrY) throws FileNotFoundException  {
     //Reads the file and stores only the symbols in the allReels array.
     Scanner sc = new Scanner(new BufferedReader(new FileReader("reels_template.txt")));
     String[] line = {};
     
     //First line is skipped as it contains no simbols
     line = sc.nextLine().trim().split("\\s+");
      
         for (int i=0; i<arrX; i++) {
             if (sc.hasNextLine()) {
                 
             //Takes one line from the file and splits it into strings    
             line = sc.nextLine().trim().split("\\s+");
             }
             
            //This foor loop is programmed in a way to skip the first string of the line array
            for (int j=0; j<=arrY; j++) {  
                if (j!=0) {
                allReels[i][j-1] = line[j] + " "; 
               }
            }
         }
 }
 
 public static void reIntializeResultArr(int resultArr []) {  
     
     // Loop through the result array and intialize it to 0
    for (int j = 0; j < resultArr.length; j++) {
                 resultArr[j] = 0;              
        }
 
}
 
 public static boolean checkWin(int resultArr [], boolean hasWon) {
     
     // Loop through the result array and return true if there is a integer greater than 2
     for (int i = 0; i < resultArr.length; i++) {
             if (resultArr[i] > 2) {
                 System.out.println("WIN");
                 hasWon = true;
                 break;
             }
         }
     return hasWon;
 }
 
 public static void incrementResulArr(String [] checkArr, String [] winningLine, int resultArr []) {
     
     // Loops through winningLine array and the checkArr, when it finds the symbol from the winning line array in the check array it increments the corresponding position in the result array
     for (int i = 0; i < 5; i++) {
             for (int j = 0; j < checkArr.length; j++) {
                 if (winningLine[i].equals(checkArr[j])) {
                     resultArr[j]++;
                     break;
                 }
            }
         }
 }
 
}