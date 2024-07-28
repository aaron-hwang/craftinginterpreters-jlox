//package craftinginterpreters.lox;
//
//public class AstPrinter implements Expr.Visitor<String>{
//    String print(Expr expr) {
//        return expr.accept(this);
//    }
//
//    @Override
//    public String visitBinaryExpr(Expr.Binary expr) {
//        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
//    }
//
//    @Override
//    public String visitGroupingExpr(Expr.Grouping expr) {
//        return parenthesize("group", expr.expression);
//    }
//
//    @Override
//    public String visitLiteralExpr(Expr.Literal expr) {
//        if (expr.value == null) return "nil";
//        return expr.value.toString();
//    }
//
//    @Override
//    public String visitUnaryExpr(Expr.Unary expr) {
//        return parenthesize(expr.operator.lexeme, expr.right);
//    }
//
//    @Override
//    public String visitVariableExpr(Expr.Variable expr) {
//        return "$" + expr.name;
//    }
//
//    /**
//     * Helper function for printing out ASTs, prints out given operator and expressions lisp style
//     * @param operator The operator tobe printed
//     * @param exprs Var arg of expressions
//     * @return A string representing the AST in haskell/fp esque prefix syntax
//     */
//    private String parenthesize(String operator, Expr... exprs) {
//        StringBuilder res = new StringBuilder();
//
//        res.append("(").append(operator);
//        for (Expr expr : exprs) {
//            res.append("  ");
//            res.append(expr.accept(this));
//        }
//
//        res.append(")");
//
//        return res.toString();
//    }
//
//    public static void main(String[] args) {
//        Expr expr = new Expr.Binary(
//                new Expr.Unary(
//                        new Token(TokenType.MINUS, "-", null ,1),
//                        new Expr.Literal(123)
//                ),
//                new Token(TokenType.STAR, "*", null, 1),
//                new Expr.Grouping(new Expr.Literal(45.67))
//        );
//
//        System.out.println(new AstPrinter().print(expr));
//    }
//}
