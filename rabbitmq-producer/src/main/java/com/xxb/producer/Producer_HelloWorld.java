package com.xxb.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * 发送消息
 */
public class Producer_HelloWorld {
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
        channel.queueDeclare("hello_world",true,false,false,null);
        //6.发送消息
        /*
        basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
        参数：
            1、exchange:交换机名称，简单模式交换机使用默认
            2、routingKey:路由名称
            3、props:配置信息
            4、body:发送消息数据
         */
        String body="hello rabbitmq";
        channel.basicPublish("","hello_world",null,body.getBytes());
        //7.释放资源
        channel.close();
        connection.close();

    }




}
