package com.zenika.academy.woodblocktoys.Order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    Optional<Order> getOrderById(Long id);

    Order mapOrderDtoToOrder(OrderDto orderDto);

    List<OrderDto> listMapperOrderToOrderDto(List<Order> orderList);

    List<Order> getAllOrders();


    List<Order> getOrdersByAccountMail(String mail);

    void deleteOrder(Long id);

    OrderDto mapOrderToOrderDto(Order order);

    @Transactional
    Order saveOrUpdateOrder(String accountMail, Long barrelId);
}
