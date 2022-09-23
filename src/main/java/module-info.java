module com.sitepark.ies.contentrepository.core {

	exports com.sitepark.ies.contentrepository.core.domain.entity;
	exports com.sitepark.ies.contentrepository.core.domain.entity.filter;
	exports com.sitepark.ies.contentrepository.core.domain.entity.sort;
	exports com.sitepark.ies.contentrepository.core.domain.entity.query;
	exports com.sitepark.ies.contentrepository.core.domain.exception;
	exports com.sitepark.ies.contentrepository.core.port;
	exports com.sitepark.ies.contentrepository.core.usecase;

	requires javax.inject;
	requires org.eclipse.jdt.annotation;
	requires com.github.spotbugs.annotations;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires org.apache.logging.log4j;

	opens com.sitepark.ies.contentrepository.core.domain.entity;
	opens com.sitepark.ies.contentrepository.core.domain.entity.filter;
	opens com.sitepark.ies.contentrepository.core.domain.entity.sort;
	opens com.sitepark.ies.contentrepository.core.domain.entity.query;
}