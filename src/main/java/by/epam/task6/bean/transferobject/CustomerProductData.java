package by.epam.task6.bean.transferobject;

import java.io.Serializable;

public class CustomerProductData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCustomer;
    private int idProduct;

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
}
