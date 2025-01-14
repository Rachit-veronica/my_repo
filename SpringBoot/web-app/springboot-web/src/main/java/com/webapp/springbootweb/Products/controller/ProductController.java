package com.webapp.springbootweb.Products.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.webapp.springbootweb.Products.ExceptionalHandeling.ProductErrorResponse;
import com.webapp.springbootweb.Products.ExceptionalHandeling.ProductNotFoundException;
import com.webapp.springbootweb.Products.entities.Product;
import com.webapp.springbootweb.Products.services.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @RestController
@Controller
@Transactional
public class ProductController {

    @Autowired
    private ProductService service;
   
    // // spring-jcl -> spring common logging bridge
    // Logger logger = LoggerFactory.getLogger(ProductController.class);

    // @ResponseBody
    @GetMapping("/product")
    public String findAllProducts(Model model) {
        // return service.getProducts();
        List<Product> Listproduct = service.getProducts();
        model.addAttribute("Listproduct", Listproduct);
        return "products";
    }

    @GetMapping("/product/edit/{id}")
    public String findProductById(@PathVariable int id, Model model, RedirectAttributes redirectAttribute) {
        try {
            Product product = service.getProductsById(id);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            return "Addnew";
        } catch (ProductNotFoundException e) {
            redirectAttribute.addFlashAttribute("message", e.getMessage());
            return "redirect:/product";
        }

    }


    // add an exception handler using @ExceptionalHandler

    @ExceptionHandler
    public String handleException(ProductNotFoundException exe) {

        // create a ProductErrorResponce
        ProductErrorResponse error = new ProductErrorResponse();
        error.setMessage(exe.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(System.nanoTime());
        // return ResponseEntity
        // return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        return "redirect:/product";
    }


    // updateProduct
    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Product prop = service.updateProduct(product);
        return new ResponseEntity<Product>(prop, prop != null ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND);
    }

    // updateProductbyId
    @PutMapping("/product/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product prop = service.updateProductbyId(product, id);
        return new ResponseEntity<Product>(prop, prop != null ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable Integer id) {

        if (service.getProductsById(id) != null) {
            return service.deleteProduct(id);
        } else {
            return "product not found";
        }

        // return service.deleteProduct(id);
    }

    @GetMapping("/product/new")
    public String ShowAddNewPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add new Product");
        return "Addnew";
    }

    @PostMapping("/product/save")
    public String addProduct(Product product, RedirectAttributes redirectAttribute) {
        service.saveProduct(product);
        System.out.println(product.getPrice());
        redirectAttribute.addFlashAttribute("message", "The Product has been saved successfully.");
        return "redirect:/product";
    }

    // @PutMapping("/product/edit/{id}")
    // public String showEditForm(@PathVariable("id") Integer id,Model model){

    // if (service.CheckAvalability(id) == null ){
    // throw new ProductNotFoundException("Student id Not Found - "+id);
    // }
    // Product prop= service.getProductsById(id);
    // // return new ResponseEntity<Product>(prop, prop!=null ? HttpStatus.FOUND :
    // HttpStatus.NOT_FOUND);
    // model.addAttribute("Listproduct", prop);
    // return "products";
    // }

    @GetMapping("/product/delete/{id}")
    public String deleteProductById(@PathVariable int id, RedirectAttributes redirectAttribute) {
        try {
            service.deleteProduct(id);
            redirectAttribute.addFlashAttribute("message", "Product ID- " + id + " Successfully deleted!");
        } catch (ProductNotFoundException e) {
            redirectAttribute.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/product";
    }

}
