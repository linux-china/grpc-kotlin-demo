package org.mvnsearch.greeter

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mvnsearch.grpc.HelloRequest
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreeterServiceTest {
  lateinit var grpcChannel: ManagedChannel
  lateinit var greeterService: GreeterService

  @BeforeAll
  fun setUp() {
    this.grpcChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
      .usePlaintext()
      .executor(Dispatchers.Default.asExecutor())
      .build()
    greeterService = GreeterServiceStub(this.grpcChannel)
  }

  @AfterAll
  fun tearDown() {
    this.grpcChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  @Test
  fun testHello() = runBlocking {
    val request = HelloRequest.newBuilder().setName("Jackie").build()
    val response = async { greeterService.sayHello(request) }
    println("Received: ${response.await().message}")
  }
}
