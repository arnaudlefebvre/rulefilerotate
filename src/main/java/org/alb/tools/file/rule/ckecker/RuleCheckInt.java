package org.alb.tools.file.rule.ckecker;

import org.alb.tools.file.rule.CompiledRule;
import org.alb.tools.file.rule.Rule;
import org.alb.tools.file.rule.exception.RuleCheckException;

public interface RuleCheckInt {
	public Rule check(CompiledRule rule) throws RuleCheckException;
	public Rule check(CompiledRule rule, boolean disableCheck) throws RuleCheckException;
}
