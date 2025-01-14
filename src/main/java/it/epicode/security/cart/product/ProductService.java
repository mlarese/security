package it.epicode.security.cart.product;

import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AppUserService appUserService;

    public Product saveProduct(@Valid ProductCreatRequest request, String username) {
        // controlliamo se esiste già un prodotto con quel codice
        if (productRepository.existsByCode(request.getCode())) {
            throw new EntityExistsException("Product with code " + request.getCode() + " already exists");
        }

        AppUser seller = appUserService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Seller with username " + username + " not found"));


        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        product.setSeller(seller);

        return productRepository.save(product);

    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll( pageable);
    }

}
