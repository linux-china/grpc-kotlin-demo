package org.mvnsearch.greeter

import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.Flow
import org.mvnsearch.grpc.GreeterServiceGrpcKt
import org.mvnsearch.grpc.HelloReply
import org.mvnsearch.grpc.HelloRequest

/**
 * Greeter Coroutines Service
 *
 * @author linux_china
 */
interface GreeterService {
    suspend fun sayHello(request: HelloRequest): HelloReply
    fun sayHellos(request: HelloRequest): Flow<HelloReply>
}

/**
 * Greeter Service Stub to call remote gRPC service
 * @param channel managed channel
 */
class GreeterServiceStub constructor(
    channel: ManagedChannel
) : GreeterService {
    private val stub: GreeterServiceGrpcKt.GreeterServiceCoroutineStub = GreeterServiceGrpcKt.GreeterServiceCoroutineStub(channel)

    override suspend fun sayHello(request: HelloRequest): HelloReply {
        return stub.sayHello(request)
    }

    override fun sayHellos(request: HelloRequest): Flow<HelloReply> {
        return stub.sayHellos(request)
    }
}
