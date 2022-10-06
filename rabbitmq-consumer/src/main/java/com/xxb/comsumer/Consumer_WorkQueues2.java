package com.xxb.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_WorkQueues2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory=new ConnectionFactory();
        //2.设置参数
        factory.setHost("114.132.246.79");
        factory.setPort(5672);
        factory.setVirtualHost("/itcast");//虚拟机 默认值为 '/'
        factory.setUsername("guest");
        factory.setPassword("guest");
        //3.创建连接
        Connection connection=factory.newConnection();
        //4.创建channel
        Channel channel=connection.createChannel();
        //5.创建队列queue
        /*
        queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        实际参数解释：
            1、queue:队列名称
            2、durable:是否持久化,当mq重启后还在
            3、exclusive:
                * 是否独占，只能有一个消费者监听这个队列
                * 当Connection关闭时，是否删除队列
            4、autoDelete:是否自动删除，没有consumer时自动删除
            5、arguments
        没有这个队列会自动创建
         */
        channel.queueDeclare("work_queues",true,false,false,null);

        /*接收消息
        basicConsume(String queue, boolean autoAck, Consumer callback)
        参数
            1、queue:队列名称
            2、autoAck:是否自动确认
            3、callback:回调对象
         */
        Consumer consumer=new DefaultConsumer(channel){
            /*
            回调方法，收到消息会自动执行该方法
            参数：
                1、consumerTag:标识
                2、envelope:获取一些信息，交换机，路由key
                3、properties:配置信息
                3、body:数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("queue2body:"+new String(body));
            }
        };

        channel.basicConsume("work_queues",true,consumer);

        //7.消费端不用去释放资源，释放了还怎么起到监听作用
    }
}
