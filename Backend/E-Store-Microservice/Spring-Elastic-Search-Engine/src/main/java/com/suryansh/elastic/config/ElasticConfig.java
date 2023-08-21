package com.suryansh.elastic.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
@Configuration
@EnableElasticsearchRepositories
public class ElasticConfig {

    @Value("${Elastic.host}")
    String host;
    @Value("${Elastic.port}")
    int port;
    @Value("${Elastic.username}")
    String username;
    @Value("${Elastic.password}")
    String password;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port,"https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }
}




