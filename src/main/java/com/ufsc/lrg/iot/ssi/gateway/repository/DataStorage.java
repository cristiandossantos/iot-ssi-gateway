/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.repository;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.ufsc.lrg.iot.ssi.gateway.model.IotDevice;
import org.bson.Document;

/**
 * MongoDB data storage class for managing device data and registration.
 */
public class DataStorage {
    private final String connectionString;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public DataStorage() {
        // MongoDB connection string
        connectionString = "mongodb://user:pass@localhost:27017";

        // Create MongoDB client
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase("mqttMongoDb");
        
        // Create a unique index on the hash field in the constructor
        createUniqueIndex("registered_devices", "hash");
    }

    /**
     * Create device data in the "subscribeData" collection.
     *
     * @param document The document to be inserted.
     */
    public void createDeviceData(Document document) {
        MongoCollection<Document> collection = database.getCollection("subscribeData");
        collection.insertOne(document);
    }

    /**
     * Read device data from the "subscribeData" collection.
     *
     * @return The "subscribeData" collection.
     */
    public MongoCollection<Document> readDeviceData() {
        return database.getCollection("subscribeData");
    }

    /**
     * Register a device in the "registered_devices" collection.
     *
     * @param deviceInfo The device information to be registered.
     */
    public void registerDevice(IotDevice deviceInfo) {
        MongoCollection<Document> collection = database.getCollection("registered_devices");

        // Create a document with the device information
        Document deviceDocument = new Document();
        deviceDocument.append("did", deviceInfo.getDID());
        deviceDocument.append("hash", deviceInfo.getDevice_hash());

        // Insert the document into the collection
        collection.insertOne(deviceDocument);
        System.out.println("Device document inserted successfully!");
    }

    public Document getDocumentById(String id) {
        MongoCollection<Document> collection = database.getCollection("registered_devices");

        // Query to find the document based on the hash
        Document query = new Document("device_id", id);

        // Execute the query and return the first matching document (or null if not found)
        return collection.find(query).first();
    }

    private void createUniqueIndex(String collectionName, String indexField) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Create a unique index on the hash field
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(new Document(indexField, 1), indexOptions);
    }
}
