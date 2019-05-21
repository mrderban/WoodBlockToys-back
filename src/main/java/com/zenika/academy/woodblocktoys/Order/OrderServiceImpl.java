package com.zenika.academy.woodblocktoys.Order;


import com.zenika.academy.woodblocktoys.Account.Account;
import com.zenika.academy.woodblocktoys.Account.AccountService;
import com.zenika.academy.woodblocktoys.Barrel.Barrel;
import com.zenika.academy.woodblocktoys.Barrel.BarrelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zenika.academy.woodblocktoys.Order.OrderState.WAITING_FOR_VALIDATION;


@Service
public class OrderServiceImpl implements OrderService {

    /************************VARIABLES & CONSTRUCTOR************************/
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final BarrelRepository barrelRepository;

    public OrderServiceImpl(OrderRepository orderRepository, BarrelRepository barrelRepository, AccountService accountService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
        this.barrelRepository = barrelRepository;
    }

    /************************DTO METHODS************************/
    @Override
    public OrderDto mapOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .createdAt(order.getCreatedAt())
                .price(order.getPrice())
                .orderState(order.getOrderState())
                .accountMail(order.getAccount().getMail())
                .barrelId(order.getBarrel().getId())
                .build();
    }

    @Override
    public Order mapOrderDtoToOrder(OrderDto orderDto) {
        return Order.builder()
                .price(orderDto.getPrice())
                .createdAt(orderDto.getCreatedAt())
                .orderState(orderDto.getOrderState())
                .account(
                        Account.builder().mail(orderDto.getAccountMail()).build())
                .barrel(
                        Barrel.builder().id(orderDto.getBarrelId()).build())
                .build();
    }


    @Override
    public List<OrderDto> listMapperOrderToOrderDto(List<Order> orderList) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderList.forEach((order -> orderDtoList.add(mapOrderToOrderDto(order))));
        return orderDtoList;
    }


    /************************OTHER METHODS************************/
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    @Override
    public List<Order> getOrdersByAccountMail(String mail) {
        return orderRepository.findByAccountMail(mail);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order saveOrUpdateOrder(String accountMail, Long barrelId) {
        Optional<Account> retrievedAccount = accountService.getAccountByMail(accountMail);
        Optional<Barrel> retrievedBarrel = barrelRepository.findById(barrelId);

        if (retrievedBarrel.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve barrel");
        } else if (retrievedAccount.isEmpty()) {
            throw new IllegalArgumentException("Cannot retrieve account");
        }
        //else build the order with retrieved objects
        else {
            Order order = Order.builder()
                    .price(retrievedBarrel.get().getPrice())
                    .barrel(retrievedBarrel.get())
                    .account(retrievedAccount.get())
                    .createdAt(LocalDate.now())
                    .orderState(WAITING_FOR_VALIDATION)
                    .build();
            return orderRepository.save(order);
        }
    }
}
