package craftinginterpreters.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * representation of a Class in lox. TODO: Add capability for static classes/methods in lox.
 */
public class LoxClass implements LoxCallable{
    final String name;
    private final Map<String, LoxFunction> methods;
    final LoxClass superclass;

    public LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods) {
        this.superclass = superclass;
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    public LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (this.superclass != null) {
            return this.superclass.findMethod(name);
        }

        return null;
    }
}
