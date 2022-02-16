import java.io.File;

/**
 * Main
 * @author Daniel Morgan
 * @description Implements the BrainParser class to create a program interpreter
 * @see BrainParser
 */
public class Main {
    public static void main(String[] args) {
        // if you enter arguments you can run programs from a file
        // e.g : java Main examples/triangle.bp extbp
        if (args.length > 0) {
            BrainParser bp = new BrainParser();
            if (args.length > 1) {
                bp.setLanguageStandard(args[1]);
            } else {
                bp.setLanguageStandard("tacobell");
            }
            bp.runProgram(new File(args[0]));
        } else {
            // create a BrainParser instance
            // 20 cells on the tape, 0 minimum cell value, 255 maximum cell value
            BrainParser bp = new BrainParser(20, 0, 255);
    
            // run a program from a string
            System.out.println("Here is the result of running a program from a string:");
            bp.runProgram("={'h'}.{'c'}={'i'}.{'c'}={'!'}.{'c'}={10}.{'c'}");
            
            // set the language standard used by the parser
            bp.setLanguageStandard("extbp");
            System.out.println("Setting the language standard to : " + bp.getLanguageStandard());
            System.out.println("This language standard has " + bp.getWrapping() + " for cell wrapping, " + bp.getPointerWrapping() + " for pointer wrapping, " + bp.getCellMin() + " for the cell min and " + bp.getCellMax() + " for the cell max.");
    
            // run a program requiring user input
            System.out.println("The next program will be loaded from a file and requires you to input an integer between 10 and 30:");
            bp.runProgram(new File("examples/triangle.bp"));
    
            System.out.println("Lets run the same program again!");
            System.out.println("Please enter the SAME number again:");
            bp.runProgram(new File("examples/triangle.bp"));
            System.out.println("You may notice the output varies significantly from the first time we ran that program.");
            System.out.println("This is because when we ran the program the second time, data from the first program was not cleared off the tape.");
            System.out.println("While this can cause errors if you forget to clear the tape, you can use this to pass data from one program to the next.");
            System.out.println("For example this next program asks for 2 numbers and stores them in the first and second cell of the tape.");
            System.out.println("Clearing the tape.");
            bp.setTape(new long[bp.getTapeLength()]);
            System.out.println("Enter two integers on separate lines:");
            bp.runProgram("^{0},^{10}={10}.{'c'}^{1},");
            System.out.println("Now that program stored the results of your input in the tape");
            System.out.println("The next program we will run will use those stored values and add them, printing the result:");
            bp.runProgram("^{0}[>+<-]>.^{10}={10}.{'c'}");
            System.out.println("Nice! Lets save this tape for later.");
            long[] savedTape = bp.getTape();
            System.out.println("Saved tape.");
            System.out.println("Now lets create a new tape.");
            bp.setTape(new long[bp.getTapeLength()]);
            System.out.println("New tape created.");
    
            // set the language stadard to something different
            System.out.println("Now lets try changing the language standard.");
            bp.setLanguageStandard("tacobell");
            System.out.println("Setting the language standard to : " + bp.getLanguageStandard());
            System.out.println("This language standard has " + bp.getWrapping() + " for cell wrapping, " + bp.getPointerWrapping() + " for pointer wrapping, " + bp.getCellMin() + " for the cell min and " + bp.getCellMax() + " for the cell max.");
    
            // run the same program as above under a different language standar
            System.out.println("Now lets run the same triangle program as above in this new standard.");
            System.out.println("Please enter an integer from 10-30:");
            bp.runProgram(new File("examples/triangle.bp"));
            System.out.println("We can see that language standards change the output of this program.");
        }
    }
}