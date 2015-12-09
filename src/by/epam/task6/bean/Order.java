package by.epam.task6.bean;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idOrder;
    private int idCustomer;
    private double totalCost;
    private Date date;
    private boolean isConfirmed;

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (idOrder != order.idOrder) return false;
        if (idCustomer != order.idCustomer) return false;
        if (Double.compare(order.totalCost, totalCost) != 0) return false;
        if (isConfirmed != order.isConfirmed) return false;
        return !(date != null ? !date.equals(order.date) : order.date != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idOrder;
        result = 31 * result + idCustomer;
        temp = Double.doubleToLongBits(totalCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (isConfirmed ? 1 : 0);
        return result;
    }
}
