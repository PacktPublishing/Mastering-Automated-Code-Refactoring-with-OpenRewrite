package de.schulte.recipes;

import org.openrewrite.*;

public class MyFirstImperativeRecipe extends Recipe {

    @Option(displayName = "GreetingMethodName", description = "The name of the method greeting personally")
    private String greetingMethodName;

    public MyFirstImperativeRecipe() {
        this(null);
    }

    public MyFirstImperativeRecipe(String greetingMethodName) {
        this.greetingMethodName = greetingMethodName;
    }

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "My first imperative recipe";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "The first imperative recipe as a showcase for the OpenRewrite course.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MyfirstVisitor(greetingMethodName);
    }

    @Override
    public Validated<Object> validate() {
        final var validate = super.validate();
        if ("bye".equals(greetingMethodName)) {
            return Validated.invalid("greetingMethodName", greetingMethodName, "%s is no greeting, sorry".formatted(greetingMethodName));
        }
        return validate;
    }
}
