package com.marketplace.service.order;

import com.marketplace.repository.category.ProductQuery;
import com.marketplace.repository.order.OrderQuery;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    public void addOrder(String username);

    public List<OrderedProduct> getCurrentOrder(String username);

    public Map<LocalDate, List<OrderedProduct>> getAllOrders(String username);

    public void addProductToOrder(OrderQuery orderQuery);

    public void deleteProductFromOrder(OrderQuery orderQuery);

    public void changeProductAmount(OrderQuery orderQuery);
}
