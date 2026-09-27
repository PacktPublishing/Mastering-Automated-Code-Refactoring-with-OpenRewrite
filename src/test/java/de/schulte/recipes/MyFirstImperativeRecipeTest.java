package de.schulte.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.Assertions;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.test.TypeValidation;

import static org.junit.jupiter.api.Assertions.*;

class MyFirstImperativeRecipeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new MyFirstImperativeRecipe("bye"))
            .typeValidationOptions(TypeValidation.builder().identifiers(false).build());
    }

    @Test
    void myFirstTest() {
        rewriteRun(Assertions.java(
                """
                        public class Foo {
                        
                          public String foo() {
                            return "Hello";
                          }
                        
                        }
                        """,
                """
                        public class Foo {
                        
                          public final String greet(String name) {
                            return "Hello %s".formatted(name);
                          }
                        
                        }
                        """
        ));
    }
}
