package de.schulte.recipes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

public class DeleteUnusedPrivateMethods extends Recipe {

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Remove unused private methods";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Removes private methods not being invoked inside a class.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new DeleteUnusedPrivateMethodsVisitor();
    }
}
