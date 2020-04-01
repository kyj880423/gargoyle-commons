/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.collections
 *	작성일   : 2016. 10. 19.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.PrimaryStageCloseable;
import com.kyj.fx.commons.threads.DemonTimerFactory;

/**
 * @author KYJ
 *
 */
public class CachedMap<K, V> implements Map<K, V>, PrimaryStageCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedMap.class);

	private Map<K, V> valueMap;
	private HashMap<K, TimerTask> scheduleMap;
	private Timer timer;
	private long cachetime;

	private List<Consumer<Object>> registedListeners = new ArrayList<>();
	private List<Consumer<Object>> scheduleCancelListeners = new ArrayList<>();
	private List<Consumer<Object>> expiredListeners = new ArrayList<>();

	public void addOnRegistedLister(Consumer<Object> listener) {
		this.registedListeners.add(listener);
	}

	public void addOnScheduleCancelListeners(Consumer<Object> listener) {
		this.scheduleCancelListeners.add(listener);
	}

	public void addOnExpiredListeners(Consumer<Object> listener) {
		this.expiredListeners.add(listener);

	}

	/**
	 * @param cachetime
	 *            millisecond
	 */
	public CachedMap(long cachetime) {
		this(new HashMap<>(), cachetime);

	}

	/**
	 * @param valueMap
	 * @param cachetime
	 *            millisecond
	 */
	public CachedMap(Map<K, V> valueMap, long cachetime) {
		this("Gargoyle-CacheMap", valueMap, cachetime);
	}

	/**
	 * @param valueMap
	 * @param cachetime
	 *            millisecond
	 */
	public CachedMap(String threadName, Map<K, V> valueMap, long cachetime) {
		this.valueMap = valueMap;
		/*
		 * 파라미터 설명 name, isDemon
		 */

		this.timer = DemonTimerFactory.newInsance(threadName); // new
																		// Timer("cacheMap",
																		// true);
		this.cachetime = cachetime;
		this.scheduleMap = new HashMap<>();

		// timer클래스가 종료되는 시점에 종료처리하기위해 리스너에 등록.
		StageStore.addPrimaryStageCloseListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return valueMap.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return valueMap.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return valueMap.containsValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		return valueMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		V put = null;
		synchronized (timer) {

			cancelSchedule(key);

			put = valueMap.put(key, value);

			registSchedule(key);
		}

		return put;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {

		synchronized (timer) {

			cancelSchedule(key);
			return valueMap.remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {

		synchronized (timer) {
			cancelSchedule(m);
			valueMap.putAll(m);
		}

		registSchedule(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {

		synchronized (timer) {
			cancelSchedule(valueMap);
			valueMap.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		return valueMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values() {
		return valueMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return valueMap.entrySet();
	}

	/*
	 * Not Override.
	 *
	 * 멀티 스레드 환경에서 toString은 위험
	 */
	@Override
	public final String toString() {
		return super.toString();
	}

	private void registSchedule(K key) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				valueMap.remove(key);
			}

		};
		timer.schedule(task, cachetime);

		scheduleMap.put(key, task);
	}

	private void registSchedule(Map<? extends K, ? extends V> m) {

		m.forEach((k, v) -> {
			registedListeners.forEach(l -> l.accept(k));

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					valueMap.remove(k);

				}
			}, cachetime);

		});

	}

	// private List<Consumer<Map.Entry<K, V>>> registedListeners = new
	// ArrayList<>();
	// private List<Consumer<Map.Entry<K, V>>> scheduleCancelListeners = new
	// ArrayList<>();
	// private List<Consumer<Map.Entry<K, V>>> expiredListeners = new
	// ArrayList<>();

	private void cancelSchedule(Object key) {
		if (scheduleMap.containsKey(key)) {
			TimerTask timerTask = scheduleMap.get(key);
			timerTask.cancel();
			scheduleCancelListeners.forEach(v -> v.accept(key));
		}
	}

	private void cancelSchedule(Map<? extends K, ? extends V> m) {
		Iterator<? extends K> iterator = m.keySet().iterator();
		while (iterator.hasNext()) {
			K next = iterator.next();
			if (scheduleMap.containsKey(next)) {
				TimerTask timerTask = scheduleMap.get(next);
				timerTask.cancel();
				scheduleCancelListeners.forEach(v -> v.accept(next));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.PrimaryStageCloseable#closeRequest()
	 */
	@Override
	public void closeRequest() {
		LOGGER.debug("CachedMap cancel request.");

		// 반드시 호출할것.
		if (timer != null)
			timer.cancel();
		else
			LOGGER.debug("CachedMap timer is empty.");

		LOGGER.debug("CachedMap cancel finish.");
	}

}
