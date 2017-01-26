package com.umanis.tools.file.rule;

import org.apache.log4j.Logger;

import com.umanis.tools.file.rule.ckecker.RuleChecker;
import com.umanis.tools.file.rule.compiler.RuleCompiler;
import com.umanis.tools.file.rule.exception.RuleCheckException;
import com.umanis.tools.file.rule.exception.RuleCompileException;
import com.umanis.tools.file.rule.exception.RuleParseException;
import com.umanis.tools.file.rule.parser.RuleParser;

public class RuleBuilder {

	public static final Logger logger = Logger.getLogger(RuleBuilder.class);
	
	public RuleBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public static Rule createRule(String rawrule) throws RuleCheckException, RuleCompileException, RuleParseException {
		RuleParser ruleparser  = new RuleParser();
		RuleCompiler compiler  = new RuleCompiler();
		RuleChecker checker = new RuleChecker();
		return checker.check(compiler.compile(ruleparser.parse(rawrule)));
	}
	
	public static Rule createRule(String rawrule, Boolean disableCheck) throws RuleCheckException, RuleCompileException, RuleParseException {
		RuleParser ruleparser  = new RuleParser();
		RuleCompiler compiler  = new RuleCompiler();
		RuleChecker checker = new RuleChecker();
		return checker.check(compiler.compile(ruleparser.parse(rawrule)),disableCheck);
	}
}
