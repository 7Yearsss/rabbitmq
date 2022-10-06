package com.xxb.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_PubSub1 {
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
        String queue1Name="test_fanout_queue1";
        String queue2Name="test_fanout_queue2";
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
                System.out.println("queue1body:"+new String(body));
                System.out.println("消费者1");
            }
        };

        channel.basicConsume(queue1Name,true,consumer);

        //7.消费端不用去释放资源，释放了还怎么起到监听作用
    }
}
