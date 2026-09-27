package de.schulte.recipes;

import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import org.openrewrite.java.template.RecipeDescriptor;

import java.util.List;

@RecipeDescriptor(name = "List access improvements", description = "Makes things more readable working with lists")
public class ListAccess {

    @BeforeTemplate
    Object beforeListGetZero(List<?> list) {
        return list.get(0);
    }

    @AfterTemplate
    Object afterListGetFirst(List<?> list) {
        return list.getFirst();
    }

}
