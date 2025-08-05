package fabricJavaClient;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class ClientApp {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public static void main(String[] args) throws Exception {

        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        Path config = Paths.get("..", "..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(config).discovery(true);

        try (Gateway gateway = builder.connect()) {

            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("CourierSystem");

            contract.submitTransaction("initLedger");
            System.out.println("successfully init ledger");

            contract.submitTransaction("addParcel",
                "P005",          // id
                "Alice",         // sender
                "Bob",           // receiver
                "Shipped",       // status
                "international", // type
                "",              // deliveryTime (not used)
                "",              // priorityHandling
                "",              // expressFee
                "",              // extraCushioning
                "",              // handlingInstructions
                "india",         // originCountry
                "uk",            // destinationCountry
                "gift"           // customsDeclaration
            );
            System.out.println("successfully added international parcel");

            contract.submitTransaction("addParcel",
                "P006", "Eve", "Frank", "Packed", "fragile",
                "", "", "", "true", "Handle with care", "", "", ""
            );

            contract.submitTransaction("updateParcelStatus", "P005", "InTransit");
            System.out.println("successfully updated status");

            contract.submitTransaction("transferParcelOwnership", "P005", "Priya");
            System.out.println("successfully transferred parcel");

            byte[] result = contract.evaluateTransaction("queryParcelById", "P005");
            String parcelDetails = new String(result);
            System.out.println("Parcel details - " + parcelDetails);
        }
    }
}
