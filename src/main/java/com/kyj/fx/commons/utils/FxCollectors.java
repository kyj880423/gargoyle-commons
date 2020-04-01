/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 7. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author KYJ
 *
 */
public final class FxCollectors {

	/*[start]copy from Collector */
	static final Set<Collector.Characteristics> CH_CONCURRENT_ID = Collections.unmodifiableSet(EnumSet
			.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
	static final Set<Collector.Characteristics> CH_CONCURRENT_NOID = Collections
			.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
	static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
	static final Set<Collector.Characteristics> CH_UNORDERED_ID = Collections
			.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
	static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
	/*[end]copy from Collector */

	private FxCollectors() {
	}

	/**
	 * ObservableList형태로 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 20.
	 * @return
	 */
	public static <T> Collector<T, ?, ObservableList<T>> toObservableList() {

		return new CollectorImpl<>((Supplier<ObservableList<T>>) () -> FXCollections.observableArrayList(), ObservableList::add,
				(left, right) -> {
					left.addAll(right);
					return left;
				} , CH_ID);

	}

	/**
	 *  copy from Collectors.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 20.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R) i;
	}

	/**
	 *
	 * 2016. 7. 20. copy from Collectors.
	 *
	* Simple implementation class for {@code Collector}.
	*
	* @param <T> the type of elements to be collected
	* @param <R> the type of the result
	*/
	static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher,
				Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
				Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, castingIdentity(), characteristics);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}
	}
}
