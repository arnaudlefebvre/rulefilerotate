package com.umanis.tools.file.rule.compiler;

import com.umanis.tools.file.rule.CompiledRule;
import com.umanis.tools.file.rule.ParsedRule;
import com.umanis.tools.file.rule.exception.RuleCompileException;

public interface RuleCompilerInt {

	public CompiledRule compile(ParsedRule rule) throws RuleCompileException;
	
}
