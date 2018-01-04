package org.alb.tools.cli;


public class Option extends org.apache.commons.cli.Option {
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 5134569086096621206L;
	protected Integer order;
	protected Long importance;
	
	public static final Long HELP = 0L;
	public static final Long REQUIRED = 10L;
	public static final Long REQ_CAT1 = 12L;
	public static final Long REQ_CAT2 = 15L;
	public static final Long REQ_CAT3 = 18L;
	public static final Long OPTIONNAL = 20L;
	public static final Long OPT_CAT1 = 22L;
	public static final Long OPT_CAT2 = 25L;
	public static final Long OPT_CAT3 = 28L;
	public static final Long MANDATORY = 30L;
	public static final Long MAN_CAT1 = 32L;
	public static final Long MAN_CAT2 = 35L;
	public static final Long MAN_CAT3 = 38L;
	
	
	public Option(org.apache.commons.cli.Option o,Integer order) {
		super(o.getOpt(),o.getLongOpt(),o.hasArg(),o.getDescription());
		this.setRequired(o.isRequired());
		this.setArgName(o.getArgName());
		this.setArgs(o.getArgs());
		this.setOptionalArg(o.hasOptionalArg());
		this.setType(o.getType());
		this.setValueSeparator(o.getValueSeparator());
		this.order = order;
		this.importance = this.HELP;
	}
	
	public Option(org.apache.commons.cli.Option o,Long importance, Integer order) {
		super(o.getOpt(),o.getLongOpt(),o.hasArg(),o.getDescription());
		this.setRequired(o.isRequired());
		this.setArgName(o.getArgName());
		this.setArgs(o.getArgs());
		this.setOptionalArg(o.hasOptionalArg());
		this.setType(o.getType());
		this.setValueSeparator(o.getValueSeparator());
		this.order = order;
		this.importance = importance;
	}
	
	public Option(org.apache.commons.cli.Option o,Long importance) {
		super(o.getOpt(),o.getLongOpt(),o.hasArg(),o.getDescription());
		this.setRequired(o.isRequired());
		this.setArgName(o.getArgName());
		this.setArgs(o.getArgs());
		this.setOptionalArg(o.hasOptionalArg());
		this.setType(o.getType());
		this.setValueSeparator(o.getValueSeparator());
		this.order = 0;
		this.importance = importance;
	}

	public Long getImportance() {
		return importance;
	}


	public void setImportance(Long importance) {
		this.importance = importance;
	}


	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
	}
	
	

}
