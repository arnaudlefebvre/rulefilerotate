package org.alb.tools.file.rule;

import org.alb.tools.file.rule.ckecker.RuleChecker;
import org.alb.tools.file.rule.compiler.RuleCompiler;
import org.alb.tools.file.rule.exception.RuleCheckException;
import org.alb.tools.file.rule.exception.RuleCompileException;
import org.alb.tools.file.rule.exception.RuleParseException;
import org.alb.tools.file.rule.parser.RuleParser;
import org.apache.log4j.Logger;

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
