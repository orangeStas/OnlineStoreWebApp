package by.epam.task6.bean.transferobject;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;

import java.io.Serializable;
import java.util.List;

public class OrderData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Order order;
    private List<Product> products;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
