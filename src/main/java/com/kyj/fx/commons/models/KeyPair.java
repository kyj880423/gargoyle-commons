/**
 * 
 */
package com.kyj.fx.commons.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Key, Value Pair Class, <br/>
 * 
 * @author KYJ
 */
public class KeyPair<K, V> {

	private K key;
	private V value;

	public KeyPair() {
		super();
	}

	public KeyPair(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 11.
	 * @param k
	 * @param v
	 * @return
	 */
	public static <K, V> KeyPair<K, V> value(K k, V v) {
		return new KeyPair<K, V>(k, v);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 11.
	 * @param k
	 * @param v
	 * @return
	 */
	public static List<KeyPair<String, Object>> stringValues(String... keyPairs) {

		if (keyPairs == null)
			return Collections.emptyList();

		int length = keyPairs.length;
		List<KeyPair<String, Object>> arrayList = new ArrayList<>((length + 1) / 2);
		KeyPair<String, Object> keyPair = new KeyPair<>();
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) {
				keyPair.setKey(keyPairs[i]);
			} else {
				keyPair.setValue(keyPairs[i]);
				arrayList.add(keyPair);
				keyPair = new KeyPair<>();
			}
		}

		return arrayList;
	}

}
