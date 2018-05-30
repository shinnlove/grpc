/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.shinnlove.grpc.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * gRpc客户端。
 *
 * 使用`ManagedChannel`来打开一个服务端TPC/IP套接字通道。
 * 在客户端本地存根是通过`xxxService`的`newBlockingStub`来构建`xxxBlockingStub`存根。
 * 而后客户端本地调用就是使用存根的`xxx`方法来调用。
 *
 * @author shinnlove.jinsheng
 * @version $Id: HelloClient.java, v 0.1 2018-05-30 下午2:28 shinnlove.jinsheng Exp $$
 */
public class HelloClient {

    /** 连接服务端的套接字通道 */
    private final ManagedChannel                                       channel;

    /** 客户端本地服务存根 */
    private final grpc.proto.HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public HelloClient(String host, int port) {
        // 初始化端口套接字连接
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        // 在套接字通道上建立blocking存根
        blockingStub = grpc.proto.HelloServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 关闭客户端连接。
     *
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException{
        // 关闭连接
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 客户端调用本地服务。
     *
     * @param name
     * @return
     */
    public String sayHello(String name){
        // 构造服务调用参数对象
        grpc.proto.HelloRequest request = grpc.proto.HelloRequest.newBuilder().setName(name).build();
        // 调用远程服务方法(直接将本地存根视为服务调用)
        grpc.proto.HelloResponse response = blockingStub.sayHello(request);
        // 返回调用结果
        return response.getMessage();
    }

    public static void main(String[] args) throws Exception {
        HelloClient client = new HelloClient("127.0.0.1", 50051);
        String response = client.sayHello("shinnlove");
        System.out.println(response);
        client.shutdown();
    }

}