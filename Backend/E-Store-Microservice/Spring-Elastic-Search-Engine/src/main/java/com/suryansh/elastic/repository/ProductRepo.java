package com.suryansh.elastic.repository;

import com.suryansh.elastic.doc.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface uses for communicating and performing ProductDoc index operation.
 */
@Repository
public interface ProductRepo extends ElasticsearchRepository<ProductDoc,String> {
}
