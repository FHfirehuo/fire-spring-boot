package com.fire.elasticsearch.rest.autoconfigure;

import org.elasticsearch.client.RestClientBuilder;

/**
 * Callback interface that can be implemented by beans wishing to further customize the
 * {@link org.elasticsearch.client.RestClient} via a {@link RestClientBuilder} whilst
 * retaining default auto-configuration.
 *
 * @author fire
 * @since 2.1.0
 */
@FunctionalInterface
public interface ElasticsearchRestBuilderCustomizer {

	/**
	 * Customize the {@link RestClientBuilder}.
	 * @param builder the builder to customize
	 */
	void customize(RestClientBuilder builder);
}
