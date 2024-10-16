package org.example.petstore.controller;

import org.example.petstore.model.Product;
import org.example.petstore.model.ProductCategory;
import org.example.petstore.service.InventoryService;
import org.example.petstore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductController is responsible for handling requests related to displaying products,
 * product categories, and managing product inventory.
 */
@Controller
public class ProductController {
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Autowired
    public ProductController(ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    /**
     * Displays the landing page for authenticated users (USER role).
     *
     * @param model the Model object to pass attributes to the view
     * @return the landing page view for users
     */
    @GetMapping("/user")
    public String getLandingUser(Model model) {
        List<ProductCategory> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "landing/landingUSER";
    }

    /**
     * Displays the landing page for admin users (ADMIN role).
     *
     * @param model the Model object to pass attributes to the view
     * @return the landing page view for admins
     */
    @GetMapping("/admin")
    public String getLandingAdmin(Model model) {
        List<ProductCategory> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "landing/landingADMIN";
    }

    /**
     * Displays the landing page for unauthorized (guest) users.
     *
     * @param model the Model object to pass attributes to the view
     * @return the landing page view for unauthorized users
     */
    @GetMapping("/")
    public String getLandingUnauthorized(Model model) {
        List<ProductCategory> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "landing/landingUnauthorized";
    }

    /**
     * Displays the products based on the selected category.
     * It also checks the stock for each product and includes it in the view.
     *
     * @param categoryId the ID of the product category to filter products
     * @param model the Model object to pass attributes to the view
     * @return the view displaying products by category
     */
    @GetMapping("/products")
    public String getProductsByCategory(@RequestParam("categoryId") int categoryId, Model model) {
        List<Product> products = productService.getProductsByCategory(categoryId);

        // check quantity
        Map<Integer, Integer> productStockMap = new HashMap<>();

        for (Product product : products) {
            int stock = inventoryService.getStockByProduct(product.getId());
            productStockMap.put(product.getId(), stock);
        }

        model.addAttribute("products", products);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("productStockMap", productStockMap);

        // add success message if exists
        if (model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", model.asMap().get("successMessage"));
        }

        return "products/productsByCategory";
    }

}
