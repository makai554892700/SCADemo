# SpringCloudAlibaba 学习项目( SpringCloudAlibaba + dubbo + Nacos + Sentinel + ribbon + openfeign + gateway + Seata)
## 本文章仅作为个人笔记
* [nacos](http://nacos.io) 接入 gateway  [参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-gateway-example/readme-zh.md)
* nacos 的服务安装(这里都仅处理单机模式)
  * 官网下载运行 startup.cmd (与docker方案二选一)
  * docker安装运行 (与直接下载方案二选一)

        docker run --network=mys --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server

  * 访问 localhost:8848/nacos 即可查看是否ok
    * 默认用户密码都是 nacos
* nacos (代替注册中心) 的集成 [注册中心参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md)
  * 服务端 
    * 为项目的 build.gradle 文件中添加相应依赖

          ext {
            set('springCloudAlibabaVersion', "2.2.1.RELEASE")
          }
        
          dependencies {
            classpath("com.alibaba.cloud:spring-cloud-alibaba-dependencies:${springCloudAlibabaVersion}")
          }
        
          dependencyManagement {
            imports {
                mavenBom "com.alibaba.cloud:spring-cloud-alibaba-dependencies:${springCloudAlibabaVersion}"
            }
          }

    * 为项目的 application.yml 添加相应配置

          spring:
             application:
              name: nacos-provider
            cloud:
              nacos:
                discovery:
                  server-addr: 192.168.3.6:8848

    * 在服务提供者项目的Application主类添加注解 @EnableDiscoveryClient 以使配置文件生效并注册服务/表明以可以使用nacos服务

          @EnableDiscoveryClient
          public class CloudProviderApplication {

    * 此时再启动服务提供者项目，服务便注册至nacos注册中心了
  * 客户端
    * 为项目的 build.gradle 文件中添加相应依赖

          dependencies {
            implementation 'com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config'
          }

    * 为项目的 application.yml 添加相应配置

          spring:
            application:
              name: nacos-consumer
            cloud:
              nacos:
                discovery:
                  server-addr: 192.168.3.6:8848

    * 再在消费者RestTemplate bean注入的地方加入 @LoadBalanced 注解加入负载均衡功能

          @Bean
          @LoadBalanced
          public RestTemplate restTemplate() {

    * 此时只需要将服务消费者的restTemplate.getForObject方法里的host改为服务提供者名称即可(例：<restTemplate.getForObject("http://nacos-provider/echo/<app-name>"...>)

          @RestController
          public class NacosController {
        
            @Autowired
            private RestTemplate restTemplate;
        
            @Value("${spring.application.name}")
            private String appName;
        
            @GetMapping("/echo/app-name")
            public String echoAppName() {
                return restTemplate.getForObject("http://nacos-provider/echo/" + appName, String.class);
            }
        
          }

* nacos (代替config) 的集成配置中心 [参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/readme-zh.md)
  * 服务端 (无服务端概念，即为nacos服务本身，直接新建配置即可)
  * 客户端
    * 为项目的 build.gradle 文件中添加相应依赖

          dependencies {
            implementation 'com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config'
          }

    * 为项目的 bootstrap.yml 添加相应配置(旧配置全迁移至bootstrap.yml)

          spring:
            application:
              name: nacos-consumer
            cloud:
              nacos:
                discovery:
                  server-addr: 192.168.3.6:8848
                config:
                  server-addr: 192.168.3.6:8848
                  file-extension: yaml
                  group: DEFAULT_GROUP
                  namespace: public

    * 为项目的 application.yml 添加相应配置(指定激活环境)

          spring:
            profiles:
              active: dev

    * 之所以需要单独配置 application.yml 是因为nacos配置严格按照这个配置来
    * dataId 完整格式：
            
            ${prefix}-${spring.profile.active}.${file-extension}

    * 使用时直接用注解 @Value("${<key>}") 即可。使用 @RefreshScop 则可以保证配置的实时同步。例：

          @RestController
          @RefreshScope
          public class ConfigClientController {
        
            @Value("${config.info")
            private String configInfo;
        
            @RequestMapping("/config/info")
            public String getConfigInfo() {
                return configInfo;
            }
        
          }
        
  * 测试：
    * 访问 nacos 本地路径，如： http://192.168.3.6:8848/nacos/
    * 在控制面版选择配置列表
    * 添加一个自定义的分组
    * 设置配置内容，例：
            
            dataId: nacos-consumer-dev.yaml
            Group: DEFAULT_GROUP
            配置内容： config: info: nacos config center,versioin = 1

    * 启动客户端服务，再访问对应接口即可获取配置的内容，更新也可即时获取更新了。
  * nacos 集群部署与配置
  * 使用nginx对多个nacos进行负载均衡
  * 多个nacos统一配置高可用mysql集群
    * 运行 conf/nacos-mysql.sql
    * 修改 conf/application/properties 文件
    
            spring.datasource.platform=mysql
            db.num=1
            db.url.0=jdbc:mysql://192.168.1.101:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&autoReconnect=true
            db.user=root
            db.passowrd=root
    
    * 修改 cluster.conf 填写集群机器ip与端口
    * 重启nacos

* Sentinel 流量防卫兵(服务降级/熔断(Hystrix)) 集成 [参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/sentinel-example/sentinel-core-example/readme-zh.md)
  * 服务端搭建
    * jar包运行
    * 去[官网](https://sentinelguard.io)下载jar包运行
        
          java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=0.0.0.0:8858 -jar sentinel-dashboard.jar

    * docker  运行

          docker run --network=mys --name sentinel -p 8719:8719 -p 8858:8858 -d bladex/sentinel-dashboard

   * 打开页面 http://localhost:8858 并登录(用户名密码均为 sentinel)
  * 客户端使用
  * 为项目的 build.gradle 文件中添加相应依赖

        dependencies {
            implementation 'com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel'
        }

  * 为项目的 application.yml 添加相应配置

        spring:
          cloud:
            sentinel:
              transport:
                dashboard: 127.0.0.1:8858
                port: 8719
     
  * 此时启动项目，项目就已经被监控了
  * 控制面板配置说明
    * 资源名： 唯一名称，默认请求路径
    * 针对来源： Sentinel可以针对调用者进行限流，填写微服务名，默认default（不区分来源）
    * 阈值类型/单机阈值：
      * QPS（每秒的请求数量）： 当调用该api的QPS达到阈值的时候，进行限流
      *   线程数： 当调用该api的线程数达到阈值的时候，进行限流 
    * 是否集群： 不需要集群
    * 流控模式
        * 直接： api达到限流条件时，直接限流
        * 关联： 当关联的资源达到阈值时，就限流自己
        * 链路： 只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就进行限流）[api级别针对来源]        
    * 流控效果：
        * 快速失败： 直接失败，抛异常
        * Warm Up： 根据codeFactor（冷加载因子，默认3）的值，从阈值/codeFactor，经过预热时长，才达到预置的QPS阈值
        * 排队等待： 匀速排队，让请求以匀速的速度通过，阈值类型必须设置QPS，否则无效
    * 错误/异常处理：
        * @SentinelResource(value = "hello", fallback = "common", fallbackClass = Fallback.class, blockHandler = "common", blockHandlerClass = BlockHandler.class)
            * value 值为sentinel管理页面名称
            * fallbackClass 为 异常处理类，不写时默认为当前类
            * fallback 为 异常处理方法，不写时使用默认方案，书写时在 fallbackClass 内找对应方法处理 
            * blockHandlerClass 为 限流时处理类，不写时默认为当前类 
            * blockHandler 为 限流处理方法，不写时使用默认方案，书写时在 blockHandlerClass 内找对应方法处理
    
         -Djava.net.preferIPv4Stack=true -Dsentinel.dashboard.auth.username=sentinel -Dsentinel.dashboard.auth.password=sentinel -Dcsp.sentinel.api.port=8720 -Dcsp.sentinel.dashboard.server=192.168.3.6:8858 -Dproject.name=shop-server
        -Djava.net.preferIPv4Stack=true -Dsentinel.dashboard.auth.username=sentinel -Dsentinel.dashboard.auth.password=sentinel -Dcsp.sentinel.api.port=8720 -Dcsp.sentinel.dashboard.server=192.168.3.6:8858 -Dproject.name=order-server
    
* sentinel 与 ribbon 的集成
  * 与上面的教程一样
  * 在消费者RestTemplate bean注入的地方加入 @LoadBalanced 注解

        @Bean
        @LoadBalanced
        public RestTemplate restTemplate() {
        
  * sentinel 与 openfeign 的集成
    * 与前面的使用类似
    * 修改 bootstrap.yml
    
          feign:
            sentinel:
              enabled: true


* gateway 集成(api网关) [参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-gateway-example/readme-zh.md)
  * 基本概念 Predicate->Filter->Route
  * 为根项目 build.gradle 添加相应依赖

        ext {
            set('springCloudAlibabaVersion', "2.2.1.RELEASE")
        }
        
        dependencies {
            implementation("com.alibaba.cloud:spring-cloud-alibaba-sentinel-gateway")
        }
        
        dependencyManagement {
            imports {
                mavenBom "com.alibaba.cloud:spring-cloud-alibaba-dependencies:${springCloudAlibabaVersion}"
            }
        }
  * 为项目 build.gradle 添加相应依赖

        //gateway 依赖
        api("org.springframework.cloud:spring-cloud-starter-gateway:${springCloudVersion}")
        api("com.alibaba.cloud:spring-cloud-alibaba-sentinel-gateway")

  * 在项目的Application主类添加注解 @EnableDiscoveryClient 将服务注册至注册中心

        @EnableDiscoveryClient
        public class GatewayServerApplication {

  * 在项目的 application.yml 中配置相应的路由/服务/断言等(路由可多个)

        spring:
          cloud:
            gateway:
              discovery:
                locator:
                  enabled: true     # 开启从注册中心动态创建路由的功能，利用服务名进行路由
              routes:
                - id: cloud-provider            # 路由的id，没有固定规则但要求唯一，建议配合服务名
        #          uri: http://localhost:8080    # 匹配提供服务的服务根地址
                  uri: lb://cloud-provider    # 匹配后提供服务的路由地址
                  predicates:
                    - Path=/provider/**    # 断言，路径相匹配的进行路由

      * predicates这个配置除了Path还有其它参数
        * After 之前              - After=2020-05-23T15:15:21.234+08:00[Asia/Shanghai] # 指定时间之后可访问
        * Before 之前             - Before=2020-06-23T15:15:21.234+08:00[Asia/Shanghai] # 指定时间之前可以访问 
        * Between 之间            - Between=2020-06-23T15:15:21.234+08:00[Asia/Shanghai],2020-07-23T15:15:21.234+08:00[Asia/Shanghai] # 指定时间之间可以访问
        * Cookie 带Cookie        - Cookie=username,aabb # 带有Cookie且key为username，value为aabb
        * Header 带Header        - Header=X-Request-Id, \d+ # 含X-Request-Id的头且为数字
        * Method 指定请求方式         - Method=GET   # get请求
        * Path 路径匹配             - Path=/provider/** # 请求路径匹配 /provider 开头
        * Query 参数匹配            - Query=username, \d+ # 带username参数且参数值为数字
    * filter 官方有详细介绍
        * 局部过滤器
        * 全局过滤器
        * 自定义过滤器
            * GlobalFilter  全局过滤器
            * Ordered       权重
  * 此时再启动项目便配置好了相关网关服务了
  * 可以访问 http://\<host>:\<port>/<具体路径> 例: http://localhost:8087/provider/hello

* SpringCloud Stream 绑定器(兼容所有MQ)
* 常用注解
    * @Input 注解标识输入通道，通过该通道接收到的消息进入应用程序
    * @Output 注解标识输入通道，发布的消息将通过该通道离开应用程序
    * @StreamListener 监听队列，用于消费者队列的消息接收
    * @EnableBinding 指信道channel和exchange绑定在一起
    
* RocketMQ 集成 [参考](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/rocketmq-example/readme-zh.md)
  * RocketMQ 安装运行
#### jar运行
  * 下载jar包运行 https://github.com/apache/rocketmq/archive/rocketmq-all-4.7.1.zip

        bin/mqnamesrv
        bin/mqbroker -n localhost:9876

  * docker运行
    
        docker run --network=mys -p 9876:9876 --name rmqnamesrv -d rocketmqinc/rocketmq:4.4.0 sh mqnamesrv
        docker run --network=mys -p 10911:10911 -p 10912:10912 -p 10909:10909 --name rmqbroker -e "NAMESRV_ADDR=192.168.3.6:9876" -d rocketmqinc/rocketmq:4.4.0 sh mqbroker -c /opt/rocketmq-4.4.0/conf/broker.conf -n 192.168.3.6:9876 autoCreateTopicEnable=true
        docker cp rmqbroker:/opt/rocketmq-4.4.0/conf/broker.conf ./
        echo "brokerIP1=192.168.3.6" >> broker.conf
        docker cp broker.conf rmqbroker:/opt/rocketmq-4.4.0/conf/broker.conf
        docker restart rmqbroker
        docker run --network=mys -p 8680:8080 --name rmqconsole -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.3.6:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -d styletang/rocketmq-console-ng

   * 运行好访问 http://192.168.3.6:8680/可查看rocketmq控制台
  * 集成
    * build.gradle文件添加依赖

          dependencies {
            implementation 'com.alibaba.cloud:spring-cloud-starter-stream-rocketmq'
          }

    * application.yml 文档添加对应的配置

          stream:
            rocketmq:
              binder:
                name-server: ${properties.rocketmq-addr}:${properties.rocketmq-port}
                bindings:
              output:
                destination: shop-topic
                content-type: application/json
              input:
                destination: shop-topic
                content-type: application/json
                group: shop-group

    * 配置 Input 和 Output 的 Binding 信息并配合 @EnableBinding 注解使其生效

          @SpringBootApplication
          @EnableBinding({ Source.class, Sink.class })
          public class RocketMQApplication {
        
    * 消息发送
    
          public class ProducerRunner implements CommandLineRunner {
            @Autowired
            private MessageChannel output; // 获取name为output的binding
            @Override
            public void run(String... args) throws Exception {
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_TAGS, "tagStr");
                Message message = MessageBuilder.createMessage(msg, new MessageHeaders(headers));
                output.send(message);
            }
          }

    * 消息接收
    
          @StreamListener("input")
          public void receiveInput1(String receiveMsg) {
        	System.out.println("input receive: " + receiveMsg);
          }

* SpringCloud Sleuth 集成(分布式请求链路跟踪)
  * zipkin 安装运行
    * jar包运行
      * 下载jar包运行或集成 https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/
    * docker运行
        
          docker run -p 9411:9411 --name zipkin -d openzipkin/zipkin

    * 访问 http://localhost:9411/zipkin
  * 集成
  * 为需要参与监控的项目的build.gradle文件添加依赖

        dependencies {
            implementation 'org.springframework.cloud:spring-cloud-starter-zipkin'
        }

  * 为需要参与监控的项目的 application.yml 文档添加对应的配置

        spring:
          application:
            name: cloud-provider
          zipkin:
            base-url: http://localhost:9411
          sleuth:
            sampler:
              probability: 1

  * 此时已经做好了链路监控了，访问 http://localhost:9411/zipkin/ 即可查看所有的链路监控

* 全局事务 Seata [参考](https://github.com/seata/seata-samples/blob/master/doc/quick-integration-with-spring-cloud.md)
  * seata 安装
  * 去[官网](https://seata.io/) 下载jar包运行
  * docker安装运行
        
        docker run --network=mys -p 8091:8091 --name seata -d seataio/seata-server

  * seata 服务端配置(使用mysql进行永久配置才需要修改配置，否则可直接使用)
    * 修改 conf 下的 file.conf 配置文件/service部分必须修改
        * 安装包直接修改
        * dock使用命令进入docker修改
        * 修改核心：
            
              service {
                vgroup_mapping.my_test_tx_grop='<自定义名称_tx_group>'
              }
              store {
                mode = "db"
              }
              db {
                url = "<数据库地址>"
                user = "<数据库用户名>"
                password = "<数据库密码>"
              }

    * 修改 conf 下的 registry.conf 配置文件
        * 修改核心：
    
              registry {
                type = "nacos"
                nacos {
                    serverAddr = "<nacos地址 host:port>"
                }
              }

     * 导入mysql文件 conf/db_s.sql (最新版本下载时无此文件，可下载[0.9](https://github.com/seata/seata/releases/download/v0.9.0/seata-server-0.9.0.zip)
    * 代码集成
      * 为项目的 build.gradle 文件中添加相应依赖

            dependencies {
              implementation 'com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel'
            }

* 本人创建一[github demo](https://github.com/makai554892700/SBJni)，有需求可自行查看借鉴。
