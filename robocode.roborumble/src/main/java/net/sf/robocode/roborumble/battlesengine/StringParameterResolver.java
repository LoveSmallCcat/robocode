package net.sf.robocode.roborumble.battlesengine;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public interface StringParameterResolver extends ParameterResolver {
    boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext);

    Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext);
}
