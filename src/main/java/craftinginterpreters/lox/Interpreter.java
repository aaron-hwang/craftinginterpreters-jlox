package craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private Environment env = new Environment();

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    private void execute(Stmt statement) {
        statement.accept(this);
    }

    public void executeBlock(List<Stmt> statements, Environment env) {
        Environment previous = this.env;
        try {
            this.env = env;

            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.env = previous;
        }
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(this.env));
        return null;
    }

    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(stmt.condition)) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null){
            execute(stmt.elseBranch);
        }

        return null;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(expr.left)) {
                return left;
            }
            else {
                if (!isTruthy(expr.left)) return left;
            }
        }

        return expr.right;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }


    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        env.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }

        return null;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
        }

        // Should be unreachable.
        return null;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return env.get(expr.name);
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        env.assign(expr.name, value);
        return value;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {return;}
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new RuntimeError(operator, "Operand must be a number");
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }

                throw new RuntimeError(expr.operator, "Operand must be a number");

            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case BANG_EQUAL:
                return !isEqual(left, right);
        }

        // Should be unreachable, smth went wrong if we get here
        return null;
    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        Object callee = evaluate(expr.callee);

        List<Object> args = new ArrayList<>();
        for (Object arg : expr.arguments) {
            args.add(arg);
        }

        if (!(callee instanceof LoxCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes");
        }

        LoxCallable function = (LoxCallable)callee;
        if (args.size() != function.arity()) {
            throw new RuntimeError(expr.paren,
                    "Expected " + function.arity() + " arguments but got " + args.size()
                    );
        }
        return function.call(this, args);
    }

    // Lox's interpretation of equality
    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;

        return left.equals(right);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

}


