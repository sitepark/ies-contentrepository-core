package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import org.junit.jupiter.api.Test;

class EntityLockedExceptionTest {

  @Test
  void testGetLock() {
    EntityLock lock = mock();
    EntityLockedException a = new EntityLockedException(lock);
    assertEquals(lock, a.getLock(), "unexpected lock");
  }

  @Test
  void testGetMessage() {
    EntityLock lock = mock();
    EntityLockedException a = new EntityLockedException(lock);
    assertTrue(a.getMessage().contains(lock.toString()), "lock expected in message");
  }
}
