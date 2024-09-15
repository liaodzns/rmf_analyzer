package finalproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

//import static org.junit.jupiter.api.Assertions.assertEquals;


public class MyHashTable<K,V> implements Iterable<MyPair<K,V>>{
	// num of entries to the table
	private int size;
	// num of buckets 
	private int capacity = 16;
	// load factor needed to check for rehashing 
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<MyPair<K,V>>> buckets;

	// constructors
	public MyHashTable() {
		// ADD YOUR CODE BELOW THIS
        this.size = 0;
        this.buckets = new ArrayList<LinkedList<MyPair<K,V>>>(capacity);
        for (int i = 0; i < this.capacity; i++) {
            this.buckets.add(new LinkedList<MyPair<K,V>>());
        }
		//ADD YOUR CODE ABOVE THIS
	}

	public MyHashTable(int initialCapacity) {
		// ADD YOUR CODE BELOW THIS
        this.size = 0;
        this.capacity = initialCapacity;
        this.buckets = new ArrayList<LinkedList<MyPair<K,V>>>(capacity);
        for (int i = 0; i < this.capacity; i++) {
            this.buckets.add(new LinkedList<MyPair<K,V>>());
        }
		//ADD YOUR CODE ABOVE THIS
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public int numBuckets() {
		return this.capacity;
	}

	/**
	 * Returns the buckets variable. Useful for testing  purposes.
	 */
	public ArrayList<LinkedList< MyPair<K,V> > > getBuckets(){
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key. 
	 */
	public int hashFunction(K key) {
		int hashValue = Math.abs(key.hashCode()) % this.capacity;
		return hashValue;
	}

	/**
	 * Takes a key and a value as input and adds the corresponding HashPair
	 * to this HashTable. Expected average run time  O(1)
	 */
	public V put(K key, V value) {
		//  ADD YOUR CODE BELOW HERE

        boolean addNewKey = true;
        int index = this.hashFunction(key);
        LinkedList<MyPair<K, V>> bucket = this.buckets.get(index);

        for (MyPair<K, V> pair : bucket) {
            if (pair.getKey().equals(key)) {
                V oldValue = pair.getValue();
                pair.setValue(value);
                addNewKey = false;
                return oldValue;
            }
        }
        if (addNewKey) {
            MyPair<K, V> newPair = new MyPair<>(key, value);
            bucket.add(newPair);
            this.size++;
        }

        if ((double)size / capacity > MAX_LOAD_FACTOR) {
            rehash();
        }

        return null;

		//  ADD YOUR CODE ABOVE HERE
	}


	/**
	 * Get the value corresponding to key. Expected average runtime O(1)
	 */

	public V get(K key) {
		//ADD YOUR CODE BELOW HERE
        int index = this.hashFunction(key);
        LinkedList<MyPair<K, V>> bucket = this.buckets.get(index);

        for (MyPair<K, V> pair : bucket) {
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }
		return null;

		//ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1) 
	 */
	public V remove(K key) {
		//ADD YOUR CODE BELOW HERE

        int index = hashFunction(key);
        LinkedList<MyPair<K,V>> bucket = buckets.get(index);

        Iterator<MyPair<K,V>> iterator = bucket.iterator();
        while(iterator.hasNext()){
            MyPair<K,V> pair = iterator.next();
            if(pair.getKey().equals(key)){
                V value = pair.getValue();
                iterator.remove();
                size--;
                return value;
            }
        }
		return null;

		//ADD YOUR CODE ABOVE HERE
	}


	/** 
	 * Method to double the size of the hashtable if load factor increases
	 * beyond MAX_LOAD_FACTOR.
	 * Made public for ease of testing.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */
	public void rehash() {
		//ADD YOUR CODE BELOW HERE
        int newCapacity = this.capacity * 2;
        ArrayList<LinkedList<MyPair<K,V>>> newBuckets = new ArrayList<>(newCapacity);

        // Initialize the new buckets
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new LinkedList<>());
        }

        // Rehash all the existing entries into the new buckets
        for (LinkedList<MyPair<K,V>> bucket : buckets) {
            for (MyPair<K,V> entry : bucket) {
                int newHash = Math.abs(entry.getKey().hashCode()) % newCapacity;
                newBuckets.get(newHash).add(entry);
            }
        }

        // Update the hash table's variables
        this.capacity = newCapacity;
        this.buckets = newBuckets;

		//ADD YOUR CODE ABOVE HERE
	}


	/**
	 * Return a list of all the keys present in this hashtable.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public ArrayList<K> getKeySet() {
		//ADD YOUR CODE BELOW HERE
        ArrayList<K> keys = new ArrayList<K>();
        for (LinkedList<MyPair<K,V>> bucket : buckets) {
            for (MyPair<K,V> pair : bucket) {
                keys.add(pair.getKey());
            }
        }
        return keys;
		//ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Returns an ArrayList of unique values present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<V> getValueSet() {
		//ADD CODE BELOW HERE
        ArrayList<V> values = new ArrayList<V>();
        for (LinkedList<MyPair<K,V>> bucket : buckets) {
            for (MyPair<K,V> pair : bucket) {
                if (!values.contains(pair.getValue())) {
                    values.add(pair.getValue());
                }
            }
        }
        return values;
		//ADD CODE ABOVE HERE
	}


	/**
	 * Returns an ArrayList of all the key-value pairs present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<MyPair<K, V>> getEntries() {
		//ADD CODE BELOW HERE
        ArrayList<MyPair<K,V>> entries = new ArrayList<MyPair<K,V>>();
        for (LinkedList<MyPair<K,V>> bucket : buckets) {
            for (MyPair<K,V> pair : bucket) {
                entries.add(pair);
            }
        }
        return entries;
		//ADD CODE ABOVE HERE
	}

	
	
	@Override
	public MyHashIterator iterator() {
		return new MyHashIterator();
	}   

	
	private class MyHashIterator implements Iterator<MyPair<K,V>> {
        private int currentBucket;
        private Iterator<MyPair<K, V>> currentBucketIterator;
		private MyHashIterator() {
			//ADD YOUR CODE BELOW HERE
            currentBucket = 0;
            currentBucketIterator = buckets.get(currentBucket).iterator();
            while (!currentBucketIterator.hasNext() && currentBucket < buckets.size() - 1) {
                currentBucket++;
                currentBucketIterator = buckets.get(currentBucket).iterator();
            }
            //ADD YOUR CODE ABOVE HERE
		}

		@Override
		public boolean hasNext() {
			//ADD YOUR CODE BELOW HERE
            if (currentBucketIterator.hasNext()) {
                return true;
            }
            for (int i = currentBucket + 1; i < buckets.size(); i++) {
                if (!buckets.get(i).isEmpty()) {
                    return true;
                }
            }
            return false;

			//ADD YOUR CODE ABOVE HERE
		}

		@Override
		public MyPair<K,V> next() {
			//ADD YOUR CODE BELOW HERE
            if (!currentBucketIterator.hasNext()) {
                currentBucket++;
                while (currentBucket < buckets.size() && buckets.get(currentBucket).isEmpty()) {
                    currentBucket++;
                }
                currentBucketIterator = buckets.get(currentBucket).iterator();
            }
            return currentBucketIterator.next();
			//ADD YOUR CODE ABOVE HERE
		}

	}

}

