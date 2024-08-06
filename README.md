An interpreter for the Lox language, as outlined by the 
online book [crafting interpreters](https://craftinginterpreters.com/).

This is a tree walk interpreter, using recursive descent and the visitor pattern to walk the AST. 

# The following features have been implemented in this interpreter:

- Tokenizing and lexing
- Creation/usage of abstract syntax trees
- Parsing of said ASTs using recursive descent
- prefix and infix expressions
- objects 
- interpretation of code (using the visitor pattern)
- lexical scope for variables and functions
- functions (with parameters)
- static variable resolution with error detection and handling
- classes, constructors, fields, methods, and inheritance

# Features I plan to add:
- A repl mode (read, evaluate, print loop) so that 
simple expressions such as math can be run and interpreted on the fly
- Static classes
- Interfaces 
- Potential optimizations for faster runtimes
- An actual executable that can be distributed for easy usage
  (currently only runnable in an ide or using javac)

A sample lox program is demonstrated in main/resources/fib.lox or test.lox
(Fib.lox will take about 30-70 seconds to run depending on your machine)