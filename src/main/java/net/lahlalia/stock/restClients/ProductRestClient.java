package net.lahlalia.stock.restClients;

import net.lahlalia.stock.dtos.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductRestClient {

    @GetMapping("/api/products/")
    public List<Product> getAllProducts();
    @GetMapping("/api/products/{idProduit}")
    public Product getProductById(@PathVariable Long idProduit);
}
