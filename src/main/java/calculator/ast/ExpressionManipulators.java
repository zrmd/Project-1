package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;

/**
 * All of the static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the TODOs in the 'toDoubleHelper' method.
        return new AstNode(toDoubleHelper(env.getVariables(), node.getChildren().get(0)));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
            
        } 
        
        else if (node.isVariable()) {
            if(variables.containsKey(node.getName())) {
                AstNode temp = variables.get(node.getName());
                return toDoubleHelper(variables, temp);
            }
            
            else {
                throw new EvaluationError("");
            }
            
        }
        
        else {
            String name = node.getName();
            
            
            if(name.equals("+")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) + toDoubleHelper(variables, node.getChildren().get(1));
            }
            
            else if(name.equals("-")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) - toDoubleHelper(variables, node.getChildren().get(1));
            }
            
            else if(name.equals("*")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) * toDoubleHelper(variables, node.getChildren().get(1));
            }
            
            else if(name.equals("/")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) / toDoubleHelper(variables, node.getChildren().get(1));
            }
            
            else if(name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, node.getChildren().get(0)), toDoubleHelper(variables, node.getChildren().get(1)));
            }
            
            else if(name.equals("negate")) {
                return -1 * toDoubleHelper(variables, node.getChildren().get(0));
            }
            
            else if(name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, node.getChildren().get(0)));
            }
            
            else if(name.equals("cos")){
                return Math.cos(toDoubleHelper(variables, node.getChildren().get(0)));
            }
            
            else {
                throw new EvaluationError("");
            }
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way

        return handleSimplifyHelper(env.getVariables(), node.getChildren().get(0));
    }
    
    private static AstNode handleSimplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) {
            return node;
        }
        else if (node.isVariable()) {
            if(variables.containsKey(node.getName())) {
                return handleSimplifyHelper(variables, variables.get(node.getName())); 
            }else {
                return node;
            } 
        }
            else {
                String name = node.getName();
                 if(name.equals("+") || name.equals("-") || name.equals("*")) {
                    AstNode temp = nodeClone(node);
                    temp.getChildren().set(0, handleSimplifyHelper(variables, temp.getChildren().get(0)));
                    temp.getChildren().set(1, handleSimplifyHelper(variables, temp.getChildren().get(1)));
                    if(temp.getChildren().get(0).isNumber() && temp.getChildren().get(1).isNumber()) {
                        return new AstNode(toDoubleHelper(variables, temp));
                    }else {
                        return temp;
                    }
                } else {
                    AstNode temp = nodeClone(node);
                    for (int i = 0; i < node.getChildren().size(); i++) {
                        temp.getChildren().set(i, handleSimplifyHelper(variables, temp.getChildren().get(i)));
                    }
                    return temp;
                }
            }     
    }
    
    
    public static AstNode nodeClone(AstNode node) {
        AstNode temp = new AstNode(node.getName(), nodeCloneHelper(node.getChildren()));
        return temp;
    }
    public static IList<AstNode> nodeCloneHelper(IList<AstNode> nodes) {
        IList<AstNode> temp = new DoubleLinkedList<>();
        for (int i = 0; i < nodes.size(); i++) {
        if (nodes.get(i).isNumber()) {
            
        temp.add(new AstNode(nodes.get(i).getNumericValue()));
        
        }else if(nodes.get(i).isVariable()){
            
        temp.add(new AstNode(nodes.get(i).getName()));
        
        }
        else {
        temp.add(new AstNode(nodes.get(i).getName(), nodeCloneHelper(nodes.get(i).getChildren())));   
        }
        } 
        return temp;
    }
    

    /**
     * Accepts a 'plot(exprToPlot, var, varMin, varMax, step)' AstNode and
     * generates the corresponding plot. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {


        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        double delta = toDoubleHelper(env.getVariables(), node.getChildren().remove()); 
        double end = toDoubleHelper(env.getVariables(), node.getChildren().remove());
        double start = toDoubleHelper(env.getVariables(), node.getChildren().remove()); 
        String variable = node.getChildren().remove().getName();
        AstNode funct = node.getChildren().remove();
        if(start >= end) {
            throw new EvaluationError("");
        }else if((end - start) < delta || delta <= 0) {
            throw new EvaluationError("");
        }
        IList<Double> xVals = new DoubleLinkedList<Double>();
        IList<Double> yVals = new DoubleLinkedList<Double>();
        for(int i = 0; i < ((end - start)/delta) + 1; i++) {
           double xVal = start + (i * delta);
           env.getVariables().put(variable, new AstNode(xVal)); 
           xVals.add(xVal);
           double yVal = toDoubleHelper(env.getVariables(), nodeClone(funct));
           yVals.add(yVal);
        }
        env.getVariables().remove(variable);
        env.getImageDrawer().drawScatterPlot("Plot", variable, "output", xVals, yVals);
        
         return new AstNode(1);
    }
}
