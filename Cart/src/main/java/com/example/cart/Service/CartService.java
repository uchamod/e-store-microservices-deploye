package com.example.cart.Service;

import com.example.cart.DTO.CountUpdater;
import com.example.cart.DTO.ProductData;
import com.example.cart.Feign.Feign_Client;
import com.example.cart.Model.Cart;
import com.example.cart.Model.CartProduct;
import com.example.cart.Model.ProductResponse;
import com.example.cart.Model.ProductWrapper;
import com.example.cart.Repo.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final Feign_Client feignClient;

    //add item to cart
    public ResponseEntity<String> addToCart(UUID userId, UUID productId) {
        try{
            if(userId==null || productId == null){
                System.out.println("Empty Credentials");
                return ResponseEntity.badRequest().build();
            }

            //get product price
            ResponseEntity<ProductData> productdata=  feignClient.getTotalAmount(productId);

            //check if cart already exist
            Cart existingCart= cartRepo.findCartByCustomerId(userId);
            //update product item count
            feignClient.updateAvailableCount(new CountUpdater(productId,1,false));
            //for new cart
            if(existingCart == null){
                List<CartProduct> cartProduct = new ArrayList<>(Arrays.asList(
                        new CartProduct(productId,productdata.getBody().getSellerId(),
                                1,productdata.getBody().getProductPrice(),
                                false)
                ));
                Cart cart=new Cart();
                cart.setCustomerId(userId);
                cart.setCartProductList(cartProduct);
                cart.setTotalAmount(productdata.getBody().getProductPrice());
                cartRepo.save(cart);
                return ResponseEntity.ok("product is add to cart successfully");
            }

            existingCart.getCartProductList().add( new CartProduct(productId,productdata.getBody().getSellerId(),
                    1,productdata.getBody().getProductPrice(),
                    false));
            //update cart total
            existingCart.setTotalAmount(existingCart.getTotalAmount()+productdata.getBody().getProductPrice());

            cartRepo.save(existingCart);
            return ResponseEntity.ok("product is add to cart successfully");
        }catch (Exception e){
            System.out.println("internal server error : "+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
   //update cart item(increse item count)
    public ResponseEntity<List<CartProduct>> updateCartItem(UUID uuid, UUID productId,Integer count) {
        try{
            if(uuid == null || productId == null){
                return ResponseEntity.badRequest().build();
            }

            Cart cart= cartRepo.findCartByCustomerId(uuid);
            if(cart == null || cart.getCartProductList().isEmpty()){
                return ResponseEntity.notFound().build();
            }
            ResponseEntity<ProductData> total=  feignClient.getTotalAmount(productId);

            for(CartProduct cartProduct : cart.getCartProductList()){
                if(cartProduct.getProductId().equals(productId)){
                    Integer existingCount=cartProduct.getProductCount();
                    Double finalAmount= (cart.getTotalAmount()-existingCount*total.getBody().getProductPrice())+count*total.getBody().getProductPrice();
                    cart.setTotalAmount(finalAmount);
                    Integer finalCount=existingCount-count;
                    feignClient.updateAvailableCount(new CountUpdater(productId,Math.abs(finalCount),existingCount > count));
                    cartProduct.setProductCount(count);
                    cartRepo.save(cart);
                    return ResponseEntity.ok(cart.getCartProductList());
                }
            }

            return ResponseEntity.notFound().build();


        }catch (Exception e){
            System.out.println("internal server error"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
//delete cart item
    public ResponseEntity<String> deleteCartItem(UUID uuid, UUID productId) {
        try{
            if(uuid == null  || productId == null){
                return ResponseEntity.badRequest().build();
            }
           Cart cart= cartRepo.findCartByCustomerId(uuid);
            if(cart == null || cart.getCartProductList().isEmpty()){
                return ResponseEntity.notFound().build();
            }
            ResponseEntity<ProductData> total=  feignClient.getTotalAmount(productId);

            CartProduct cartProduct= cart.getCartProductList().remove(productId.compareTo(productId));
            feignClient.updateAvailableCount(new CountUpdater(productId,cartProduct.getProductCount(),true));
              cart.setTotalAmount(cart.getTotalAmount()-(total.getBody().getProductPrice()*cartProduct.getProductCount()));
            cartRepo.save(cart);

            return ResponseEntity.ok("product is deleted successfully");
        }catch (Exception e){
            System.out.println("internal server error"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
//get all user product from cart
    public ResponseEntity<ProductResponse> getProductFromCart(UUID uuid) {
        try{
            if(uuid == null){
                return ResponseEntity.badRequest().build();
            }
           Cart cart= cartRepo.findCartByCustomerId(uuid);
            if(cart == null){
                return ResponseEntity.notFound().build();
            }
            List<ProductWrapper> productWrappers=new ArrayList<>();
            ProductWrapper productWrapper=new ProductWrapper();
            for(CartProduct cartProduct : cart.getCartProductList()){
                 productWrapper= feignClient.getProductById(cartProduct.getProductId()).getBody();
                 productWrapper.setPurchaseCount(cartProduct.getProductCount());
                 productWrappers.add(productWrapper);
            }
            ProductResponse productResponse=new ProductResponse(cart.getTotalAmount(),productWrappers);
            return ResponseEntity.ok(productResponse);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
//check out the cart
    public ResponseEntity<String> toggleCheckOut(UUID uuid, List<UUID> productIds) {
       try{
           if(uuid == null || productIds.isEmpty()){
               return ResponseEntity.badRequest().build();
           }
           Cart cart=cartRepo.findCartByCustomerId(uuid);
           if(cart == null || cart.getCartProductList().isEmpty()){
               return ResponseEntity.notFound().build();
           }
           cart.getCartProductList().forEach(((p)->p.setIsCheckout(true)));
           cartRepo.save(cart);
           return ResponseEntity.ok("checkout the items");
       }catch (Exception e){
           return ResponseEntity.internalServerError().build();
       }
    }
//get total amount of items
    public ResponseEntity<Double> getTotal(UUID uuid) {
       try{
           if(uuid == null){
               return ResponseEntity.badRequest().build();
           }
           Cart cart=cartRepo.findCartByCustomerId(uuid);
           if(cart == null || cart.getCartProductList().isEmpty()){
               return ResponseEntity.ok(0.0);
           }

           return ResponseEntity.ok(cart.getTotalAmount());
       }catch (Exception e){
           return ResponseEntity.internalServerError().build();
       }
    }
//remove user checkout cart
    public ResponseEntity<String> checkoutFromCart(UUID userId) {
        try{
            if(userId==null){
                return ResponseEntity.badRequest().build();
            }
               Cart cart= cartRepo.deleteByCustomerId(userId);
            if(cart.getCartProductList().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user product list is empty");
            }
            return ResponseEntity.status(HttpStatus.OK).body("user product list is removed succsussfuly");
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    //get cart by userid
    public ResponseEntity<Cart> getCartByUserId(UUID userId){
        try{
            if(userId==null){
                return ResponseEntity.badRequest().build();
            }
            Cart userCart=cartRepo.findCartByCustomerId(userId);
            System.out.println(userCart);
            if(userCart.getCartProductList().isEmpty()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userCart);
        }catch (Exception e){
            System.out.println("error while creating order"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
