# 如何使用
##1.在项目里引入下面依赖
<!-- 日志收集支持 -->
    <dependency>
        <groupId>com.ssaw</groupId>
        <artifactId>support-log-collect</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
		
##2.在spring-boot启动类上加上@EnableLogCollect注解
    @EnableLogCollect
    public class AuthenticateCenterServiceApplication......
        
##3.在application.yml中加入下面配置
    ssaw:
      log:
        kafka:
          producer:
            app-name: ${spring.application.name}
    
    spring:
      kafka:
        bootstrap-servers: 118.24.38.46:9092
        consumer:
          auto-offset-reset: earliest
          key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        producer:
          retries: 3
          acks: all
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
          batch-size: 65536
          buffer-memory: 524288
          bootstrap-servers: 118.24.38.46:9092     
          
##4.实现LogHandler接口中的3个方法
    @Component
    public class ClientLogHandler implements LogHandler {
    
        @Override
        public String formatLog(Log log) {
            return JsonUtils.object2JsonString(log);
        }
    
        @Override
        public Log setLogBaseInfo(String logType, Object o) {
            Log log = new Log();
            log.setType(logType);
            log.setLog(o);
            return log;
        }
    
        @Override
        public Log setLogBaseInfo(String logType, Object o, String message) {
            Log log = new Log();
            log.setType(logType);
            log.setLog(o);
            log.setMessage(message);
            return log;
        }
    }
    
##5.在需要进行日志收集的类中注入此ClientLogHandler并调用Log方法
    clientLogHandler.log(entity, "新增客户端", "新增成功");  
    
##6.需要进行日志收集还需要在logstash.conf中进行如下配置
    input {
      kafka{
    	bootstrap_servers => ["118.24.38.46:9092"]
    	client_id => "test"
    	group_id => "logstash-es-test"
    	auto_offset_reset => "latest" 
    	consumer_threads => 5
    	decorate_events => "true"
    	topics => ["ssaw.log.kafka.producer.app-name"]
      }
    } 
    
    filter {
       if [@metadata][kafka][topic] == "ssaw.log.kafka.producer.app-name" {
          mutate {
             add_field => {"[@metadata][index]" => "ssaw.log.kafka.producer.app-name"}
          }
       }
       # remove the field containing the decorations, unless you want them to land into ES
       mutate {
          remove_field => ["kafka"]
       }
    }
    
    output {
      elasticsearch {
        hosts => ["http://localhost:9200"]
        index => "%{[@metadata][index]}"
        timeout => 300
      }
    }
            