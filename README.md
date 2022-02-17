# BrainParser

An ever so slightly less painful version of Brain****. Interpreter written in Java. 

# Setup

Create a file with the extension .bp. By default all non-command characters are ignored as comments.
To execute the program, compile and run the Main class: ```javac Main.java && java Main <your-file-name>.bp```.

# Coming from Brain****

The basic commands are the same, however two new commands have been added:
```
^{value} moves the pointer to the 'value'th cell
={value} sets the current cell to 'value'
e.g
^{10} moves the pointer to the 10th cell
={100} sets the current cell to 100
={'h'} sets the current cell to the ascii value of 'h'
```
The ```command{value}``` syntax allows you to write long chains of commands in a more concise manner:
instead of:
````
+++++
````
you can use:
````
+{5}
````
same with decrement:
````
-{5}
````
you can also use this syntax for pointer movement:
````
>>>>>
<<<<<
````
becomes:
````
>{5}
<{5}
````
Anywhere you can use command{number} you can also use command{'character'}. For example instead of:
````
++++[>++++++++++++<-].
````
to print a 0 character (ascii 48) you can use:
````
={'0'}.
````
note that by default the interpreter prints the value of the cell in decimal, so the above would print 48 not 0.
to print the ascii character, you can add a value argument to the . command:
````
={'0'}.{'c'}
````
'c' is for character.
input also is read in decimal format, so ````,.```` with an input of 0 would print 0 not 48.
to read the ascii value of a character, you can add a value argument to the . command:
````
,{'c'}.
````
the above would print 48 upon entering 0.

# Reference

<h3>Operations</h3>
<ul>
 <li>Increment : +{value:1}</li>
 <li>Decrement : -{value:1}</li>
 <li>Set : ={value:0}</li>
</ul>
<h3>Movement</h3>
<ul>
 <li>Move Right : >{value:1}</li>
 <li>Move Left : <{value:1}</li>
 <li>Move value : ^{value:0}</li>
</ul>
<h3>Control</h3>
<ul>
 <li>Start Block : [</li>
 <li>Close Block : ]</li>
</ul>
<h3>Input/Ouput</h3>
<ul>
 <li>Get Char : ,</li>
 <li>Print Char : .</li>
</ul>
