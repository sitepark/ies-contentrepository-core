module com.sitepark.ies.contentrepository.core {
	exports com.sitepark.ies.contentrepository.core.domain.entity;
	exports com.sitepark.ies.contentrepository.core.domain.exception;
	exports com.sitepark.ies.contentrepository.core.port;
	exports com.sitepark.ies.contentrepository.core.usecase;
	requires javax.inject;
	requires org.eclipse.jdt.annotation;
}