# grpc
gRpc通信

演示gRpc通信的基础demo。


# 一、编写服务

**特别注意：自动扫描的文件是在`src/main/proto`目录下的`xxx.proto`。**

建议`xxx.proto`中`option java_package = "protobuf.hello";`设置的包路径是：
`src/main/java/protobuf/hello`路径下，这样所有生成的service类都可以分功能放在`protobuf`目录下。
注意是类文件。

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "proto.generate";
option java_outer_classname = "HelloProto";
option objc_class_prefix = "HLW";

package service;

service HelloService {
    rpc SayHello (HelloRequest) returns (HelloResponse) {}
}

message HelloRequest {
    string name = 1;
}

message HelloResponse {
    string message = 1;
}
```

# 二、生成文件

运行命令

```shell
mvn protobuf:compile

mvn protobuf:compile-custom
```

将编译目录下`target/generated-sources/目录下生成的文件复制到`src/main/java/protobuf/xxxService`目录下，然后正常引用即可。

# 三、运行

先运行`HelloService`的main方法，而后运行`HelloClient`的main方法，就可以看到gRpc通信结果了。
