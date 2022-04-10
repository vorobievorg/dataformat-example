/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm;

import java.io.StringWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.camunda.bpm.client.variable.value.XmlValue;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {
  
  public static void main(String... args) throws JAXBException {
    ExternalTaskClient client = ExternalTaskClient.create()
        .baseUrl("http://localhost:8080/engine-rest/")
        .asyncResponseTimeout(10000)
        .disableBackoffStrategy()
        .disableAutoFetching()
        .maxTasks(1)
        .build();
    
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
   /* Marshaller customerMarshaller = JAXBContext.newInstance(Customer.class).createMarshaller();
    customerMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    */

    
    TopicSubscriptionBuilder jsonSubscriptionBuilder = client.subscribe("jsonCustomerCreation")
      .lockDuration(20000)
      .handler((externalTask, externlTaskService) -> {
          VariableMap variables = Variables.createVariables();

              variables.putValue("col", ClientValues.jsonValue("[\"aa\",\"bb\",\"cc\",\"dd\"]"));

          externlTaskService.complete(externalTask, variables);
        
      });




      TopicSubscriptionBuilder iteration = client.subscribe("iteration")
              .lockDuration(20000)
              .handler((externalTask, externalTaskService) -> {
                  externalTaskService.extendLock(externalTask,20000);
                  String colvar = externalTask.getVariable("colvar");
                  System.out.println(Thread.currentThread().getId()+" start:"+colvar+" "+ LocalTime.now());
                  try {
                      Thread.sleep(5000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  System.out.println(Thread.currentThread().getName()+" finish:"+colvar);

                  externalTaskService.complete(externalTask);
              });



    client.start();
    jsonSubscriptionBuilder.open();


      ExecutorService executor = Executors.newFixedThreadPool(5);

      executor.submit(()->{

          System.out.println("start "+Thread.currentThread().getId()+" 5");

          ExternalTaskClient client1 = ExternalTaskClient.create()
                  .baseUrl("http://localhost:8080/engine-rest/")
                  .asyncResponseTimeout(10000)
                  .disableBackoffStrategy()
                  .disableAutoFetching()
                  .maxTasks(1)
                  .build();
          client1.start();
          client1
                  .subscribe("iteration")
                  .lockDuration(20000)
                  .handler((externalTask, externalTaskService) -> {

                      externalTaskService.extendLock(externalTask,20000);
                      String colvar = externalTask.getVariable("colvar");
                      System.out.println(Thread.currentThread().getId()+" start:"+colvar+" "+ LocalTime.now());
                      try {
                          Thread.sleep(5000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      System.out.println(Thread.currentThread().getName()+" finish:"+colvar);

                      externalTaskService.complete(externalTask);
                  }).open();

      });


      executor.submit(()->{
          System.out.println("start "+Thread.currentThread().getId()+" 5");

          ExternalTaskClient client2 = ExternalTaskClient.create()
                  .baseUrl("http://localhost:8080/engine-rest/")
                  .asyncResponseTimeout(10000)
                  .disableBackoffStrategy()
                  .disableAutoFetching()
                  .maxTasks(1)
                  .build();
          client2.start();
          client2
                  .subscribe("iteration")
                  .lockDuration(20000)
                  .handler((externalTask, externalTaskService) -> {

                      externalTaskService.extendLock(externalTask,20000);
                      String colvar = externalTask.getVariable("colvar");
                      System.out.println(Thread.currentThread().getId()+" start:"+colvar+" "+ LocalTime.now());
                      try {
                          Thread.sleep(5000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      System.out.println(Thread.currentThread().getName()+" finish:"+colvar);

                      externalTaskService.complete(externalTask);
                  }).open();

      });


      executor.submit(()->{
          System.out.println("start "+Thread.currentThread().getId()+" 5");

          ExternalTaskClient client3 = ExternalTaskClient.create()
                  .baseUrl("http://localhost:8080/engine-rest/")
                  .asyncResponseTimeout(10000)
                  .disableBackoffStrategy()
                  .disableAutoFetching()
                  .maxTasks(1)
                  .build();
          client3.start();
          client3
                  .subscribe("iteration")
                  .lockDuration(20000)
                  .handler((externalTask, externalTaskService) -> {

                      externalTaskService.extendLock(externalTask,20000);
                      String colvar = externalTask.getVariable("colvar");
                      System.out.println(Thread.currentThread().getId()+" start:"+colvar+" "+ LocalTime.now());
                      try {
                          Thread.sleep(5000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      System.out.println(Thread.currentThread().getName()+" finish:"+colvar);

                      externalTaskService.complete(externalTask);
                  }).open();



      });
  }


}
