package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase{
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
         StreamObserver<billing.BillingResponse> responseObserver){
        
        log.info("createBillingAccount request recieved {}", billingRequest.toString());

        // Business logic - e.g save to database, perform calculates etc

        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId("12345")
            .setStatus("Active")
            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
    } 
}
