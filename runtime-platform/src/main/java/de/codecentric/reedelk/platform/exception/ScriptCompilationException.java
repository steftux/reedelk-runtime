package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.script.ScriptBlock;
import de.codecentric.reedelk.runtime.api.script.ScriptSource;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;

import java.util.Collection;

public class ScriptCompilationException extends PlatformException {

    public ScriptCompilationException(ScriptSource scriptSource, Throwable cause) {
        super(messageFrom(scriptSource, cause), cause);
    }

    public ScriptCompilationException(ScriptBlock scriptBlock, Throwable cause) {
        super(messageFrom(scriptBlock, cause), cause);
    }

    private static String messageFrom(ScriptSource source, Throwable cause) {
        Collection<String> moduleNames = source.scriptModuleNames();
        String scriptResource = source.resource();
        String error = StackTraceUtils.rootCauseMessageOf(cause);
        return Messages.Script.SCRIPT_SOURCE_COMPILATION_ERROR.format(error, scriptResource, moduleNames);
    }

    private static String messageFrom(ScriptBlock scriptBlock, Throwable cause) {
        String body = scriptBlock.body();
        String error = StackTraceUtils.rootCauseMessageOf(cause);
        return Messages.Script.SCRIPT_BLOCK_COMPILATION_ERROR.format(error, body);
    }
}
