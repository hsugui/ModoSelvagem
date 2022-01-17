package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.*;
import br.com.rd.ModoSelvagem.model.embeddable.SalePriceId;
import br.com.rd.ModoSelvagem.model.entity.*;
import br.com.rd.ModoSelvagem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    SubcategoryService subcategoryService;

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = dtoToBusiness(productDTO);

        if (product.getBrand() != null) {
            Long idBrand = product.getBrand().getId();
            Brand b;
            if (idBrand != null) {
                b = brandRepository.getById(idBrand);
                product.setBrand(b);
            }
        }

        if (product.getSubcategory() != null) {
            Long idSubcategory = product.getSubcategory().getId();
            Subcategory b;
            if (idSubcategory != null) {
                b = subcategoryRepository.getById(idSubcategory);
                product.setSubcategory(b);
            }
        }

        product.setUpdateDate(LocalDateTime.now());
        product = productRepository.save(product);


        return businessToDto(product);
    }

    public List<ProductDTO> findAllProducts() {
        List<Product> all = productRepository.findAll();
        return listToDto(all);
    }

    public ProductsPageDTO findProductsBySubcategory(Long id) {
        ProductsPageDTO productsPage = new ProductsPageDTO();
        Subcategory subcategory = subcategoryRepository.findById(id).get();
        productsPage.setSubcategory(subcategoryService.businessToDto(subcategory));
        List<Product> productsBySubcategory = productRepository.findBySubcategoryId(id);
        productsPage.setProducts(listCardToDto(productsBySubcategory));
        return productsPage;
    }

    public List<ProductCardDTO> findProductsByCategory(Long id) {
        List<Product> productsByCategory = productRepository.findByCategoryId(id);
        return listCardToDto(productsByCategory);
    }

    public CategoryProductsDTO findProductsAndCategoryNameByCategoryId(Long id) {
        CategoryProductsDTO categoryProducts = new CategoryProductsDTO();
        String category = categoryRepository.findById(id).get().getCategoryName();
        categoryProducts.setCategory(category);
        List<Product> productsByCategory = productRepository.findByCategoryId(id);
        categoryProducts.setProducts(listCardToDto(productsByCategory));
        return categoryProducts;
    }

    public List<HomeDTO> findHomeProductsByCategory() {
        List<HomeDTO> homeItems = new ArrayList<>();
        for (Long i = 1l; i <= 3; i++) {
            List<ProductCardDTO> products = new ArrayList<>();
            if (!findProductsByCategory(i).isEmpty()) {
                products = findProductsByCategory(i);
                Collections.shuffle(products);
                products = products.subList(0, 4);
            }
            Category category = categoryRepository.findById(i).get();
            HomeDTO homeDTO = new HomeDTO();
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setCategoryName(category.getCategoryName());
            homeDTO.setCategory(categoryDTO);
            homeDTO.setProducts(products);
            homeItems.add(homeDTO);
        }
        return homeItems;
    }


    public List<ProductCardDTO> findByProductNameOrDescription(String productName) {
        List<Product> products = productRepository.findByProductNameOrDescription(productName);
        return listToProductItemDto(products);
    }

    public ProductItemDTO findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return businessToProductItemDto(product.get());
        }
        return null;
    }

    public void subtractProductStorage(Long productId, Integer quantity) {
        if (storageRepository.existsById(productId)) {
            Storage storage = storageRepository.findById(productId).get();
            storage.setQuantity(storage.getQuantity() - quantity);
            storageRepository.save(storage);
        }
    }

    private Product dtoToBusiness(ProductDTO dto) {
        Product business = new Product();
        business.setName(dto.getName());
        business.setShortDescription(dto.getShortDescription());
        business.setFullDescription(dto.getFullDescription());
        business.setWeight(dto.getWeight());
        business.setMaterial(dto.getMaterial());
        business.setColor(dto.getColor());
        business.setDimensions(dto.getDimensions());
        business.setWarranty(dto.getWarranty());


        if (dto.getBrand() != null) {
            Brand b = new Brand();
            if (dto.getBrand().getId() != null) {
                b.setId(dto.getBrand().getId());
            } else {
                b.setBrandName(dto.getBrand().getBrandName());
            }
            business.setBrand(b);
        }

        if (dto.getSubcategory() != null) {
            Subcategory s = new Subcategory();
            if (dto.getSubcategory().getId() != null) {
                s.setId(dto.getSubcategory().getId());
            } else {
                s.setSubcategoryName(dto.getSubcategory().getSubcategoryName());
            }
            business.setSubcategory(s);
        }

        if (dto.getImages() != null) {
            List<ProductImage> imgList = new ArrayList<>();
            for (ProductImageDTO imgDto : dto.getImages()) {
                ProductImage imgBusiness = new ProductImage();
                imgBusiness.setPath(imgDto.getPath());
                imgBusiness.setTitle(imgDto.getTitle());
                imgList.add(imgBusiness);
            }
            business.setImages(imgList);
        }

        if (dto.getPrices() != null) {
            List<SalePrice> salePrices = new ArrayList<>();
            for (SalePriceDTO priceDto : dto.getPrices()) {
                SalePrice priceBusiness = new SalePrice();
                SalePriceId priceId = new SalePriceId();
                priceId.setValidity(priceDto.getSalePriceId().getValidity());
                priceBusiness.setPrice(priceDto.getPrice());
                priceBusiness.setSalePriceId(priceId);
                salePrices.add(priceBusiness);
            }
            business.setPrices(salePrices);
        }

        if (dto.getStorage() != null) {
            Storage s = new Storage();
            if (dto.getStorage().getProductId() != null) {
                s.setId(dto.getStorage().getProductId());
            } else {
                s.setQuantity(dto.getStorage().getQuantity());
                s.setProduct(business);
            }
            business.setStorage(s);
        }

        return business;
    }

    public ProductDTO businessToDto(Product business) {
        ProductDTO dto = new ProductDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setShortDescription(business.getShortDescription());
        dto.setFullDescription(business.getFullDescription());
        dto.setWeight(business.getWeight());
        dto.setMaterial(business.getMaterial());
        dto.setColor(business.getColor());
        dto.setDimensions(business.getDimensions());
        dto.setWarranty(business.getWarranty());
        dto.setUpdateDate(business.getUpdateDate());

        if (business.getSubcategory() != null) {
            SubcategoryResponseDTO subcategoryDTO = new SubcategoryResponseDTO();
            subcategoryDTO.setId(business.getSubcategory().getId());
            subcategoryDTO.setSubcategoryName(business.getSubcategory().getSubcategoryName());
            dto.setSubcategory(subcategoryDTO);

        }

        if (business.getStorage() != null) {
            StorageDTO sDTO = new StorageDTO();
            sDTO.setProductId(business.getStorage().getId());
            sDTO.setQuantity(business.getStorage().getQuantity());
            dto.setStorage(sDTO);
        }

        if (business.getBrand() != null) {
            BrandDTO bDTO = new BrandDTO();
            bDTO.setId(business.getBrand().getId());
            bDTO.setBrandName(business.getBrand().getBrandName());
            dto.setBrand(bDTO);
        }

        if (business.getImages() != null) {
            List<ProductImageDTO> listImgDTO = new ArrayList<>();
            for (ProductImage img : business.getImages()) {
                ProductImageDTO imgDTO = new ProductImageDTO();
                imgDTO.setId(img.getId());
                imgDTO.setTitle(img.getTitle());
                imgDTO.setPath(img.getPath());
                listImgDTO.add(imgDTO);
            }
            dto.setImages(listImgDTO);
        }

        if (business.getPrices() != null) {
            List<SalePriceDTO> pricesDTO = new ArrayList<>();
            for (SalePrice sl : business.getPrices()) {
                SalePriceDTO slDTO = new SalePriceDTO();
                SalePriceIdDTO priceId = new SalePriceIdDTO();
                priceId.setId(sl.getSalePriceId().getId());
                priceId.setValidity(sl.getSalePriceId().getValidity());
                slDTO.setSalePriceId(priceId);
                slDTO.setPrice(sl.getPrice());
                pricesDTO.add(slDTO);
            }
            dto.setPrices(pricesDTO);
        }

        return dto;
    }

    private ProductCardDTO businessProductCardToDTO(Product product) {
        ProductCardDTO productCard = new ProductCardDTO();
        productCard.setId(product.getId());
        productCard.setName(product.getName());
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            productCard.setImage(product.getImages().get(0).getPath());
        }
        if (product.getPrices() != null && !product.getPrices().isEmpty()) {
            productCard.setPrice(product.getPrices().get(0).getPrice());
        }
        if (product.getStorage() != null) {
            Storage storageBusiness = product.getStorage();
            productCard.setStorage(storageBusiness.getQuantity());
        }
        return productCard;
    }

    private List<ProductDTO> listToDto(List<Product> list) {
        List<ProductDTO> listDto = new ArrayList<>();
        for (Product p : list) {
            listDto.add(businessToDto(p));
        }
        return listDto;
    }

    private List<ProductCardDTO> listCardToDto(List<Product> list) {
        List<ProductCardDTO> listCardDto = new ArrayList<>();
        for (Product p : list) {
            listCardDto.add(businessProductCardToDTO(p));
        }
        return listCardDto;
    }

    private List<ProductCardDTO> listToProductItemDto(List<Product> list) {
        List<ProductCardDTO> listDto = new ArrayList<>();
        for (Product p : list) {
            listDto.add(businessProductCardToDTO(p));
        }
        return listDto;
    }


    private ProductItemDTO businessToProductItemDto(Product business) {
        ProductItemDTO dto = new ProductItemDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setDimensions(business.getDimensions());
        dto.setWarranty(business.getWarranty());
        dto.setMaterial(business.getMaterial());
        dto.setColor(business.getColor());
        dto.setShortDescription(business.getShortDescription());
        dto.setFullDescription(business.getFullDescription());
        dto.setWeight(business.getWeight());

        if (business.getBrand() != null) {
            Brand businessBrand = business.getBrand();
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(businessBrand.getId());
            brandDTO.setBrandName(businessBrand.getBrandName());
            dto.setBrand(brandDTO);
        }

        if (business.getSubcategory() != null) {
            SubcategoryResponseDTO subcategoryDTO = new SubcategoryResponseDTO();
            subcategoryDTO.setSubcategoryName(business.getSubcategory().getSubcategoryName());
            subcategoryDTO.setId(business.getSubcategory().getId());
            dto.setSubcategory(subcategoryDTO);
        }

        if (business.getPrices() != null) {
            SalePrice currentPrice = business.getPrices().get(0);
            for (SalePrice sp : business.getPrices()) {
                if (currentPrice.getSalePriceId().getValidity().isBefore(sp.getSalePriceId().getValidity())) {
                    currentPrice = sp;
                }
            }
            dto.setPrice(currentPrice.getPrice());
        }

        if (business.getImages() != null) {
            List<ProductImage> productImages = business.getImages();
            List<ProductImageDTO> productImageDTOS = new ArrayList<>();
            for (ProductImage productImage : productImages) {
                productImageDTOS.add(businessToProductImageDto(productImage));
            }
            dto.setImages(productImageDTOS);
        }

        if (business.getStorage() != null) {
            Storage storageBusiness = business.getStorage();
            dto.setStorage(storageBusiness.getQuantity());
        }
        return dto;
    }

    private ProductImageDTO businessToProductImageDto(ProductImage productImage) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(productImage.getId());
        dto.setTitle(productImage.getTitle());
        dto.setPath(productImage.getPath());
        return dto;
    }

}
