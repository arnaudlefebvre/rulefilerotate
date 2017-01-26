package com.umanis.tools.file.rule.ckecker;

import com.umanis.tools.file.rule.CompiledRule;
import com.umanis.tools.file.rule.Rule;
import com.umanis.tools.file.rule.exception.RuleCheckException;

public interface RuleCheckInt {
	public Rule check(CompiledRule rule) throws RuleCheckException;
	public Rule check(CompiledRule rule, boolean disableCheck) throws RuleCheckException;
}
