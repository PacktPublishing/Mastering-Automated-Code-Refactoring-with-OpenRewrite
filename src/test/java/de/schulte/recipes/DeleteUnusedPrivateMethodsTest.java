package de.schulte.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.Assertions;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class DeleteUnusedPrivateMethodsTest implements RewriteTest{

        @Override
        public void defaults(RecipeSpec spec) {
            spec.recipe(new DeleteUnusedPrivateMethods());
        }

        @Test
        void test() {
            rewriteRun(Assertions.java(
                    """
                            public class Math {
                            
                              public int inc(int a) {
                                return this.sum(a, 1);
                              }
                            
                              private int sum(int a, int b) {
                                return a + b;
                              }
                            
                              private boolean isNegative(int number) {
                                return number < 0;
                              }
                            
                            }
                            """,
                    """
                            public class Math {
                            
                              public int inc(int a) {
                                return this.sum(a, 1);
                              }
                            
                              private int sum(int a, int b) {
                                return a + b;
                              }
                            
                            }
                            """
            ));
        }

}
