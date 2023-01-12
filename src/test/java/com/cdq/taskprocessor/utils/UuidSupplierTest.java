package com.cdq.taskprocessor.utils;

import static com.cdq.taskprocessor.utils.UuidSupplier.getUuidExcluding;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UuidSupplierTest {

  @Test
  void getUuidExcludingShouldReturnUuidThatIsNotInExistingIds() {
    int listSize = 1000;

    List<Long> existingUuids = new ArrayList<>();
    Long actualId;

    for (int i = 0; i < listSize; i++) {
      actualId = getUuidExcluding(existingUuids);
      assertFalse(existingUuids.contains(actualId));
      existingUuids.add(actualId);
    }

    assertEquals(listSize, existingUuids.size());
  }
}