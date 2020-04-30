package com.camunda.invade.invader;

import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;


import org.camunda.bpm.client.ExternalTaskClient;

public class InvadeWorker {

    private final static Logger LOGGER = Logger.getLogger(InvadeWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();



        // subscribe to an external task topic as specified in the process
        client.subscribe("DecideDirection")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    Random random = new Random();

                    boolean north = random.nextBoolean();


                    LOGGER.info("Decided on direction "+ north);

                    // Complete the task, add process variables here
                    externalTaskService.complete(externalTask, Collections.singletonMap("north", north));
                })
                .open();


        // subscribe to an external task topic as specified in the process
        client.subscribe("Gaul")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    LOGGER.info("Invading Gaul!");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();

        // subscribe to an external task topic as specified in the process
        client.subscribe("Persia")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    LOGGER.info("Invading Persia!");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}