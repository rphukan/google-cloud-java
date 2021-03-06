/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.compute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RegionIdTest {

  private static final String PROJECT = "project";
  private static final String REGION = "region";
  private static final String URL =
      "https://www.googleapis.com/compute/v1/projects/project/regions/region";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testOf() {
    RegionId regionId = RegionId.of(PROJECT, REGION);
    assertEquals(PROJECT, regionId.project());
    assertEquals(REGION, regionId.region());
    assertEquals(URL, regionId.selfLink());
    regionId = RegionId.of(REGION);
    assertNull(regionId.project());
    assertEquals(REGION, regionId.region());
  }

  @Test
  public void testToAndFromUrl() {
    RegionId regionId = RegionId.of(PROJECT, REGION);
    compareRegionId(regionId, RegionId.fromUrl(regionId.selfLink()));
  }

  @Test
  public void testSetProjectId() {
    RegionId regionId = RegionId.of(PROJECT, REGION);
    assertSame(regionId, regionId.setProjectId(PROJECT));
    compareRegionId(regionId, RegionId.of(REGION).setProjectId(PROJECT));
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("notMatchingUrl is not a valid region URL");
    RegionId.fromUrl("notMatchingUrl");
  }

  @Test
  public void testMatchesUrl() {
    assertTrue(RegionId.matchesUrl(RegionId.of(PROJECT, REGION).selfLink()));
    assertFalse(RegionId.matchesUrl("notMatchingUrl"));
  }

  private void compareRegionId(RegionId expected, RegionId value) {
    assertEquals(expected, value);
    assertEquals(expected.project(), expected.project());
    assertEquals(expected.region(), expected.region());
    assertEquals(expected.selfLink(), expected.selfLink());
    assertEquals(expected.hashCode(), expected.hashCode());
  }
}
