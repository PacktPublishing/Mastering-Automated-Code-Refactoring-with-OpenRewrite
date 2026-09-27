package de.schulte.recipes;

import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class MyfirstVisitor extends JavaVisitor<ExecutionContext> {

    public static final String MESSAGE_NAME_UNPERSONAL_GREETING = "unpersonalGreeting";

    private final String greetingMethodName;

    private JavaTemplate addParameterTemplate = JavaTemplate.builder("String name").build();

    private JavaTemplate personalGreetingTemplate = JavaTemplate.builder("\"Hello %s\".formatted(name)").build();

    public MyfirstVisitor(String greetingMethodName) {
        this.greetingMethodName = greetingMethodName == null ? "greet" : greetingMethodName;
    }

    @Override
    public J visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
        final var methodDeclaration = super.visitMethodDeclaration(method, executionContext);
        if (isUnpersonalGreeting(getCursor())) {
            final J.MethodDeclaration newMethodDeclaration = addParameterTemplate.apply(new Cursor(getCursor(),
                            methodDeclaration),
                    method.getCoordinates().replaceParameters());

            final var modifiers = new ArrayList<>(newMethodDeclaration.getModifiers());
            modifiers.add(new J.Modifier(UUID.randomUUID(), Space.SINGLE_SPACE, Markers.EMPTY, "final",
                    J.Modifier.Type.Final, Collections.emptyList()));

            return newMethodDeclaration.withName(method.getName().withSimpleName(greetingMethodName))
                                       .withModifiers(modifiers)
                                       .withMethodType(newMethodDeclaration.getMethodType()
                                                                           .withName(greetingMethodName));
        }
        return methodDeclaration;
    }

    private boolean isUnpersonalGreeting(Cursor methodCursor) {
        return methodCursor.getMessage(MESSAGE_NAME_UNPERSONAL_GREETING, false);
    }

    @Override
    public J visitLiteral(J.Literal literal, ExecutionContext executionContext) {
        if (literal.getValue() instanceof String && literal.getValue().toString().equals("Hello")) {
            final var methodCursor = getCursor().dropParentUntil(node -> node instanceof J.MethodDeclaration);
            if (methodCursor != null) {
                final J.MethodDeclaration method = methodCursor.getValue();
                if (!method.getName().getSimpleName().equals(greetingMethodName)) {
                    methodCursor.putMessage(MESSAGE_NAME_UNPERSONAL_GREETING, true);
                    return personalGreetingTemplate.apply(getCursor(), literal.getCoordinates().replace());
                }
            }
        }
        return super.visitLiteral(literal, executionContext);
    }
}
