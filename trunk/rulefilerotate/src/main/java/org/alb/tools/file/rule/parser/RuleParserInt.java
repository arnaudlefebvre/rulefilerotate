package org.alb.tools.file.rule.parser;

import org.alb.tools.file.rule.ParsedRule;
import org.alb.tools.file.rule.exception.RuleParseException;

public interface RuleParserInt {

	public ParsedRule parse(String rawrule) throws RuleParseException;
}
