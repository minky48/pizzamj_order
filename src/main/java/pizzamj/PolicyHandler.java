package pizzamj;

import pizzamj.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCanceled_CancelPolicy(@Payload PaymentCanceled paymentCanceled){

        if(paymentCanceled.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(paymentCanceled.getOrderId());
            Order order = orderOptional.get();
            order.setOrderStatus(paymentCanceled.getPaymentStatus());
            orderRepository.save(order);

            System.out.println("##### listener CancelPolicy : " + paymentCanceled.toJson());
        }
    }

}
