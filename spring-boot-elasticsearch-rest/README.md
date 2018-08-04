#Elasticsearch High Level REST Client Starter

A spring boot of elasticsearch 6.x high level rEST client starter

    本项目依赖于elasticsearch 6.2.4 的 rest-high-level-client。
    提供基于springboot的elasticsearchrest-high-level-client自动注入功能
    
Installation and Getting Started
--

maven

```xml
<dependency>
    <groupId>com.fire</groupId>
    <artifactId>spring-boot-starter-elasticsearch-rest</artifactId>
    <version>${revsion}</version>
</dependency>
```

application.properties

```properties
spring.elasticsearch.rest.uris= #默认http://localhost:9200。各个node用逗号(,)分割。
spring.elasticsearch.rest.username=
spring.elasticsearch.rest.password=
```

springboot

```java
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PressureService {

	private RestHighLevelClient restHighLevelClient;
	
	@Autowired
	public PressureService(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}
	
	...
}
```

