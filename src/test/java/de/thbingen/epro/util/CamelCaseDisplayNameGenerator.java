package de.thbingen.epro.util;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

// Source:
// https://www.baeldung.com/junit-custom-display-name-generator
public class CamelCaseDisplayNameGenerator extends DisplayNameGenerator.Standard {
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCamelCase(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceCamelCase(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        // Adapted from source to not include parentheses at the end of the name
        return this.replaceCamelCase(testMethod.getName());
    }

    // Adapted to not seperate consecutive Upper-Case-Chars with Whitespace
    // Adapted to make first char uppercase
    String replaceCamelCase(String camelCase) {
        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(camelCase.charAt(0)));
        for (int i=1; i<camelCase.length(); i++) {
            if (Character.isUpperCase(camelCase.charAt(i)) && Character.isLowerCase(camelCase.charAt(i-1))) {
                result.append(' ');
                result.append(camelCase.charAt(i));
            } else {
                result.append(camelCase.charAt(i));
            }
        }
        return result.toString();
    }
}
