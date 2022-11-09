package dmo.server.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

@Configuration
public class RepositoryConfiguration {
    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {

            @NotNull
            @Override
            public String getReverseColumnName(@NotNull RelationalPersistentProperty property) {
                return NamingStrategy.super.getReverseColumnName(property) + "_id";
            }

            @NotNull
            @Override
            public String getReverseColumnName(@NotNull PersistentPropertyPathExtension path) {
                return NamingStrategy.super.getReverseColumnName(path) + "_id";
            }
        };
    }
}
