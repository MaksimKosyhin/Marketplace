package com.marketplace.repository.order;

import com.marketplace.service.order.OrderQuery;

import java.util.List;

public interface OrderRepository {
    public long addOrder(long userId);

    public List<OrderedProduct> getOrder(long orderId);

    public List<OrderedProduct> getAllOrders(long userId);

    public boolean addProductToOrder(OrderQuery orderQuery);

    public boolean deleteProductFromOrder(OrderQuery orderQuery);

    public boolean changeProductAmount(OrderQuery orderQuery);

    public long getCurrentOrderId(String username);

    public long getUserId(String username);
}
