package ordermodel;


public class Order {
    private String name;
    private Order order;
    private boolean success;

    public Order(String name, Order order, boolean success) {
        this.name = name;
        this.order = order;
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }



}
