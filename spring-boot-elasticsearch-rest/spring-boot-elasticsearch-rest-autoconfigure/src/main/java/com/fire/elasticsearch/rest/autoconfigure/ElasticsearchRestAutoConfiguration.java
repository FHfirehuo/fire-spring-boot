package com.fire.elasticsearch.rest.autoconfigure;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * {@link EnableAutoConfiguration Auto-configuration} for Elasticsearch REST
 * clients.
 * 
 * 
 * @author: fire
 */
@Configuration
@ConditionalOnClass(RestClient.class)
@EnableConfigurationProperties(ElasticsearchRestProperties.class)
public class ElasticsearchRestAutoConfiguration {

	private final ElasticsearchRestProperties properties;

	private final List<ElasticsearchRestBuilderCustomizer> builderCustomizers;

	public ElasticsearchRestAutoConfiguration(ElasticsearchRestProperties properties,
			ObjectProvider<List<ElasticsearchRestBuilderCustomizer>> builderCustomizers) {
		this.properties = properties;
		this.builderCustomizers = builderCustomizers.getIfAvailable(Collections::emptyList);
	}

	@Bean
	@ConditionalOnMissingBean
	public RestClient restClient(RestClientBuilder builder) {
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public RestClientBuilder restClientBuilder() {
		HttpHost[] hosts = this.properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
		RestClientBuilder builder = RestClient.builder(hosts);
		PropertyMapper map = PropertyMapper.get();
		map.from(this.properties::getUsername).whenHasText().to((username) -> {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			Credentials credentials = new UsernamePasswordCredentials(this.properties.getUsername(),
					this.properties.getPassword());
			credentialsProvider.setCredentials(AuthScope.ANY, credentials);
			builder.setHttpClientConfigCallback(
					(httpClientBuilder) -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		});
		this.builderCustomizers.forEach((customizer) -> customizer.customize(builder));
		return builder;
	}

	@Configuration
	@ConditionalOnClass(RestHighLevelClient.class)
	public static class RestHighLevelClientConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {
			return new RestHighLevelClient(restClientBuilder);
		}

	}
}
