package com.agoda.exam.addresscache.main;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author Isuru Jayakantha
 * @version 1.0
 */

/*
 * The AddressCache has a max age for the elements it's storing, an add method
 * for adding elements, a remove method for removing, a peek method which
 * returns the most recently added element, and a take method which removes and
 * returns the most recently added element.
 */
public class AddressCache {
	private ConcurrentLinkedDeque<InetAddress> addressCache;
	private int maxAge;

	/**
	 * AddressCache constructor.
	 * 
	 * @param maxAge
	 *            in seconds
	 * @param unit
	 */
	public AddressCache(final int maxAge, final TimeUnit unit) {
		addressCache = new ConcurrentLinkedDeque<>();
		this.maxAge = maxAge;
		/**
		 * Scan cache every 30 minutes, remove expired elements.
		 */
		new CacheManagerThread().start();
	}

	/**
	 * add() method must store unique elements only (existing elements must be
	 * ignored). This will return true if the element was successfully added.
	 * 
	 * @param address
	 * @return true if added successfully
	 */

	public boolean add(final InetAddress address) {
		if (address != null) {
			return addressCache.offer(address);
		} else {
			return false;
		}

	}

	/**
	 * remove() method will return true if the address was successfully removed
	 * 
	 * @param address
	 * @return InetAddress
	 */
	public boolean remove(final InetAddress address) {
		if (address != null && !addressCache.isEmpty()) {
			return addressCache.remove(address);
		} else {
			return false;
		}
	}

	/**
	 * The peek() method will return the most recently added element, null if no
	 * element exists.
	 * 
	 * @return
	 */
	public InetAddress peek() {
		return addressCache.peekLast();
	}

	/**
	 * take() method retrieves and removes the most recently added element from
	 * the cache and waits if necessary until an element becomes available.
	 * 
	 * @return
	 */
	public InetAddress take() {
		return addressCache.pollLast();
	}

	public int getSize() {
		return addressCache.size();
	}

	class CacheManagerThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(maxAge);
				while (true) {
					if (!addressCache.isEmpty()) {
						InetAddress address = take();
						System.out.println("EXPIRED : " + address.getHostAddress());
					}
					Thread.sleep(maxAge);
				}
			} catch (InterruptedException e) {
				System.out.println("Error Occured Shutting down the caching system.");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
