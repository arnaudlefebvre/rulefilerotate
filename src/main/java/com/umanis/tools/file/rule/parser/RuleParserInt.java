package com.umanis.tools.file.rule.parser;

import com.umanis.tools.file.rule.ParsedRule;
import com.umanis.tools.file.rule.exception.RuleParseException;

public interface RuleParserInt {

	public ParsedRule parse(String rawrule) throws RuleParseException;
}
