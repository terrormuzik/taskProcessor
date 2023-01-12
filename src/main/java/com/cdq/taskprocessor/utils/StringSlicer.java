package com.cdq.taskprocessor.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class StringSlicer {

  private StringSlicer() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Map<Integer, String> sliceInputIntoPartsOfLength(String input, int patternLength) {
    if (input == null || input.isEmpty() || patternLength == 0) {
      return Collections.emptyMap();
    }

    HashMap<Integer, String> result = new HashMap<>();

    int partFrom = 0;
    int partTo = patternLength;

    while(partTo <= input.length()) {
      result.put(partFrom, input.substring(partFrom, partTo));
      partFrom++;
      partTo++;
    }

    return result;
  }

}
