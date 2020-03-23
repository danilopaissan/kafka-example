package net.differentplace.java.jugkafka.clazz;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemForSale {
    private List<String> categories;
    private Boolean limitedOffer;
    private Date validUntil;
    private Boolean discounted;
    private Integer discount;
    private String  description;
    private String name;
    private Integer price;

    public ItemForSale() {
    }

    public ItemForSale(String name, List<String> categories, Boolean limitedOffer, Integer offsetValidity, Boolean discounted, Integer discount, String description, Integer price) {
        this.categories = categories;
        this.limitedOffer = limitedOffer;
        this.discounted = discounted;
        this.discount = discount;
        this.name = name;

        Calendar calendarValidUntil = Calendar.getInstance();
        calendarValidUntil.add(Calendar.DAY_OF_MONTH,offsetValidity);

        this.validUntil = calendarValidUntil.getTime();
        this.description = description;
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getLimitedOffer() {
        return limitedOffer;
    }

    public void setLimitedOffer(Boolean limitedOffer) {
        this.limitedOffer = limitedOffer;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Boolean discounted) {
        this.discounted = discounted;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
