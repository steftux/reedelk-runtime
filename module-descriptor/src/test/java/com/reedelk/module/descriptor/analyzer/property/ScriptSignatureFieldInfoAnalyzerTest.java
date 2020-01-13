package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.module.descriptor.model.TypeScriptDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ScriptSignatureFieldInfoAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private ScriptSignatureFieldInfoAnalyzer analyzer = new ScriptSignatureFieldInfoAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponentWithScriptSignature.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateScriptSignatureDefinition() {
        // Given
        String propertyName = "scriptPropertyWithScriptSignature";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        ComponentPropertyDescriptor.Builder builder =
                ComponentPropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(new TypeScriptDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        ComponentPropertyDescriptor descriptor = builder.build();
        ScriptSignatureDefinitionMatchers.ScriptSignatureDefinitionMatcher matcher = ScriptSignatureDefinitionMatchers.with(Arrays.asList("arg1", "arg2", "arg3"));
        assertThat(matcher.matches(descriptor.getScriptSignatureDescriptor())).isTrue();
    }

    @Test
    void shouldReturnEmptyScriptSignatureDefinition() {
        // Given
        String propertyName = "scriptPropertyWithoutScriptSignature";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        ComponentPropertyDescriptor.Builder builder =
                ComponentPropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(new TypeScriptDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        ComponentPropertyDescriptor descriptor = builder.build();
        assertThat(descriptor.getScriptSignatureDescriptor()).isNull();
    }
}