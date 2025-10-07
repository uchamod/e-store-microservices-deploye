package com.uchamod.estore.Service;


import com.uchamod.estore.DTO.ProductData;
import com.uchamod.estore.Model.CountUpdater;
import com.uchamod.estore.Model.Product;
import com.uchamod.estore.Repo.Product_Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RequiredArgsConstructor
@Service
public class Product_Service {

    private final Product_Repo productRepo;
    private final File_store_service file_store_service;
    //get all products
    public ResponseEntity<List<Product>> getAllProducts() {
        try{
            List<Product> products = productRepo.findAll();
            if(products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    //get product by given id
    public ResponseEntity<Product> getProductById(UUID productId) {
        try{
            if(productId == null){
                return ResponseEntity.badRequest().build();
            }
            Optional<Product> product = productRepo.findById(productId);
            if(product.isPresent()){
                return new ResponseEntity<>(product.get(),HttpStatus.OK);
            }
            return  ResponseEntity.notFound().build();

        }catch (Exception e){
            e.fillInStackTrace();
        }
        return new ResponseEntity<>(new Product(), HttpStatus.BAD_REQUEST);
    }
    //add new products(without images)
    public ResponseEntity<String> addProduct(List<Product> products,UUID sellerId) {
        try{
            if(products.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            for(Product product : products){
                product.setSellerId(sellerId);
            }
            productRepo.saveAll(products);
            return new ResponseEntity<>("succsussfuly stored the data", HttpStatus.CREATED);
        }catch (Exception e){
            e.fillInStackTrace();

        }
        return new ResponseEntity<>("faild to store data", HttpStatus.BAD_REQUEST);
    }

    //update a product by id
    public ResponseEntity<String> updateProduct(Product product) {
        try {
            if(productRepo.existsById(product.getProductId())){
                productRepo.save(product);
                return new ResponseEntity<>("succsussfuly update the product", HttpStatus.OK);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return new ResponseEntity<>("Faild to update product", HttpStatus.BAD_REQUEST);
    }

    //delete a product by id
    public ResponseEntity<String> deleteProduct(UUID productId,UUID sellerId) {
        try {
            Optional<Product> existingProduct=productRepo.findById(productId);
            if(existingProduct.isPresent()){
                if(!existingProduct.get().getSellerId().equals(sellerId)){
                    System.out.println("cannot delete others content");
                    return ResponseEntity.badRequest().build();
                }
                if(!existingProduct.get().getProductImage().isEmpty()){
                    file_store_service.deleteFile(existingProduct.get().getProductImage());
                }

                productRepo.deleteById(productId);
                return new ResponseEntity<>("succsussfuly delete the product", HttpStatus.OK);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return new ResponseEntity<>("Faild to delete product", HttpStatus.BAD_REQUEST);
    }
    //find products by product category
    public ResponseEntity<List<Product>> getProductByCategory(String category) {
        try{
            if(category == null || category.trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            List<Product> products=productRepo.findProductByProductCategory(category);

            if(products.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            return new  ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //get product by brand
    public ResponseEntity<List<Product>> getProductByBrand(String brand) {
        try{
            if(brand == null || brand.trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            List<Product> products=productRepo.findProductByProductBrand(brand);

            if(products.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            return new  ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("error"+e);
            e.fillInStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //add new product with image
    public ResponseEntity<Product> addProductWithImage(Product product, MultipartFile imageFile,UUID sellerId) {
        try{
            if(product == null){
                return ResponseEntity.badRequest().build();
            }
            product.setSellerId(sellerId);
            if(imageFile.isEmpty()){
              Product newProduct =  productRepo.save(product);
              return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
            }
            String imageUrl=file_store_service.uploadFile(imageFile);
            //set image data
            product.setProductImageName(imageFile.getOriginalFilename());
            product.setProductImageType(imageFile.getContentType());
            product.setProductImage(imageUrl);

            return new ResponseEntity<>(productRepo.save(product),HttpStatus.CREATED);
        }catch (Exception e){
            System.err.println("Error adding product with image: " + e.getMessage());
            e.fillInStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //update image with image file
    public ResponseEntity<String> updateProductWithImage(UUID id, Product product, MultipartFile imageFile) {
        try{
            Optional<Product> existingProduct=productRepo.findById(id);
            if(existingProduct.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            if(product == null){
                return ResponseEntity.badRequest().build();
            }

            if(imageFile.isEmpty()){
                productRepo.save(product);
                return  ResponseEntity.ok("product updated successfully");
            }
            String imageUrl=file_store_service.uploadFile(imageFile);
            file_store_service.deleteFile(existingProduct.get().getProductImage());
            product.setProductImage(imageUrl);
            product.setProductImageType(imageFile.getContentType());
            product.setProductImageName(imageFile.getOriginalFilename());
            productRepo.save(product);
            return  ResponseEntity.ok("product updated successfully with image");

        }catch (Exception e){
            System.err.println("Error updating product: " + e.getMessage());
            e.fillInStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
   //search product by keyword
    public ResponseEntity<List<Product>> searchProduct(String keyword) {
        try{
            if(keyword==null){
                return ResponseEntity.badRequest().build();
            }
           List<Product> products= productRepo.searchProduct(keyword);
            if(products.isEmpty()){
                return ResponseEntity.notFound().build();

            }
            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("error"+e);
            e.fillInStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<List<Product>> getProductsBySellerId(UUID sellerId) {
        try{
              if(sellerId == null){
                  System.out.println("no seller id provided");
                  return ResponseEntity.badRequest().build();
              }
            List<Product> products=  productRepo.findProductBySellerId(sellerId);
            if(products.isEmpty()){
                System.out.println("no product related to sellerId :"+sellerId);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(products);

        }catch (Exception e){
            System.out.println("Internal server error"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
   //get total amount
    public ResponseEntity<ProductData> getTotalAmount(UUID productIds) {
        try{
            if(productIds== null){
                return ResponseEntity.badRequest().build();
            }
            ProductData productData=productRepo.findProductPriceByProductId(productIds);
            return ResponseEntity.ok(productData);
        }catch (Exception e){
            System.out.println("exeption from product service"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
//update current product count of a product
    public void updateAvailableCount(CountUpdater countUpdater) {
        try{

              Product product=  productRepo.findById(countUpdater.getProductId()).get();
              if(countUpdater.getIsIncrease()){
                  product.setProductCount(product.getProductCount()+countUpdater.getProductCount());
              }else {
                  product.setProductCount(product.getProductCount()-countUpdater.getProductCount());
              }
              productRepo.save(product);
                System.out.println("update product count");

        }catch (Exception e){
            System.out.println("item count is not updated"+e.getMessage());
        }
    }
}
