package by.epam.task6.bean.transferobject;

import by.epam.task6.bean.Order;

import java.io.Serializable;
import java.util.List;

public class UnpaidOrdersData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Order> unpaidOrders;

    public List<Order> getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(List<Order> unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }
}
