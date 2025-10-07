package com.uchamod.estore.Controller;


import com.uchamod.estore.DTO.ProductData;
import com.uchamod.estore.Model.CountUpdater;
import com.uchamod.estore.Model.Product;
import com.uchamod.estore.Service.Product_Service;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class Product_Controller {

   private final Product_Service productService;

   //get all products
   @GetMapping("/getAllProducts")
   public ResponseEntity<List<Product>> getAllProducts(){
       return productService.getAllProducts();
   }

   //get one by id
    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID productId){
          return productService.getProductById(productId);
    }

    //add products
    @PostMapping ("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody List<Product> products,@RequestHeader("X-User-Id") String sellerId){
       return productService.addProduct(products,UUID.fromString(sellerId));
    }

    //update product
    @PutMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@RequestBody Product product){
       return productService.updateProduct(product);
    }
    //delete product
    @DeleteMapping ("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId,@RequestHeader("X-User-Id") String sellerId){
      return   productService.deleteProduct(productId,UUID.fromString(sellerId));
    }
    //get product by category
    @GetMapping("/getProductByCategory/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category){
        return productService.getProductByCategory(category);
    }
    //get product by brand
    @GetMapping("/getProductByBrand/{brand}")
    public ResponseEntity<List<Product>> getProductByBrand(@PathVariable String brand){
        return productService.getProductByBrand(brand);
    }

    //add product with image
    @PostMapping("/addProductWithImage")
    public ResponseEntity<Product> addProductWithImage(@RequestPart Product product, @RequestPart MultipartFile imageFile,@RequestHeader("X-User-Id") String sellerId){
        return productService.addProductWithImage(product,imageFile,UUID.fromString(sellerId));
    }

    //get product image
//    @GetMapping("/getProductImage/{id}/image")
//    public ResponseEntity<byte[]> getProductImage(@PathVariable UUID id){
//        try{
//            Product product= productService.getProductById(id).getBody();
//            byte[] imageFile=product.getProductImageData();
//            return ResponseEntity.ok().contentType(MediaType.valueOf(product.getProductImageType())).body(imageFile);
//        }catch (Exception e){
//            return  ResponseEntity.notFound().build();
//        }
//    }
    //update product with image
    @PutMapping("/updateProductWithImage/{id}")
    public ResponseEntity<String> updateProductWithImage(@PathVariable UUID id,
                                                         @RequestPart Product product,
                                                         @RequestPart MultipartFile imageFile){
        return productService.updateProductWithImage(id,product,imageFile);

    }

    @GetMapping("/searchProduct")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        return productService.searchProduct(keyword);
    }

    @GetMapping("/getProductsBySellerId")
    public ResponseEntity<List<Product>> getProductsBySellerId(@RequestHeader("X-User-Id") String sellerId ){
        return productService.getProductsBySellerId(UUID.fromString(sellerId));
    }
    @GetMapping("/getTotal/{productIds}")
    public ResponseEntity<ProductData> getTotalAmount(@PathVariable UUID productIds){
            return productService.getTotalAmount(productIds);
    }

    @PutMapping("/updateAvailableCount")
    public void updateAvailableCount(@RequestBody CountUpdater countUpdater){
        productService.updateAvailableCount(countUpdater);
    }
}
