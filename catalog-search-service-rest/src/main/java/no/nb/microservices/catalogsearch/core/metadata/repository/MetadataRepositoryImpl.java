package no.nb.microservices.catalogsearch.core.metadata.repository;

import org.springframework.stereotype.Repository;

@Repository
public class MetadataRepositoryImpl implements IMetadataRepository {

    @Override
    public String findById(String id) {
        return "TODO";
    }

}
