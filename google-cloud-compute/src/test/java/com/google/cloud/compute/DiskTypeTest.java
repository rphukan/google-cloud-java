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

import org.junit.Test;

public class DiskTypeTest {

  private static final String GENERATED_ID = "42";
  private static final Long CREATION_TIMESTAMP = 1453293540000L;
  private static final String DESCRIPTION = "description";
  private static final String VALID_DISK_SIZE = "10GB-10TB";
  private static final Long DEFAULT_DISK_SIZE_GB = 10L;
  private static final DiskTypeId DISK_TYPE_ID = DiskTypeId.of("project", "zone", "diskType");
  private static final DeprecationStatus<DiskTypeId> DEPRECATION_STATUS =
      DeprecationStatus.of(DeprecationStatus.Status.DELETED, DISK_TYPE_ID);
  private static final DiskType DISK_TYPE = DiskType.builder()
      .generatedId(GENERATED_ID)
      .diskTypeId(DISK_TYPE_ID)
      .creationTimestamp(CREATION_TIMESTAMP)
      .description(DESCRIPTION)
      .validDiskSize(VALID_DISK_SIZE)
      .defaultDiskSizeGb(DEFAULT_DISK_SIZE_GB)
      .deprecationStatus(DEPRECATION_STATUS)
      .build();

  @Test
  public void testBuilder() {
    assertEquals(GENERATED_ID, DISK_TYPE.generatedId());
    assertEquals(DISK_TYPE_ID, DISK_TYPE.diskTypeId());
    assertEquals(CREATION_TIMESTAMP, DISK_TYPE.creationTimestamp());
    assertEquals(DESCRIPTION, DISK_TYPE.description());
    assertEquals(VALID_DISK_SIZE, DISK_TYPE.validDiskSize());
    assertEquals(DEFAULT_DISK_SIZE_GB, DISK_TYPE.defaultDiskSizeGb());
    assertEquals(DEPRECATION_STATUS, DISK_TYPE.deprecationStatus());
  }

  @Test
  public void testToPbAndFromPb() {
    compareDiskTypes(DISK_TYPE, DiskType.fromPb(DISK_TYPE.toPb()));
    DiskType diskType = DiskType.builder().diskTypeId(DISK_TYPE_ID).build();
    compareDiskTypes(diskType, DiskType.fromPb(diskType.toPb()));
  }

  private void compareDiskTypes(DiskType expected, DiskType value) {
    assertEquals(expected, value);
    assertEquals(expected.generatedId(), value.generatedId());
    assertEquals(expected.diskTypeId(), value.diskTypeId());
    assertEquals(expected.creationTimestamp(), value.creationTimestamp());
    assertEquals(expected.description(), value.description());
    assertEquals(expected.validDiskSize(), value.validDiskSize());
    assertEquals(expected.defaultDiskSizeGb(), value.defaultDiskSizeGb());
    assertEquals(expected.deprecationStatus(), value.deprecationStatus());
    assertEquals(expected.hashCode(), value.hashCode());
  }
}
