module com.sitepark.ies.contentrepository.core {
	exports com.sitepark.ies.contentrepository.core.domain.entity;
	exports com.sitepark.ies.contentrepository.core.domain.entity.query.filter;
	exports com.sitepark.ies.contentrepository.core.domain.entity.query.sort;
	exports com.sitepark.ies.contentrepository.core.domain.entity.query.limit;
	exports com.sitepark.ies.contentrepository.core.domain.entity.query;
	exports com.sitepark.ies.contentrepository.core.domain.exception;
	exports com.sitepark.ies.contentrepository.core.port;
	exports com.sitepark.ies.contentrepository.core.usecase;

	requires jakarta.inject;
	requires org.eclipse.jdt.annotation;
	requires com.github.spotbugs.annotations;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires org.apache.logging.log4j;
	requires com.fasterxml.jackson.datatype.jsr310;

	opens com.sitepark.ies.contentrepository.core.domain.entity;
	opens com.sitepark.ies.contentrepository.core.domain.entity.query.filter;
	opens com.sitepark.ies.contentrepository.core.domain.entity.query.sort;
	opens com.sitepark.ies.contentrepository.core.domain.entity.query.limit;
	opens com.sitepark.ies.contentrepository.core.domain.entity.query;
}
