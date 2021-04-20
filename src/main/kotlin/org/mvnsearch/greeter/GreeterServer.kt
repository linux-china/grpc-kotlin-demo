package org.mvnsearch.greeter

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.mvnsearch.grpc.GreeterServiceGrpcKt
import org.mvnsearch.grpc.HelloReply
import org.mvnsearch.grpc.HelloRequest

class GreeterServer constructor(
  private val port: Int
) {
  private val server: Server = ServerBuilder
    .forPort(port)
    .addService(GreeterServiceImpl())
    .addService(ProtoReflectionService.newInstance())
    .build()

  fun start() {
    server.start()
    println("Server started, listening on $port")
    Runtime.getRuntime().addShutdownHook(
      Thread {
        println("*** shutting down gRPC server since JVM is shutting down")
        this@GreeterServer.stop()
        println("*** server shut down")
      }
    )
  }

  private fun stop() {
    server.shutdown()
  }

  fun blockUntilShutdown() {
    server.awaitTermination()
  }

  private class GreeterServiceImpl : GreeterServiceGrpcKt.GreeterServiceCoroutineImplBase(), GreeterService {
    override suspend fun sayHello(request: HelloRequest): HelloReply {
      return HelloReply
        .newBuilder()
        .setMessage("Hello ${request.name}")
        .build()
    }

    override fun sayHellos(request: HelloRequest): Flow<HelloReply> {
      return arrayOf("Hello", "你好").map {
        HelloReply.newBuilder().setMessage("$it ${request.name}").build()
      }.asFlow()
    }
  }
}

fun main() {
  val port = 50052
  val server = GreeterServer(port)
  server.start()
  server.blockUntilShutdown()
}
