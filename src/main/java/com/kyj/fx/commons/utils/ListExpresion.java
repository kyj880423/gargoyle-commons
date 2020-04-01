/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 4. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author KYJ
 *
 */
public class ListExpresion {

	public static <T> boolean contains(List<T> list, Predicate<T> predicate) {
		return list.stream().filter(predicate).findFirst().isPresent();
	}

	public static <T> boolean contains(List<T> list, int index, Predicate<T> predicate) {
		if (list.size() > index)
			return contains(list, predicate);
		else
			return false;
	}

	public static <T> void containsAction(List<T> list, Predicate<T> predicate, Consumer<T> consumer) {
		list.stream().filter(predicate).findFirst().ifPresent(consumer);
	}

	public static <T> void containsAction(List<T> list, int index, Consumer<T> consumer) {
		containsAction(list, index, consumer, null);
	}

	public static <T> void containsAction(List<T> list, int index, Consumer<T> consumer, Consumer<Void> emptyThan) {
		if (0 <= index && index < list.size()) {
			consumer.accept(list.get(index));
		} else {
			if (emptyThan != null)
				emptyThan.accept(null);
		}
	}

	public static <T, R> R containsMapper(List<T> list, int index, Function<T, R> mapper, Function<T, R> emptyThan) {
		if (0 <= index && index < list.size())
			return mapper.apply(list.get(index));
		else {
			if (emptyThan != null)
				return emptyThan.apply(null);
			return null;
		}
	}

	/**
	 * 빈값여부를 체크하고 스트림객체를 생성하는 함수.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 14.
	 * @param values
	 * @return
	 */
	public static <T> Stream<T> of(List<T> values) {
		if (values != null && !values.isEmpty())
			return values.stream();
		return Stream.empty();
	}


}
