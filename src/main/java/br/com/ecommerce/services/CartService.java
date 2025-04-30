package br.com.ecommerce.services;

import br.com.ecommerce.models.CartItem;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String CART_PREFIX = "cart:";

    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getCartKey(Long customerId) {
        return CART_PREFIX + customerId;
    }

    public void addItem(Long customerId, CartItem item) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();

        String productIdKey = String.valueOf(item.getProductId());
        CartItem existing = (CartItem) ops.get(getCartKey(customerId), productIdKey);

        if (existing != null) {
            item.setQuantity(existing.getQuantity() + item.getQuantity());
        }

        ops.put(getCartKey(customerId), productIdKey, item);
    }

    public Map<String, CartItem> getCart(Long customerId) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        Map<Object, Object> entries = ops.entries(getCartKey(customerId));

        Map<String, CartItem> cart = new HashMap<>();
        entries.forEach((k, v) -> cart.put((String) k, (CartItem) v));
        return cart;
    }

    public void updateItemQuantity(Long customerId, Long productId, int quantity) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        String productIdKey = String.valueOf(productId);

        CartItem item = (CartItem) ops.get(getCartKey(customerId), productIdKey);
        if (item != null) {
            item.setQuantity(quantity);
            ops.put(getCartKey(customerId), productIdKey, item);
        }
    }

    public void clearCart(Long customerId) {
        redisTemplate.delete(getCartKey(customerId));
    }

    public void removeItem(Long customerId, Long productId) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        ops.delete(getCartKey(customerId), String.valueOf(productId));
    }
    
}
