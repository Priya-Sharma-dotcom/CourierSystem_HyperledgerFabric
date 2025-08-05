package _CourierSystem;

import org.hyperledger.fabric.contract.*;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.owlike.genson.Genson;

import java.util.*;

@Contract(name = "CourierSystem",
    info = @Info(
        title = "Courier System",
        description = "courier system chaincode",
        version = "0.0.1-SNAPSHOT"
))
@Default
public class ParcelContract implements ContractInterface {

    Genson genson = new Genson();

    enum Errors { parcel_not_found, parcel_already_exists }

    @Transaction
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Parcel> parcels = new ArrayList<>();

        parcels.add(new StandardParcel("P001", "Alice", "Bob", "Shipped", "1 day"));
        parcels.add(new ExpressParcel("P002", "Charlie", "David", "In Transit", 15.5, "2 days", true));
        parcels.add(new FragileParcel("P003", "Eve", "Frank", "Packed", "Handle with care", true));
        parcels.add(new InternationalParcel("P004", "Grace", "Heidi", "Customs", "USA", "India", "Electronics"));

        for (Parcel parcel : parcels) {
            stub.putStringState(parcel.getId(), genson.serialize(parcel));
        }
    }

    @Transaction
    public Parcel addParcel(final Context ctx, final String id, final String sender, final String receiver,
                            final String status, final String type,
                            final String deliveryTime, final String priorityHandling, final String expressFee,
                            final String extraCushioning, final String handlingInstructions,
                            final String originCountry, final String destinationCountry, final String customsDeclaration) {

        ChaincodeStub stub = ctx.getStub();

        if (!stub.getStringState(id).isEmpty()) {
            String errorMsg = String.format("parcel already exists at id %s", id);
            System.out.println(errorMsg);
            throw new ChaincodeException(errorMsg, Errors.parcel_already_exists.name());
        }

        Parcel parcel;

        switch (type.toLowerCase()) {
            case "standard":
                parcel = new StandardParcel(id, sender, receiver, status, deliveryTime);
                break;

            case "express":
                parcel = new ExpressParcel(id, sender, receiver, status,
                                           Double.parseDouble(expressFee), deliveryTime,
                                           Boolean.parseBoolean(priorityHandling));
                break;

            case "fragile":
                parcel = new FragileParcel(id, sender, receiver, status,
                                           handlingInstructions, Boolean.parseBoolean(extraCushioning));
                break;

            case "international":
                parcel = new InternationalParcel(id, sender, receiver, status,
                                                 originCountry, destinationCountry, customsDeclaration);
                break;

            default:
                throw new ChaincodeException("UNKNOWN PARCEL TYPE", type);
        }

        stub.putStringState(id, genson.serialize(parcel));
        return parcel;
    }

    @Transaction
    public Parcel updateParcelStatus(final Context ctx, final String id, final String newStatus) {
        ChaincodeStub stub = ctx.getStub();

        if (stub.getStringState(id).isEmpty()) {
            String errMsg = String.format("parcel at id %s not found", id);
            System.out.println(errMsg);
            throw new ChaincodeException(errMsg, Errors.parcel_not_found.toString());
        }

        Parcel parcel = genson.deserialize(stub.getStringState(id), Parcel.class);
        parcel.setStatus(newStatus);
        stub.putStringState(id, genson.serialize(parcel));
        return parcel;
    }

    @Transaction
    public Parcel transferParcelOwnership(final Context ctx, final String id, final String newReceiver) {
        ChaincodeStub stub = ctx.getStub();

        if (stub.getStringState(id).isEmpty()) {
            String errMsg = String.format("parcel at id %s not found", id);
            System.out.println(errMsg);
            throw new ChaincodeException(errMsg, Errors.parcel_not_found.toString());
        }

        Parcel parcel = genson.deserialize(stub.getStringState(id), Parcel.class);
        parcel.setReceiver(newReceiver);
        stub.putStringState(id, genson.serialize(parcel));
        return parcel;
    }

    @Transaction
    public Parcel queryParcelById(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();

        if (stub.getStringState(id) == null || stub.getStringState(id).isEmpty()) {
            String errMsg = String.format("parcel not found at id %s", id);
            System.out.println(errMsg);
            throw new ChaincodeException(errMsg, Errors.parcel_not_found.toString());
        }

        Parcel parcel = genson.deserialize(stub.getStringState(id), Parcel.class);
        return parcel;
    }
}

