package org.alb.tools.file.rule.compiler;

import org.alb.tools.file.rule.CompiledRule;
import org.alb.tools.file.rule.ParsedRule;
import org.alb.tools.file.rule.exception.RuleCompileException;

public interface RuleCompilerInt {

	public CompiledRule compile(ParsedRule rule) throws RuleCompileException;
	
}
