/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 4. 7.
 *	작성자   : KYJ
 *
 *
 * 2019.01.23 KYJ
 * NullExpresion API Enhance. 
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.kyj.fx.commons.threads.ExecutorDemons;

/**
 * Null 혹은 콜렉션에서 빈값유형을 체크한후 Scope처리를 지원하는 유틸리티 <br/>
 * 
 * @author KYJ
 *
 */
public class NullExpresion<T> {

	/**
	 * @param o
	 * @param consumer
	 */
	public NullExpresion(T o, Consumer<T> consumer) {
		this(o, t -> t != null, consumer, other -> {
		});
	}

	/**
	 * @param o
	 * @param filter
	 * @param consumer
	 * @param other
	 */
	public NullExpresion(T o, Predicate<T> filter, Consumer<T> consumer, Consumer<Void> other) {
		this.o = o;
		this.filter = filter;
		this.consumer = consumer;
		this.other = other;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 4. 7.
	 * @param o
	 * @param consumer
	 */
	public static <T> void ifNullDo(T o, Consumer<T> consumer) {
		ifNullDo(o, consumer, (nullvalue) -> {
		});
	}

	public static <T> void ifNullDo(T o, Consumer<T> consumer, Consumer<Void> other) {
		ifNotNullDo(o, t -> t == null, consumer, other);
	}

	public static <T> void ifNotNullDo(T o, Consumer<T> consumer) {
		ifNotNullDo(o, consumer, (nullvalue) -> {
		});
	}

	public static <T> void ifNotNullDo(T o, Consumer<T> consumer, Consumer<Void> other) {
		ifNotNullDo(o, t -> t != null, consumer, other);
	}

	/**
	 * List가 null이거나 empty인경우 수행.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 22.
	 * @param o
	 * @param filter
	 * @param consumer
	 * @param other
	 */
	public static <T extends List<?>> void ifListEmptyDo(T o, Consumer<T> consumer, Consumer<Void> other) {
		ifNotNullDo(o, t -> (t == null || t.isEmpty()), consumer, other);
	}

	public static <T extends List<?>> void ifListNotEmptyDo(T o, Consumer<T> consumer) {
		ifNotNullDo(o, t -> ValueUtil.isNotEmpty(t), consumer, t -> {
		});
	}

	public static <T extends List<?>> void ifListEmptyDo(T o, Consumer<T> consumer) {
		ifNotNullDo(o, t -> (t == null || t.isEmpty()), consumer, t -> {
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 23.
	 * @param o
	 * @param filter
	 * @param consumer
	 * @param other
	 */
	public static <T> void ifNotNullDo(T o, Predicate<T> filter, Consumer<T> consumer, Consumer<Void> other) {
		new NullExpresion<T>(o, filter, consumer, other).execute();
	}

	private boolean async;
	private boolean consume;
	private T o;
	private Predicate<T> filter;
	private Consumer<T> consumer;
	private Consumer<Void> other;

	/**
	 * execute processing. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 23.
	 */
	public void execute() {

		if (consume) {
			throw new RuntimeException("Already Consumed.");
		}

		consume = true;
		Runnable command = () -> {
			if (filter.test(o)) {
				consumer.accept(o);
			} else {
				other.accept(null);
			}
		};

		if (async) {
			ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(command);
		} else
			command.run();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 23.
	 * @return
	 */
	public NullExpresion<T> async() {
		this.async = true;
		return this;
	}

	/**
	 * 
	 * create non-execute instance <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 23.
	 * @param o
	 * @param consumer
	 * @return
	 */
	public static <T> NullExpresion<T> of(T o, Consumer<T> consumer) {
		return new NullExpresion<T>(o, consumer);
	}

}
