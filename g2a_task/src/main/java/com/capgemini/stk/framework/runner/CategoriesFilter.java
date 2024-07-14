package com.capgemini.stk.framework.runner;

import com.googlecode.junittoolbox.ExcludeCategories;
import com.googlecode.junittoolbox.IncludeCategories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.util.*;

//Copy of com.googlecode.junittoolbox.CategoriesFilter
@SuppressWarnings("unchecked")
class CategoriesFilter extends Filter {
    private final List<Class<?>> includedCategories = new ArrayList();
    private final List<Class<?>> excludedCategories = new ArrayList();

    static CategoriesFilter forTestSuite(Class<?> testSuiteClass) {
        List<Class<?>> includedCategories = new ArrayList();
        List<Class<?>> excludedCategories = new ArrayList();
        IncludeCategory includeCategoryAnnotation = (IncludeCategory) testSuiteClass
                .getAnnotation(IncludeCategory.class);
        if (includeCategoryAnnotation != null) {
            includedCategories.addAll(Arrays.asList(includeCategoryAnnotation.value()));
        }

        IncludeCategories includeCategoriesAnnotation = (IncludeCategories) testSuiteClass
                .getAnnotation(IncludeCategories.class);
        if (includeCategoriesAnnotation != null) {
            includedCategories.addAll(Arrays.asList(includeCategoriesAnnotation.value()));
        }

        ExcludeCategory excludeCategoryAnnotation = (ExcludeCategory) testSuiteClass
                .getAnnotation(ExcludeCategory.class);
        if (excludeCategoryAnnotation != null) {
            excludedCategories.addAll(Arrays.asList(excludeCategoryAnnotation.value()));
        }

        ExcludeCategories excludeCategoriesAnnotation = (ExcludeCategories) testSuiteClass
                .getAnnotation(ExcludeCategories.class);
        if (excludeCategoriesAnnotation != null) {
            excludedCategories.addAll(Arrays.asList(excludeCategoriesAnnotation.value()));
        }

        return includedCategories.isEmpty() && excludedCategories.isEmpty() ? null : new CategoriesFilter(
                includedCategories, excludedCategories);
    }

    private CategoriesFilter(Collection<Class<?>> includedCategories, Collection<Class<?>> excludedCategories) {
        this.includedCategories.addAll(includedCategories);
        this.excludedCategories.addAll(excludedCategories);
    }

    @Override
    public String describe() {
        StringBuilder sb        = new StringBuilder();
        String        separator = "include categories: ";

        Iterator var3;
        Class    categoryClass;
        for (var3 = this.includedCategories.iterator(); var3.hasNext(); separator = ", ") {
            categoryClass = (Class) var3.next();
            sb.append(separator);
            sb.append(categoryClass.getSimpleName());
        }

        separator = (sb.length() == 0 ? "" : "; ") + "exclude categories: ";

        for (var3 = this.excludedCategories.iterator(); var3.hasNext(); separator = ", ") {
            categoryClass = (Class) var3.next();
            sb.append(separator);
            sb.append(categoryClass.getSimpleName());
        }

        return sb.toString();
    }

    @Override
    public boolean shouldRun(Description description) {
        if (this.hasCorrectCategoryAnnotation(description)) {
            return true;
        }
        else {
            Iterator var2 = description.getChildren().iterator();

            Description childDescription;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                childDescription = (Description) var2.next();
            }
            while (!this.shouldRun(childDescription));

            return true;
        }
    }

    private boolean hasCorrectCategoryAnnotation(Description description) {
        List<Class<?>> categories = this.categories(description);
        if (categories.isEmpty()) {
            return this.includedCategories.isEmpty();
        }
        else {
            Iterator var3;
            Class    category;
            Iterator var5;
            Class    includedCategory;
            if (!this.excludedCategories.isEmpty()) {
                var3 = categories.iterator();

                while (var3.hasNext()) {
                    category = (Class) var3.next();
                    var5     = this.excludedCategories.iterator();

                    while (var5.hasNext()) {
                        includedCategory = (Class) var5.next();
                        if (includedCategory.isAssignableFrom(category)) {
                            return false;
                        }
                    }
                }
            }

            if (this.includedCategories.isEmpty()) {
                return true;
            }
            else {
                var3 = categories.iterator();

                while (var3.hasNext()) {
                    category = (Class) var3.next();
                    var5     = this.includedCategories.iterator();

                    while (var5.hasNext()) {
                        includedCategory = (Class) var5.next();
                        if (includedCategory.isAssignableFrom(category)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    private List<Class<?>> categories(Description description) {
        ArrayList<Class<?>> categories = new ArrayList();
        categories.addAll(Arrays.asList(this.directCategories(description)));
        categories.addAll(Arrays.asList(this.directCategories(this.parentDescription(description))));
        return categories;
    }

    private Description parentDescription(Description description) {
        Class<?> testClass = description.getTestClass();
        return testClass == null ? null : Description.createSuiteDescription(testClass);
    }

    private Class<?>[] directCategories(Description description) {
        if (description == null) {
            return new Class[0];
        }
        else {
            Category annotation = (Category) description.getAnnotation(Category.class);
            return annotation == null ? new Class[0] : annotation.value();
        }
    }
}
