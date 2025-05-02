module com.sitepark.ies.contentrepository.core {
  exports com.sitepark.ies.contentrepository.core.domain.entity;
  exports com.sitepark.ies.contentrepository.core.domain.entity.permission;
  exports com.sitepark.ies.contentrepository.core.usecase.query.filter;
  exports com.sitepark.ies.contentrepository.core.usecase.query.sort;
  exports com.sitepark.ies.contentrepository.core.usecase.query.limit;
  exports com.sitepark.ies.contentrepository.core.usecase.query;
  exports com.sitepark.ies.contentrepository.core.domain.exception;
  exports com.sitepark.ies.contentrepository.core.port;
  exports com.sitepark.ies.contentrepository.core.usecase;

  requires jakarta.inject;
  requires com.github.spotbugs.annotations;
  requires com.fasterxml.jackson.datatype.jdk8;
  requires com.fasterxml.jackson.datatype.jsr310;
  requires com.sitepark.ies.sharedkernel;
  requires org.apache.logging.log4j;

  opens com.sitepark.ies.contentrepository.core.domain.entity;
  opens com.sitepark.ies.contentrepository.core.usecase.query.filter;
  opens com.sitepark.ies.contentrepository.core.usecase.query.sort;
  opens com.sitepark.ies.contentrepository.core.usecase.query.limit;
  opens com.sitepark.ies.contentrepository.core.usecase.query;
  opens com.sitepark.ies.contentrepository.core.domain.entity.permission;
}
