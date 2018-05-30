/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.shinnlove.grpc.demo;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import protobuf.hello.HelloRequest;
import protobuf.hello.HelloResponse;
import protobuf.hello.HelloServiceGrpc;

/**
 * gRpc的服务端。
 *
 * `io.grpc.Server`是由`ServerBuilder`绑定一个端口、`addService`添加一个服务后`build`返回的gRpc服务。
 *
 * 服务端实现具体`proto`类服务的时候，会添加一个形参`StreamObserver<T> responseObserver`；
 * 这个观察者使用`onNext`将返回结果传入stream、使用`onCompleted`来通知通知stream结束。
 *
 * @author shinnlove.jinsheng
 * @version $Id: HelloServer.java, v 0.1 2018-05-30 下午2:12 shinnlove.jinsheng Exp $$
 */
public class HelloServer {

    /** gRpc端口号 */
    private int    port = 50051;

    /** gRpc服务端 */
    private Server server;

    /**
     * gRpc服务器启动。
     *
     * @throws IOException
     */
    private void start() throws IOException {
        // 初始化并启动服务
        server = ServerBuilder.forPort(port).addService(new HelloServiceImpl()).build().start();

        // 注册JVM钩子（退出JVM的时候执行）
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HelloServer.this.stop();
            }
        });
    }

    /**
     * gRpc关闭服务器。
     */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * 阻塞直到退出程序。
     *
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * 实现服务接口的类
     */
    private class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

        @Override
        public void sayHello(HelloRequest req,
                             StreamObserver<HelloResponse> responseObserver) {
            // 构建返回结果对象
            HelloResponse reply = HelloResponse.newBuilder()
                .setMessage("hello " + req.getName()).build();
            // 将返回结果传入stream，返回调用方
            responseObserver.onNext(reply);
            // 通知stream结束
            responseObserver.onCompleted();
        }

    }

    public static void main(String[] args) throws Exception {
        final HelloServer server = new HelloServer();
        server.start();
        server.blockUntilShutdown();
    }

}