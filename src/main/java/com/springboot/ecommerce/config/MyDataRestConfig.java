package com.springboot.ecommerce.config;

import com.springboot.ecommerce.entity.Country;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.entity.ProductCategory;
import com.springboot.ecommerce.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE};
        // disable http methods for Product : Post, Put , delete
        disableHttpMethods(config, theUnsupportedActions,Product.class);
        // disable http methods for ProductCategory : Post, Put , delete
        disableHttpMethods(config, theUnsupportedActions,ProductCategory.class);
        // disable http methods for Country : Post, Put , delete
        disableHttpMethods(config, theUnsupportedActions, Country.class);
        // disable http methods for State : Post, Put , delete
        disableHttpMethods(config, theUnsupportedActions, State.class);

        // call an internal helper method
        exposeIds(config);

    }

    private static void disableHttpMethods(RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions,Class theClass) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata,httpMethos)->httpMethos.disable(theUnsupportedActions))
                .withCollectionExposure((metadata,httpMethos)->httpMethos.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config){

        // expose entity ids
        //
        // get list of all entity classes from entityManager
        Set<EntityType<?>> entities = this.entityManager.getMetamodel().getEntities();

        // - create an array list of entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for entities
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        //expose entity id for the array of entitity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);


    }
}
