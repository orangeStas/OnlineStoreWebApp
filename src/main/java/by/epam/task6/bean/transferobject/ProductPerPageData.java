package by.epam.task6.bean.transferobject;

import by.epam.task6.bean.Product;

import java.io.Serializable;
import java.util.List;

public class ProductPerPageData implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Product> products;
    private int countAllProducts;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCountAllProducts() {
        return countAllProducts;
    }

    public void setCountAllProducts(int countAllProducts) {
        this.countAllProducts = countAllProducts;
    }
}
