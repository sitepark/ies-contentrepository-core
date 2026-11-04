[![codecov](https://codecov.io/gh/sitepark/ies-contentrepository-core/graph/badge.svg?token=AKQ68GKS9T)](https://codecov.io/gh/sitepark/ies-contentrepository-core)
[![Known Vulnerabilities](https://snyk.io/test/github/sitepark/ies-contentrepository-core/badge.svg)](https://snyk.io/test/github/sitepark/ies-contentrepository-core)

# Content-Repository

The content repository contains entries and groups. Both have master data and content in sections.

- **Entry** - any kind of content: pages, categories, configurations, events, services, persons, and the
  placement of a medium. The entry type decides what an entry can do: whether it is published, whether
  publishing creates a file (otherwise the entry is published without output, formerly a resource
  article) and whether it can be aggregated. A medium itself is managed by the media repository; an
  entry of the system type `ies:media` places it in a group. An optional property of an entry is a
  file name.
- **Group** - entries and groups are structured in groups, which can be nested. A group can carry
  content in sections like an entry, but it is neither published nor aggregated. An optional property
  of a group is a directory name, used for the files of the entries within it.

### Sections

The content of an entry or a group is an ordered list of sections. A section has

- a UUID, which stays the same when the section is moved,
- a type, the section type,
- an optional container (e.g. `sp_main_iterate`); sections without container are fixed parts of the
  entry,
- its data, a JSON object.

A section type may describe the data with a JSON schema; types without schema are possible. Selected
section types name a grant (`SectionGrant`) that an editor needs to change their sections. Section
types come from product and customer modules and the client configuration.

In the data, texts are strings. Links and media are URI values: `ies:entity/<id>` for an entry or a
group, `ies:media/<id>` for a medium, and any other URI for an external target. When an entry is
stored, these values are extracted as references, which feed the dependencies, the media repository
and the link checker.

Single sections can be added, updated, removed and moved. Each change is based on the version of the
entry the client has read and creates a new version of the whole entry. There is no localization in
the content model.

Points in time are `Instant`s.

## Features

Features of the content repository are:

* To store objects and groups
* To make objects and groups findable and return them.
* To version changes of objects and groups.
* To provide the history of an object.
* To lock objects for exclusive write access for others.
* Deliver different versions of an object.
* To be able to compare contents before saving, in order to be able to announce changes of individual fields
* To be able to revert an object to an older version.
* To be able to restore deleted objects (recycle bin).

### Saving

When saving an object or a group, the changes are always determined so that other components can react to them.

Rule when saving objects:
Only the data specified for saving will be changed. All other data remain unchanged.
When saving, all changes are compared with the actual state, so that all changed fields are known.

Contents can be structured in lists and sub-objects. If a list is saved, it is not merged but replaced. This means that if a list with three elements is updated with two elements, only the two elements are kept. If not all fields of the list element are specified, only the specified fields are updated, the others remain unchanged.

List elements can be reordered and then saved. In this case, the fields of the list elements that were not specified will also be reordered when saving.

### Handling media

The content module does not handle media but only uses references to media.

The media itself is managed in a media repository.

## Architecture

The architecture follows the principles of [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Ports

Ports are interfaces that the core requires for the execution of the individual usecases. The implementation is provided externally.

#### AccessControl

Is needed to be able to valid the permissions for individual actions.

#### ContentRepository

Basic CRUD operations for repository entries

#### Publisher

Publisher to handle publishing

#### MediaReferenceManager

Manage the references to the media-repository

#### EntityLockManager

Manage it exclusive write access.

#### HistoryManager

Provides methods to manage the history of an entry

#### IdGenerator

Creates unique ID's for new entries

#### RecycleBin

Recycle Bin Management

#### VersioningManager

Versioning management to be able to get or restore older versions.


## Coding-Standard

### Use @NotNull Annotation for Method-Arguments

```java
public Optional<Entity> remove(@NonNull Identifier identifier) {

	if (identifier == null) {
		throw new IllegalArgumentException("identifier is null");
	}

	// ...
}
```

### Use Unchecked Exceptions

```java
public abstract class ContentRepositoryException extends RuntimeException {
	// ...
}
```

See [How to handle Java exceptions in clean code](https://ahmadatwi.me/2017/01/20/how-to-handle-java-exceptions-in-clean-code-part-1/)

### Use Builder-Pattern for immutable objects

```java
public class Settings {

	private final String a;
	private final String b;

	private Settings(Builder builder) {
		this.a = builder.a;
		this.b = builder.b;
	}

	public String getA() {
		return this.a;
	}

	public String getB() {
		return this.b;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private String a;

		private String b;

		private Builder() {}

		private Builder(Settings settings) {
			this.a = settings.a;
			this.b = settings.b;
		}

		public Builder a(String a) {
			this.a = a;
			return this;
		}

		public Builder b(String b) {
			this.b = b;
			return this;
		}

		public Settings build() {
			return new Settings(this);
		}
	}
}
```

Inheritance is a bit of a challenge in the Builder pattern. See [here](https://github.com/rtenhove/eg-builder-inheritance) for a good explanation and apply the proposed solution. `com.sitepark.ies.contentrepository.core.domain.entity.Entity` shows this solution.
