package com.cdq.taskprocessor.utils;

import java.util.List;
import java.util.UUID;

public final class UuidSupplier {

  private static final int UUID_SIZE = 4;

  private UuidSupplier() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static long getUuidExcluding(List<Long> existingIds) {
    long uuid = getUuid();

    while(existingIds.contains(uuid)) {
      uuid = getUuid();
    }

    return uuid;
  }

  private static long getUuid() {
    long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    return Long.parseLong(String.valueOf(uuid).substring(0, UUID_SIZE));
  }
}
