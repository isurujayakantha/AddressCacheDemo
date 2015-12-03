package com.agoda.exam.addresscache.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.agoda.exam.addresscache.main.AddressCache;

import junit.framework.Assert;

public class AddressCacheTest {

	public AddressCache getCreatedAddressCache(int limit) {
		AddressCache addressCache = new AddressCache(limit, null);
		try {
			InetAddress addr1 = InetAddress.getByName("127.0.0.1");
			boolean el1 = addressCache.add(addr1);

			InetAddress addr2 = InetAddress.getByName("127.0.0.2");
			boolean el2 = addressCache.add(addr2);

			InetAddress addr3 = InetAddress.getByName("127.0.0.3");
			boolean el3 = addressCache.add(addr3);

			InetAddress addr4 = InetAddress.getByName("127.0.0.4");
			boolean el4 = addressCache.add(addr4);

		} catch (UnknownHostException hostEx) {
			hostEx.printStackTrace();
			System.out.println("ERROR : Unknown Host Address." + hostEx.getMessage());
		}
		return addressCache;
	}

	@Test
	public void testAddAllAddresses() {
		AddressCache addressCache = getCreatedAddressCache(1000);
		Assert.assertTrue(addressCache.getSize() == 4);
	}

	@Test
	public void testRemoveAddress() {
		AddressCache addressCache = getCreatedAddressCache(1000);
		InetAddress address = addressCache.peek();
		Assert.assertTrue(addressCache.remove(address));
		Assert.assertFalse(addressCache.remove(address));
	}

	@Test
	public void testGetLatestAddress() {
		AddressCache addressCache = getCreatedAddressCache(1000);
		InetAddress address = addressCache.peek();
		Assert.assertEquals(address.getHostAddress(), "127.0.0.4");
	}

	@Test
	public void testGetAndRemoveLatestAddress() {
		AddressCache addressCache = getCreatedAddressCache(1000);
		InetAddress address = addressCache.take();
		Assert.assertEquals(address.getHostAddress(), "127.0.0.4");
		Assert.assertFalse(addressCache.remove(address));
	}

	@Test
	public void testExpireAddress() throws InterruptedException {
		AddressCache addressCache = getCreatedAddressCache(1000);
		Thread.sleep(5000); // Waiting to expire and auto remove addresses
		System.out.println(addressCache.getSize());
		Assert.assertTrue(addressCache.getSize() == 0);
	}

}
