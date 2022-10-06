package com.xxb.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * 发送消息
 */
public class Producer_Routing {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置参数
        factory.setHost("主机ip");
        factory.setPort(5672);
        factory.setVirtualHost("/itcast");//虚拟机 默认值为 '/'
        factory.setUsername("guest");
        factory.setPassword("guest");
        //3.创建连接
        Connection connection = factory.newConnection();
        //4.创建channel
        Channel channel = connection.createChannel();
        /*5.创建交换机、
        exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
        参数：
            exchange:交换机名称
            type:交换机类型
                DIRECT("direct"),定向
                FANOUT("fanout"),扇形
                TOPIC("topic"),通配符形式
                HEADERS("headers");参数匹配
            durable:是否持久化
            autoDelete:自动删除
            internal:内部使用,一般为false
            arguments:参数
         */
        String exchangeName="test_direct";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,false,null);
        //6.创建队列
        String queue1Name="test_direct_queue1";
        String queue2Name="test_direct_queue2";

        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7.绑定队列和交换机
        /*
        queueBind(String queue, String exchange, String routingKey)
         */
        channel.queueBind(queue1Name,exchangeName,"error");
        //队列2绑定info error warnig 这些key
        channel.queueBind(queue2Name,exchangeName,"info");
        channel.queueBind(queue2Name,exchangeName,"error");
        channel.queueBind(queue2Name,exchangeName,"waring");
        //8.发送消息
        String routingKey="error";
        String body="日志信息:张三调用findAll方法...日志级别:"+routingKey;
        channel.basicPublish(exchangeName,routingKey,null,body.getBytes());

        //9.释放消息
        channel.close();
        connection.close();
    }


}
