# 📦 Parcel Tracking System using Hyperledger Fabric (Java Chaincode + Java SDK)

A complete Java-based project for managing a parcel delivery tracking system on Hyperledger Fabric. This includes:

* Smart contract (chaincode) written in **Java**
* Multiple parcel types via inheritance
* Client application using the **Fabric Java SDK**
* End-to-end workflow: from deploying chaincode to invoking/querying via SDK

---

## 🗂️ Project Structure

```
.
├── ClientApp.java                     # Java client application to interact with Fabric
├── EnrollAdmin                        # Enroll admin identity
├── EnrollRegisterClientUser.java     # Register and enroll application user
├── StandardParcel.java                # Standard parcel subclass
├── ExpressParcel.java                # Express parcel subclass
├── FragileParcel.java                # Fragile parcel subclass
├── InternationalParcel.java          # International parcel subclass
├── Parcel.java                       # Base abstract class for all parcels
├── ParcelContract.java               # Smart contract with logic for all parcel types
├── Util.java                         # Helper methods for Fabric client
├── wallet/                           # Wallet to store identities (admin, appUser)
```

---

## 📊 Features

* Register parcels (Normal, Express, Fragile, International)
* Update parcel status and location
* Transfer parcel to next handler
* View parcel history
* Inheritance-based parcel classes
* Java client with full interaction using Fabric Gateway SDK

---

## 🚀 How to Run This Project

### ✅ Prerequisites

* Node.js, Fabric binaries installed
* Java 11+
* Hyperledger Fabric test network running (`test-network`)

### 🔄 1. Start Fabric Network

```bash
cd fabric-samples/test-network
./network.sh up createChannel -c mychannel -ca
```

### 💲 2. Deploy Java Chaincode

```bash
./network.sh deployCC -ccn parcel -ccp ../chaincode/parcel/java -ccl java
```

### 📅 3. Enroll Admin and Register User

```bash
java EnrollAdmin
java EnrollRegisterClientUser
```

### 🚪 4. Run Client Application

```bash
java ClientApp
```

This file handles:

* Connect to gateway
* Submit transactions: addParcel, transferParcel, queryParcel

---

## ⚖️ Smart Contract Highlights

```java
@Contract(name = "ParcelContract")
public class ParcelContract implements ContractInterface {
    @Transaction()
    public void addParcel(final Context ctx, String id, String type, String from, String to) {
        // Handles Normal, Express, Fragile, and International parcels
    }

    @Transaction()
    public void transferParcel(final Context ctx, String id, String newLocation) {
        // Update parcel current location
    }
}
```

---

## 📙 Technologies Used

* Hyperledger Fabric v2.5+
* Java SDK (fabric-gateway)
* Java Chaincode
* Maven
* Gradle

---

## 📚 License

MIT License. Use this as a template for your supply chain projects using Hyperledger Fabric.

---

