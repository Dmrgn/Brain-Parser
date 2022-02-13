import java.io.File;

/**
 * Main
 * @author Daniel Morgan
 * @description Implements the BrainParser class to create a program interpreter
 * @see BrainParser
 */
public class Main {
    public static void main(String[] args) {
        BrainParser bp = new BrainParser();
        bp.runProgram(new File(args[0]));
    }
}

/*
       +
      + +
     +   +
    + + + +
   +       +
  + +     + +
 +   +   +   +
+ + + + + + + +
*/