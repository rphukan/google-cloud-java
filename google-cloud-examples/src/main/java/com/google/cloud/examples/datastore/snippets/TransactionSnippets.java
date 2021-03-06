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

/*
 * EDITING INSTRUCTIONS
 * This file is referenced in Transaction's javadoc. Any change to this file should be reflected in
 * Transaction's javadoc.
 */

package com.google.cloud.examples.datastore.snippets;

import com.google.api.client.util.Lists;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Transaction;

import java.util.Iterator;
import java.util.List;

/**
 * This class contains a number of snippets for the {@link Transaction} interface.
 */
public class TransactionSnippets {

  private final Transaction transaction;

  public TransactionSnippets(Transaction transaction) {
    this.transaction = transaction;
  }

  /**
   * Example of getting an entity for a given key.
   */
  // [TARGET get(Key)]
  // [VARIABLE "my_key_name"]
  public Entity get(String keyName) {
    Datastore datastore = transaction.datastore();
    // [START get]
    Key key = datastore.newKeyFactory().kind("MyKind").newKey(keyName);
    Entity entity = transaction.get(key);
    transaction.commit();
    // Do something with the entity
    // [END get]
    return entity;
  }

  /**
   * Example of getting entities for several keys.
   */
  // [TARGET get(Key...)]
  // [VARIABLE "my_first_key_name"]
  // [VARIABLE "my_second_key_name"]
  public List<Entity> getMultiple(String firstKeyName, String secondKeyName) {
    Datastore datastore = transaction.datastore();
    // TODO change so that it's not necessary to hold the entities in a list for integration testing
    // [START getMultiple]
    KeyFactory keyFactory = datastore.newKeyFactory().kind("MyKind");
    Key firstKey = keyFactory.newKey(firstKeyName);
    Key secondKey = keyFactory.newKey(secondKeyName);
    Iterator<Entity> entitiesIterator = transaction.get(firstKey, secondKey);
    List<Entity> entities = Lists.newArrayList();
    while (entitiesIterator.hasNext()) {
      Entity entity = entitiesIterator.next();
      // do something with the entity
      entities.add(entity);
    }
    transaction.commit();
    // [END getMultiple]
    return entities;
  }

  /**
   * Example of fetching a list of entities for several keys.
   */
  // [TARGET fetch(Key...)]
  // [VARIABLE "my_first_key_name"]
  // [VARIABLE "my_second_key_name"]
  public List<Entity> fetchEntitiesWithKeys(String firstKeyName, String secondKeyName) {
    Datastore datastore = transaction.datastore();
    // [START fetchEntitiesWithKeys]
    KeyFactory keyFactory = datastore.newKeyFactory().kind("MyKind");
    Key firstKey = keyFactory.newKey(firstKeyName);
    Key secondKey = keyFactory.newKey(secondKeyName);
    List<Entity> entities = transaction.fetch(firstKey, secondKey);
    for (Entity entity : entities) {
      // do something with the entity
    }
    transaction.commit();
    // [END fetchEntitiesWithKeys]
    return entities;
  }

  /**
   * Example of running a query to find all entities with an ancestor.
   */
  // [TARGET run(Query)]
  // [VARIABLE "my_parent_key_name"]
  public List<Entity> run(String parentKeyName) {
    Datastore datastore = transaction.datastore();
    // [START run]
    KeyFactory keyFactory = datastore.newKeyFactory().kind("ParentKind");
    Key parentKey = keyFactory.newKey(parentKeyName);
    // Build a query
    Query<Entity> query = Query.entityQueryBuilder()
        .kind("MyKind")
        .filter(PropertyFilter.hasAncestor(parentKey))
        .build();
    QueryResults<Entity> results = transaction.run(query);
    List<Entity> entities = Lists.newArrayList();
    while (results.hasNext()) {
      Entity result = results.next();
      // do something with result
      entities.add(result);
    }
    transaction.commit();
    // [END run]
    return entities;
  }

  /**
   * Example of committing a transaction.
   */
  // [TARGET commit()]
  public Key commit() {
    Datastore datastore = transaction.datastore();
    // [START commit]
    // create an entity
    KeyFactory keyFactory = datastore.newKeyFactory().kind("MyKind");
    Key key = datastore.allocateId(keyFactory.newKey());
    Entity entity = Entity.builder(key).set("description", "commit()").build();

    // add the entity and commit
    try {
      transaction.put(entity);
      transaction.commit();
    } catch (DatastoreException ex) {
      // handle exception
    }
    // [END commit]

    return key;
  }

  /**
   * Example of rolling back a transaction.
   */
  // [TARGET rollback()]
  public Key rollback() {
    Datastore datastore = transaction.datastore();
    // [START rollback]
    // create an entity
    KeyFactory keyFactory = datastore.newKeyFactory().kind("MyKind");
    Key key = datastore.allocateId(keyFactory.newKey());
    Entity entity = Entity.builder(key).set("description", "rollback()").build();

    // add the entity and rollback
    transaction.put(entity);
    transaction.rollback();
    // calling transaction.commit() now would fail
    // [END rollback]
    return key;
  }

  /**
   * Example of verifying if a transaction is active.
   */
  // [TARGET active()]
  public Key active() {
    Datastore datastore = transaction.datastore();
    // [START active]
    // create an entity
    KeyFactory keyFactory = datastore.newKeyFactory().kind("MyKind");
    Key key = datastore.allocateId(keyFactory.newKey());
    Entity entity = Entity.builder(key).set("description", "active()").build();
    // calling transaction.active() now would return true
    try {
      // add the entity and commit
      transaction.put(entity);
      transaction.commit();
    } finally {
      // if committing succeeded
      // then transaction.active() will be false
      if (transaction.active()) {
        // otherwise it's true and we need to rollback
        transaction.rollback();
      }
    }
    // [END active]
    return key;
  }
}
