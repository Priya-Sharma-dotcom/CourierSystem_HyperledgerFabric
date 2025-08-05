package _CourierSystem;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType
public abstract class Parcel {

    @Property
    public final String id;

    @Property
    public final String sender;

    @Property
    public final String receiver;

    @Property
    public final String status;

    public Parcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status
    ) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Parcel other = (Parcel) obj;
        return Objects.equals(id, other.id) &&
               Objects.equals(sender, other.sender) &&
               Objects.equals(receiver, other.receiver) &&
               Objects.equals(status, other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, status);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\" }";
    }
}
