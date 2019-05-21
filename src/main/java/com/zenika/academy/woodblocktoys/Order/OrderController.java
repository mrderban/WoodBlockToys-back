package com.zenika.academy.woodblocktoys.Order;


import com.zenika.academy.woodblocktoys.Account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(path = "/orders")
public class OrderController {


    /************************VARIABLES & CONSTRUCTOR************************/
    private final OrderService orderService;

    //constructor w/ dependency injection
    public OrderController(OrderService orderService, AccountService accountService) {
        this.orderService = orderService;
    }


    /************************POST & DEL METHODS************************/
    @PostMapping(path = "/{mail}/buy/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderDto> createOrder(@PathVariable String mail, @PathVariable long id) {
        //TODO implement barrel repository
        log.info("{mail} trying to order barrel with id {id}" + mail + id);
        try {
            Order order = orderService.saveOrUpdateOrder(mail, id);
            //convert outcoming (from repository) Order response to OrderDto then send it
            OrderDto resultOrderDto = orderService.mapOrderToOrderDto(order);
            return ResponseEntity.ok(resultOrderDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") long id) {
        log.info("Trying to delete order with id: {}", id);
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isEmpty()) {
            log.info("Unable to delete order data with id: {}, order not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderService.deleteOrder(id);
        return new ResponseEntity<>("Order has been deleted!", HttpStatus.OK);
    }


    /************************GET METHODS************************/
    @GetMapping(path = "/mail/{mail}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderDto>> findOrdersByAccountMail(@PathVariable String mail) {
        log.info("Trying to fetch orders data with mail: {}", mail);
        List<Order> orderList = orderService.getOrdersByAccountMail(mail);

        if (orderList.isEmpty()) {
            log.info("Unable to fetch orders data with mail: {}", mail);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No orders found !");
        } else {
            //convert outcoming (from repository) list Order response to list OrderDto then send it
            List<OrderDto> resultOrderDtoList = orderService.listMapperOrderToOrderDto(orderList);
            return ResponseEntity.ok(resultOrderDtoList);
        }
    }

    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("Trying to fetch orders data");
        List<Order> orderList = orderService.getAllOrders();
        if (orderList.isEmpty()) {
            log.info("Unable to fetch any order data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No orders in database");
        } else {
            //convert outcoming (from repository) list Order response to list OrderDto then send it
            List<OrderDto> resultOrderDtoList = orderService.listMapperOrderToOrderDto(orderList);
            return ResponseEntity.ok(resultOrderDtoList);
        }
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderDto> findOrderById(@PathVariable long id) {
        log.info("Trying to fetch order data with id: {}", id);
        Optional<Order> optionalOrder = orderService.getOrderById(id);

        if (optionalOrder.isEmpty()) {
            log.info("Unable to fetch order data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        } else {
            //convert outcoming (from repository) Order response to OrderDto then send it
            OrderDto resultOrderDto = orderService.mapOrderToOrderDto(optionalOrder.get());
            return ResponseEntity.ok(resultOrderDto);
        }
    }
}
