package com.haogrgr.test.pattern.strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {

	public static void main(String[] args) {
		MsgProcessStrategy strategy = new MsgProcessStrategy();

		//所有msg的内容前加上消息类型 如:  hello! --> info : hello!
		strategy.addRule(MsgRuleBuilder.when(msg -> true)
				.goon()
				.then(msg -> msg.setContent(msg.getType() + " : " + msg.getContent())));

		//warn和error类型的msg加上html红色标签
		strategy.addRule(MsgRuleBuilder.when(msg -> Msg.WARN.equals(msg.getType()))
				.or(msg -> Msg.ERROR.equals(msg.getType()))
				.goon()
				.then(msg -> msg.setContent("<font color=\"FF0000\">" + msg.getContent() + "</font>")));

		Msg before = new Msg(1, Msg.WARN, "hello!");
		System.out.println(before);
		Msg after = strategy.apply(before);
		System.out.println(after);

		before = new Msg(1, Msg.INFO, "hello!");
		System.out.println(before);
		after = strategy.apply(before);
		System.out.println(after);

	}

}

class Msg {

	public static final String INFO = "info", WARN = "warn", ERROR = "error";

	private Integer id;
	private String type, content;

	public Msg(Integer id, String type, String content) {
		this.id = id;
		this.type = type;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public Msg setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public Msg setType(String type) {
		this.type = type;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Msg setContent(String content) {
		this.content = content;
		return this;
	}

	@Override
	public String toString() {
		return "Msg [id=" + id + ", type=" + type + ", content=" + content + "]";
	}

}

class MsgProcessStrategy implements Function<Msg, Msg> {

	List<MsgRule> rules = new LinkedList<>();

	public MsgProcessStrategy addRule(MsgRule rule) {
		this.rules.add(rule);
		return this;
	}

	@Override
	public Msg apply(Msg before) {
		Msg after = before;
		for (MsgRule rule : rules) {
			if (rule.accept(after))
				after = rule.process(after);
			if (!rule.goon())
				return after;
		}
		return after;
	}

}

class MsgRuleBuilder {

	private Predicate<Msg> predicate;
	private boolean goon = false;

	public MsgRuleBuilder(Predicate<Msg> predicate) {
		this.predicate = predicate;
	}

	public static MsgRuleBuilder when(Predicate<Msg> predicate) {
		return new MsgRuleBuilder(predicate);
	}

	public MsgRuleBuilder and(Predicate<Msg> one) {
		this.predicate = this.predicate.and(one);
		return this;
	}

	public MsgRuleBuilder or(Predicate<Msg> one) {
		this.predicate = this.predicate.or(one);
		return this;
	}

	public MsgRuleBuilder goon() {
		this.goon = true;
		return this;
	}

	public MsgRule then(Function<Msg, Msg> function) {
		return new MsgRule(predicate, function, goon);
	}

}

class MsgRule {

	private Predicate<Msg> predicate;
	private Function<Msg, Msg> function;
	private boolean goon;

	public MsgRule(Predicate<Msg> p, Function<Msg, Msg> f, boolean goon) {
		this.predicate = p;
		this.function = f;
		this.goon = goon;
	}

	public boolean accept(Msg msg) {
		return predicate.test(msg);
	}

	public Msg process(Msg msg) {
		return function.apply(msg);
	}

	public boolean goon() {
		return this.goon;
	}
}

class MsgProcessStrategyFactory {

	public static MsgProcessStrategy get() {
		MsgProcessStrategy strategy = new MsgProcessStrategy();

		//所有msg的内容前加上消息类型 如:  hello! --> info : hello!
		strategy.addRule(MsgRuleBuilder.when(msg -> true).goon()
				.then(msg -> msg.setContent(msg.getType() + " : " + msg.getContent())));

		//warn和error类型的msg加上html红色标签
		strategy.addRule(MsgRuleBuilder.when(msg -> Msg.WARN.equals(msg.getType()))
				.or(msg -> Msg.ERROR.equals(msg.getType())).goon()
				.then(msg -> msg.setContent("<font color=\"FF0000\">" + msg.getContent() + "</font>")));

		return strategy;
	}

}