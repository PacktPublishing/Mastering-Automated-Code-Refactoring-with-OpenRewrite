package de.schulte.recipes;

import org.openrewrite.Cursor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IsMethodDeclarationUsedLocallyVisitor extends JavaIsoVisitor<AtomicBoolean> {

    private final J.MethodDeclaration methodDeclaration;

    public IsMethodDeclarationUsedLocallyVisitor(J.MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    @Override
    public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, AtomicBoolean atomicBoolean) {
        final var methodInvocation = super.visitMethodInvocation(method, atomicBoolean);

        final boolean nameMatches = method.getName().getSimpleName()
                                          .equals(methodDeclaration.getName().getSimpleName());
        final boolean returnTypeMatches = method.getType().equals(methodDeclaration.getType());

        final var invocationArguments = method.getArguments().stream().map(Expression::getType).toList();
        final var methodArguments = methodDeclaration.getParameters().stream()
                                                     .filter(J.VariableDeclarations.class::isInstance)
                                                     .map(J.VariableDeclarations.class::cast)
                                                     .flatMap(vd -> vd.getVariables().stream())
                                                     .map(J.VariableDeclarations.NamedVariable::getType).toList();
        final boolean argumentsMatches = invocationArguments.equals(methodArguments);

        if (nameMatches && returnTypeMatches && argumentsMatches) {
            atomicBoolean.set(true);
        }

        return methodInvocation;
    }

}
