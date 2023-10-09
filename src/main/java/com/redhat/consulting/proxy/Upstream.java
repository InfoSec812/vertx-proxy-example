package com.redhat.consulting.proxy;

import io.vertx.core.net.SocketAddress;

import java.util.concurrent.atomic.AtomicInteger;

public class Upstream {

  private final SocketAddress socketAddr;

  private final AtomicInteger useCount = new AtomicInteger(0);

  private final int weight;

  public Upstream(int port, String address) {
    super();
    this.socketAddr = SocketAddress.inetSocketAddress(port, address);
    this.weight = 1;
  }

  public Upstream(SocketAddress socketAddr) {
    super();
    this.socketAddr = socketAddr;
    this.weight = 1;
  }

  public Upstream(int port, String address, int weight) {
    super();
    this.socketAddr = SocketAddress.inetSocketAddress(port, address);
    this.weight = weight;
  }

  public Upstream(SocketAddress socketAddr, int weight) {
    super();
    this.socketAddr = socketAddr;
    this.weight = weight;
  }

  public SocketAddress getSocketAddr() {
    return socketAddr;
  }

  public int getUseCount() {
    return useCount.get();
  }

  public void setUseCount(int useCount) {
    this.useCount.set(useCount);
  }

  public int incrementUseCount() {
    return this.useCount.incrementAndGet();
  }

  public double getWeightedUse() {
    return (double) this.useCount.get() / weight;
  }
}
