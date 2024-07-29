package craftinginterpreters.lox;

import java.util.List;

/**
 * A Class to represent expressions that can be called in Lox (classes and functions)
 */
public interface LoxCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
