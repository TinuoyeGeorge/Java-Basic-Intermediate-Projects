package jd.com.util;

public interface DiscountStrategy {
    double applyDiscount(double originalPrice);
}

// Implementation examples in same file:
class CouponDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * 0.9; // 10% off
    }
}

class SeasonalDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * 0.8; // 20% off
    }
}