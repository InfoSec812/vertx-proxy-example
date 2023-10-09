package com.redhat.consulting.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;

import static io.netty.handler.codec.compression.StandardCompressionOptions.deflate;
import static io.netty.handler.codec.compression.StandardCompressionOptions.gzip;

public class MainVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    var serverOpts = new HttpServerOptions()
                       .setCompressionSupported(true)
                       .setCompressionLevel(2)
                       .addCompressor(deflate())
                       .addCompressor(gzip())
                       .setPort(7080);

    var proxyServer = vertx.createHttpServer(serverOpts);

    var proxyRouter = Router.router(vertx);

    var client = vertx.createHttpClient();

    var proxy = HttpProxy.reverseProxy(client);
    proxy.origin(8080, "192.168.100.10");

    proxyRouter.route().handler(ProxyHandler.create(proxy));

    proxyServer.requestHandler(proxyRouter)
      .listen()
      .onSuccess(server -> startPromise.complete())
      .onFailure(startPromise::fail);
  }
}
