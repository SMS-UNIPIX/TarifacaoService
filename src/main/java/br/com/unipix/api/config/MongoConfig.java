package br.com.unipix.api.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "br.com.unipix.api.mongo.repository")
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Value("${spring.data.mongodb.host}")
	public String mongoUri;

	@Value("${spring.data.mongodb.database}")
	public String mongoDataBase;

	@Override
	protected String getDatabaseName() {
		return mongoDataBase;
	}

	@Override
	protected void configureClientSettings(MongoClientSettings.Builder builder) {
		builder.applyConnectionString(new ConnectionString("mongodb://" + mongoUri));
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {

		// remove _class
		@SuppressWarnings("deprecation")
		MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);

		return mongoTemplate;

	}
}