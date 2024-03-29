package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Name.Builder.class)
public class Name extends OrderCriteria {

  protected Name(Builder builder) {
    super(builder);
  }

  @Override
  public String toString() {
    StringBuilder b =
        new StringBuilder(100)
            .append("OrderBy.Name[name:")
            .append(this.getName())
            .append(", direction:")
            .append(this.getDirection())
            .append(']');
    return b.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
  public static class Builder extends OrderCriteria.Builder<Builder> {

    protected Builder() {
      super();
    }

    protected Builder(Name name) {
      super(name);
    }

    @Override
    public Name build() {
      return new Name(this);
    }

    @Override
    public Builder self() {
      return this;
    }
  }
}
