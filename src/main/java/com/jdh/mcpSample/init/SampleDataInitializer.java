package com.jdh.mcpSample.init;

import com.jdh.mcpSample.api.product.domain.entity.Product;
import com.jdh.mcpSample.api.product.domain.repository.ProductRepository;
import com.jdh.mcpSample.api.region.domain.entity.Region;
import com.jdh.mcpSample.api.region.domain.repository.RegionRepository;
import com.jdh.mcpSample.api.store.domain.entity.Store;
import com.jdh.mcpSample.api.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SampleDataInitializer implements ApplicationRunner {

    private final RegionRepository regionRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, List<Map<String, List<Map<String, Integer>>>>> sampleData = Map.of(
                "서울", List.of(
                        Map.of("편의점 CU", List.of(
                                Map.of("삼각김밥", 1200),
                                Map.of("생수 500ml", 800),
                                Map.of("초코바", 1000)
                        )),
                        Map.of("올리브영", List.of(
                                Map.of("핸드크림", 5500),
                                Map.of("립밤", 4500)
                        ))
                ),
                "부산", List.of(
                        Map.of("이마트24", List.of(
                                Map.of("우유 1L", 2500),
                                Map.of("식빵", 2800)
                        )),
                        Map.of("약국", List.of(
                                Map.of("두통약", 3500),
                                Map.of("종합비타민", 12500)
                        ))
                ),
                "대구", List.of(
                        Map.of("베이커리", List.of(
                                Map.of("크로아상", 3200),
                                Map.of("마카롱", 2700)
                        )),
                        Map.of("문구점", List.of(
                                Map.of("볼펜", 1500),
                                Map.of("노트", 2000)
                        ))
                )
        );

        for (var entry : sampleData.entrySet()) {
            String regionName = entry.getKey();
            Region savedRegion = regionRepository.save(Region.builder().name(regionName).build());

            for (Map<String, List<Map<String, Integer>>> storeEntry : entry.getValue()) {
                for (var storeData : storeEntry.entrySet()) {
                    String storeName = storeData.getKey();
                    Store savedStore = storeRepository.save(Store.builder()
                            .regionId(savedRegion.getId())
                            .name(storeName)
                            .build());

                    for (Map<String, Integer> productData : storeData.getValue()) {
                        for (var productEntry : productData.entrySet()) {
                            productRepository.save(Product.builder()
                                    .storeId(savedStore.getId())
                                    .name(productEntry.getKey())
                                    .price(productEntry.getValue())
                                    .build());
                        }
                    }
                }
            }
        }
    }
}
