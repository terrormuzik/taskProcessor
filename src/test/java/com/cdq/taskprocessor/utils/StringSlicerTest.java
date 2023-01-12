package com.cdq.taskprocessor.utils;

import static com.cdq.taskprocessor.utils.StringSlicer.sliceInputIntoPartsOfLength;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StringSlicerTest {

  @Test
  void sliceInputIntoPartsOfLengthShouldReturnEmptyMapWhenPatternLengthIsZero() {
    Map<Integer, String> actualResult = sliceInputIntoPartsOfLength("qwe", 0);
    assertEquals(Collections.emptyMap(), actualResult);
  }

  @Test
  void sliceInputIntoPartsOfLengthShouldReturnEmptyMapWhenCalledWithNullInput() {
    Map<Integer, String> actualResult = sliceInputIntoPartsOfLength(null, 3);
    assertEquals(Collections.emptyMap(), actualResult);
  }

  @Test
  void sliceInputIntoPartsOfLengthShouldReturnEmptyMapWhenCalledWithEmptyInput() {
    Map<Integer, String> actualResult = sliceInputIntoPartsOfLength("", 0);
    assertEquals(Collections.emptyMap(), actualResult);
  }

  @Test
  void sliceInputIntoPartsOfLengthShouldSliceInputIntoEqualParts() {
    String input = "ABCDE";
    Map<Integer, String> expectedResult = Map.of(0, "ABC", 1, "BCD", 2, "CDE");
    Map<Integer, String> actualResult = sliceInputIntoPartsOfLength(input, 3);
    assertEquals(expectedResult, actualResult);
  }
}