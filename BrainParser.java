import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * BrainParser
 * 
 * @author Daniel Morgan
 * @description This class is an interface for an interpreter abiding to the
 *              following rules:
 *              <h3>Operations</h3>
 *              <ul>
 *              <li>Increment : +{value:1}</li>
 *              <li>Decrement : -{value:1}</li>
 *              <li>Set : ={value:0}</li>
 *              </ul>
 *              <h3>Movement</h3>
 *              <ul>
 *              <li>Move Right : >{value:1}</li>
 *              <li>Move Left : <{value:1}</li>
 *              <li>Move value : ^{value:0}</li>
 *              </ul>
 *              <h3>Control</h3>
 *              <ul>
 *              <li>Start Block : [</li>
 *              <li>Close Block : ]</li>
 *              </ul>
 *              <h3>Input/Ouput</h3>
 *              <ul>
 *              <li>Get Char : ,</li>
 *              <li>Print Char : .</li>
 *              </ul>
 */
public class BrainParser extends TuringMachine {
    // what values are prefixed with to distinguish them from commands
    private static final char VALUE_PREFIX = 'T';
    /**
     * The scanner used for reading user input
     * 
     * @see #runCommand(String, String)
     * 
     */
    private Scanner scanner = new Scanner(System.in);
    /**
     * The max value that can be stored in a cell before it wraps (if cell wrapping is enabled) or errors.
     */
    private long cellMax = Long.MAX_VALUE;
    /**
     * The min value that can be stored in a cell before it wraps (if cell wrapping is enabled) or errors.
     */
    private long cellMin = Long.MIN_VALUE;
    /**
     * Array of the indexes of the tokens "sblock" (in bp "[" or "starting block").
     * Array uses the stack architecture. When an ending block token is read during
     * the runCommand() method, the sblock at the top of the stack (the most
     * recently
     * added) is popped off. If the ending block requires the program to loop back
     * to the starting block, the sblock token is pushed back onto the stack and
     * the tokenIndex returns to the corresponding value in blocks array.
     * 
     */
    private ArrayList<Integer> blocks = new ArrayList<Integer>();
    /**
     * The index of the token the parser is currently executing. Used when an ending
     * block requires returning to a starting block token.
     * 
     * @see #runCommand(String, String)
     */
    private int tokenIndex = 0;
    /**
     * String containing the previously run program.
     */
    public String previousProgram = "";
    /**
     * A String containing the current standard of the language being used by the parser.
     */
    private String langaugeStandard;
    /**
     * boolean containing if cells should wrap when they meet their max or min values.
     */
    private boolean doesWrapping;
    /**
     * boolean containing if pointer should wrap to the first cell if it goes past the last cell and vice versa
     */
    private boolean doesPointerWrapping;
    /**
     * ArrayList of currently parsed tokens.
     */
    ArrayList<String> tokens;
    /**
     * Constructor creates a BrainParser with passed tape length
     * 
     * @see TuringMachine#TuringMachine(int)
     * @param size The tape length of the {@link TuringMachine} to be instansiated.
     */
    public BrainParser(int size, long _cellMin, long _cellMax) {
        super(size, false);
        setCellMin(_cellMin);
        setCellMax(_cellMax);
    }
    /**
     * Constructor creates a BrainParser with {@link TuringMachine#DEFUALT_SIZE
     * defualt} tape length and default 
     * 
     * @see TuringMachine#TuringMachine()
     */
    public BrainParser() {
        super();
        setLanguageStandard("tacobell");
    }

    /**
     * Constructor creates a BrainParser with {@link TuringMachine#DEFUALT_SIZE
     * defualt} tape length and default 
     * 
     * @see TuringMachine#TuringMachine()
     */
    public BrainParser(String _languageStandard) {
        super();
        setLanguageStandard(_languageStandard);
    }

    /**
     * Scans a string from the specified point adding each scanned character
     * to a string. Stops scanning when the character == stopper
     * 
     * @param mess    Reference to the string to be scanned through
     * @param start   The index to start scanning from
     * @param stopper The character which when scanned will stop the scanning
     *                process
     * @return Each character from mess[start] to mess[{first index with value
     *         stopper}]
     */
    private String scanTo(String mess, int start, char stopper) {
        int scanIndex = start;
        String scanned = "";
        while (scanIndex < mess.length() && mess.charAt(scanIndex) != stopper) {
            scanned += mess.charAt(scanIndex);
            scanIndex++;
        }
        return scanned;
    }

    /**
     * Starts scanning a string from after a { and returns a string of all
     * characters before the matching },
     * contains checking to ensure the data between brackets is a character literal
     * or a number.
     * 
     * @param mess  Reference to the string to be scanned through
     * @param start The index to start scanning from
     * @see #scanTo(String, int, char)
     */
    private String scanValue(String mess, int start) {
        String value = scanTo(mess, start + 1, '}');
        // if the value is a character literal e.g {'a'} or {'9'}
        if (value.charAt(0) == '\'' && value.charAt(2) == '\'') {
            return value;
        }
        // otherwise the value must be a number
        try {
            Long.parseLong(value);
        } catch (Exception e) {
            crash("scanValue expected a character literal {'a'} or a number {123} but found " + value
                    + " instead. At position " + start);
        }
        return value;
    }

    /**
     * Converts a value (a character wrapped with single quotes prefixed fith T or a
     * number prefixed with T) into a long
     * 
     * @return A long representing the token passed by the value parameter.
     */
    private long convertValue(String value) {
        if (value.charAt(1) == '\'') {
            return (long) value.charAt(2);
        }
        return Long.parseLong(value.substring(1, value.length()));
    }

    /**
     * Parses a raw program into tokens.
     * 
     * @param program A string containing a valid pb program
     * @return An ArrayList<String> of bp tokens
     */
    public ArrayList<String> parse(String program) {
        tokens = new ArrayList<String>();
        // the index of the current character looked at by the parser
        int progIndex = 0;
        try {
            while (progIndex < program.length()) {
                char cur = program.charAt(progIndex);
                switch (cur) {
                    case '+':
                        tokens.add("incr");
                        break;
                    case '-':
                        tokens.add("decr");
                        break;
                    case '=':
                        tokens.add("set");
                        break;
                    case '>':
                        tokens.add("right");
                        break;
                    case '<':
                        tokens.add("left");
                        break;
                    case '^':
                        tokens.add("goto");
                        break;
                    case '[':
                        tokens.add("sblock");
                        break;
                    case ']':
                        tokens.add("eblock");
                        break;
                    case ',':
                        tokens.add("in");
                        break;
                    case '.':
                        tokens.add("out");
                        break;
                    case '{':
                        // get value between {}
                        tokens.add(VALUE_PREFIX + scanValue(program, progIndex));
                        // scan until the close of the brackets (added 1 for the } bracket)
                        progIndex += scanTo(program, progIndex + 1, '}').length() + 1;
                        break;
                }
                progIndex++;
            }
        } catch (Exception e) {
            crash("Uncaught syntax error while parsing at position " + progIndex + ": " + e.getMessage());
        }
        return tokens;
    }

    /**
     * Runs a bp program from the passed file
     * 
     * @see #runProgram(String)
     * @param file A File object containing a raw bp program
     */
    public void runProgram(File file) {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i + 1);
        }
        if (!file.exists())
            crash("File " + file.getName() + " does not exist.");
        if (!(extension.equals("bp")))
            crash("Unsupported extension, expected .bp but found " + extension);
        String line = null;
        String program = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                program += line;
            }
            reader.close();
        } catch (Exception e) {
            crash("Error attempting to open file " + file.getName() + ": " + e.getMessage());
        }
        runProgram(program);
    }

    /**
     * Parses and runs a bp program passed by a string
     * 
     * @see #runProgram(File)
     * @param program A valid bp program represented by a String
     */
    public void runProgram(String program) {
        previousProgram = program;
        tokens = parse(program);
        for (tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++) {
            // System.out.println(tokens.get(i));
            if (tokenIndex < tokens.size() - 1) {
                if (tokens.get(tokenIndex + 1).charAt(0) == VALUE_PREFIX) {
                    runCommand(tokens.get(tokenIndex), tokens.get(tokenIndex + 1));
                    tokenIndex++;
                } else
                    runCommand(tokens.get(tokenIndex));
            } else
                runCommand(tokens.get(tokenIndex));
        }
    }

    /**
     * Checks if the current cell is out of its min and max values and wraps accordingly, also checks if wrapping is enabled.
     */
    private void wrap() {
        if (doesWrapping) {
            long curval = get();
            if (curval < cellMin) {
                set(cellMax+(curval-cellMin));
            } else if (curval > cellMax) {
                set(cellMin+(curval-cellMax));
            }
        }
    }

    /**
     * Checks if the pointer is in an invalid position 
     */
    private void wrapPointer() {
        if (doesPointerWrapping) {
            long curval = getPointer();
            if (curval < 0) {
                setPointer(getTapeLength()-1);
            } else if (curval > getTapeLength()) {
                setPointer(0);
            }
        }
    }

    /**
     * Runs a command token with a passed value. A value is a token prefixed
     * with the {@link #VALUE_PREFIX token value prefix} that denotes a character
     * literal or number to be used in conjuction with the command token that comes
     * before it. In bp code, a token takes the
     * form of "{'h'}" or "{100}", for token values which represent the ascii value
     * of
     * the letter 'h' and the value of 100 respectively.
     * 
     * @param command Command token to be run (e.g "incr", "left", 'eblock', etc.)
     * @param _value  Value to be run with the command token (e.g "T'h'", "T100")
     *                where
     *                'T' is the {@link #VALUE_PREFIX token value prefix}.
     */
    public void runCommand(String command, String _value) {
        long value = convertValue(_value);
        try {
            if (command.equals("incr")) {
                set(get() + value);
                wrap();
            } else if (command.equals("decr")) {
                set(get() - value);
                wrap();
            } else if (command.equals("set")) {
                set(value);
                wrap();
            } else if (command.equals("right")) {
                traverse((int) value);
                wrapPointer();
            } else if (command.equals("left")) {
                traverse(-(int) value);
                wrapPointer();

            } else if (command.equals("goto")) {
                setPointer((int) value);
                wrapPointer();

            } else if (command.equals("sblock")) {
                blocks.add(tokenIndex);
            } else if (command.equals("eblock")) {
                if (blocks.size() == 0)
                    crash("Found ending block without matching starting block.");
                if (get() > 0) {
                    tokenIndex = blocks.get(blocks.size() - 1);
                } else {
                    blocks.remove(blocks.size() - 1);
                }
                wrapPointer();
            } else if (command.equals("in")) {
                if (value == 'c')
                    set(scanner.next().charAt(0));
                else
                    set(scanner.nextLong());
                wrap();
            } else if (command.equals("out")) {
                if (value == 'c')
                    System.out.print((char) (get() % 255));
                else
                    System.out.print(get());
            }
        } catch (Exception e) {
            crash("Uncaught syntax error while running token \"" + command + "\" executing with value " + _value);
        }
        // System.out.println(get());
    }
    /**
     * Runs a parsed token command with a default value.
     * @see #runCommand(String, String)
     * @param command The command token to be run.
     */
    public void runCommand(String command) {
        if (command.equals("incr") || command.equals("decr")) {
            runCommand(command, "T1");
        } else if (command.equals("set") || command.equals("goto")) {
            runCommand(command, "T'0'");
        } else if (command.equals("right") || command.equals("left")) {
            runCommand(command, "T1");
        } else if (command.equals("sblock") || command.equals("eblock") || command.equals("in")
                || command.equals("out")) {
            runCommand(command, "T0");
        }
    }

    /**
     * Sets the the current scanner
     */
    public void setScanner(Scanner _scanner) {
        _scanner = scanner;
    }
    /** 
     * Gets a reference to the current scanner 
     */
    public Scanner getScanner() {
        return scanner;
    }
    /**
     * Sets the minimum cell value
     */
    public void setCellMin(long newMin) {
        cellMin = newMin;
    }
    /**
     * Sets the cell min to a standard value.
     */
    public void setCellMin() {
        cellMin = 0;
    }
    /**
     * Get the current cell minimum
     */
    public long getCellMin() {
        return cellMin;
    }
    /**
     * Sets the maximum cell value
     */
    public void setCellMax(long newMax) {
        cellMax = newMax;
    }
    /**
     * Sets the cell maximum to a standard value.
     */
    public void setCellMax() {
        cellMax = 255;
    }
    /**
     * Get the current cell maximum
     */
    public long getCellMax() {
        return cellMax;
    }
    /**
     * Gets the current language standard
     */
    public String getLanguageStandard() {
        return langaugeStandard;
    }
    /**
     * Attempts to set the current langauge standard.
     * @param standard A string containing the name of the new standard.
     * @return boolean false upon failure, otherwise true.
     */
    public boolean setLanguageStandard(String standard) {
        standard = standard.trim().toLowerCase();
        langaugeStandard = standard;
        if (standard.equals("tacobell")) {
            setCellMax();
            setCellMin();
            setWrapping(true);
            setPointerWrapping(true);
            setTapeLength(30000);
        } else if(standard.equals("bp")) {
            setCellMax(Integer.MAX_VALUE);
            setCellMin(0);
            setWrapping(true);
            setPointerWrapping(true);
            setTapeLength(50000);
        } else if (standard.equals("extbp")) {
            setCellMax(Long.MAX_VALUE);
            setCellMin(Long.MIN_VALUE);
            setWrapping(true);
            setPointerWrapping(true);
            setTapeLength(100000);
        }
        return true;
    }
    /**
     * Sets whether cells should wrap if they reach their min or max
     */
    public void setWrapping(boolean _doesWrap) {
        doesWrapping = _doesWrap;
    }
    /**
     * Gets whether cells should wrap if they reach their min or max
     */
    public boolean getWrapping() {
        return doesWrapping;
    }
    /**
     * Sets whether the pointer should wrap to the first cell if it goes past the last cell and vice versa.
     */
    public void setPointerWrapping(boolean _doesWrap) {
        doesPointerWrapping = _doesWrap;
    }
    /**
     * Gets whether the pointer should wrap to the first cell if it goes past the last cell and vice versa.
     */
    public boolean getPointerWrapping() {
        return doesPointerWrapping;
    }
}