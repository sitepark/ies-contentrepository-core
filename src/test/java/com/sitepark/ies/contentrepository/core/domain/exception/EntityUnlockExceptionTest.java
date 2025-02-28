package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import org.junit.jupiter.api.Test;

class EntityUnlockExceptionTest {
  @Test
  void testGetLock() {
    EntityLock lock = EntityLock.builder().entity("1").build();
    EntityUnlockException exception = new EntityUnlockException(lock, new Exception());
    assertEquals(lock, exception.getLock(), "lock id expected");
  }
}
